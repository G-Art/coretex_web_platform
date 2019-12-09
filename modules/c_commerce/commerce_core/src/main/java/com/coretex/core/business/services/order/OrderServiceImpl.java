package com.coretex.core.business.services.order;

import com.coretex.core.business.constants.Constants;

import com.coretex.core.business.modules.order.InvoiceModule;
import com.coretex.core.business.repositories.order.OrderDao;
import com.coretex.core.business.services.catalog.product.PricingService;
import com.coretex.core.business.services.catalog.product.ProductService;
import com.coretex.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.coretex.core.business.services.customer.CustomerService;
import com.coretex.core.model.catalog.product.price.FinalPrice;
import com.coretex.core.model.order.OrderCriteria;
import com.coretex.core.model.order.OrderList;
import com.coretex.core.model.order.OrderSummary;
import com.coretex.core.model.order.OrderSummaryType;
import com.coretex.core.model.order.OrderTotalSummary;
import com.coretex.core.model.order.OrderValueType;
import com.coretex.core.model.shipping.ShippingConfiguration;
import com.coretex.enums.commerce_core_model.OrderStatusEnum;
import com.coretex.enums.commerce_core_model.OrderTotalTypeEnum;
import com.coretex.items.commerce_core_model.CustomerItem;
import com.coretex.items.core.LocaleItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.OrderItem;
import com.coretex.items.commerce_core_model.OrderProductItem;
import com.coretex.items.commerce_core_model.OrderStatusHistoryItem;
import com.coretex.items.commerce_core_model.OrderTotalItem;
import com.coretex.items.commerce_core_model.ProductAvailabilityItem;
import com.coretex.items.commerce_core_model.ProductItem;
import com.coretex.items.commerce_core_model.ShoppingCartEntryItem;
import com.coretex.items.commerce_core_model.ShoppingCartItem;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;


@Service("orderService")
public class OrderServiceImpl extends SalesManagerEntityServiceImpl<OrderItem> implements OrderService {

	private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);

	@Resource
	private InvoiceModule invoiceModule;

	@Resource
	private PricingService pricingService;

	@Resource
	private ProductService productService;

	@Resource
	private CustomerService customerService;

	private final OrderDao orderDao;

	public OrderServiceImpl(OrderDao orderDao) {
		super(orderDao);
		this.orderDao = orderDao;
	}

	@Override
	public Map getStaticForPeriod(Date from) {
		if (Objects.isNull(from)) {
			return orderDao.getOrderStatistic();
		}
		return orderDao.getOrderStatistic(from);
	}

	@Override
	public void addOrderStatusHistory(OrderItem order, OrderStatusHistoryItem history) {
		order.getOrderHistory().add(history);
		history.setOrder(order);
		save(order);
	}

	@Override
	public OrderItem processOrder(OrderItem order, CustomerItem customer, List<ShoppingCartEntryItem> items, OrderTotalSummary summary, MerchantStoreItem store) {

		return this.process(order, customer, items, summary, store);
	}


	private OrderItem process(OrderItem order, CustomerItem customer, List<ShoppingCartEntryItem> items, OrderTotalSummary summary, MerchantStoreItem store) {


		Validate.notNull(order, "OrderItem cannot be null");
		Validate.notNull(customer, "CustomerItem cannot be null (even if anonymous order)");
		Validate.notEmpty(items, "ShoppingCartItem items cannot be null");
		Validate.notNull(store, "MerchantStoreItem cannot be null");
		Validate.notNull(summary, "OrderItem total Summary cannot be null");

		/**
		 * decrement inventory
		 */
		Set<OrderProductItem> products = order.getOrderProducts();
		for (OrderProductItem orderProduct : products) {
			orderProduct.getProductQuantity();
			ProductItem p = productService.getByCode(orderProduct.getSku());
			for (ProductAvailabilityItem availability : p.getAvailabilities()) {
				int qty = availability.getProductQuantity();

				qty = qty - orderProduct.getProductQuantity();
				availability.setProductQuantity(qty);
			}
			productService.save(p);
		}


		if (order.getOrderHistory() == null || order.getOrderHistory().size() == 0 || order.getStatus() == null) {
			OrderStatusEnum status = order.getStatus();
			if (status == null) {
				status = OrderStatusEnum.ORDERED;
				order.setStatus(status);
			}
			Set<OrderStatusHistoryItem> statusHistorySet = new HashSet<>();
			OrderStatusHistoryItem statusHistory = new OrderStatusHistoryItem();
			statusHistory.setStatus(status);
			statusHistory.setDateAdded(new Date());
			statusHistory.setOrder(order);
			statusHistorySet.add(statusHistory);
			order.setOrderHistory(statusHistorySet);

		}

		if (customer.getUuid() == null) {
			customerService.create(customer);
		}

		order.setCustomerId(customer.getUuid());

		this.create(order);

		//TODO post order processing


		return order;


	}

	private OrderTotalSummary caculateOrder(OrderSummary summary, CustomerItem customer, final MerchantStoreItem store, final LocaleItem language) {

		OrderTotalSummary totalSummary = new OrderTotalSummary();
		List<OrderTotalItem> orderTotals = new ArrayList<OrderTotalItem>();
		Map<String, OrderTotalItem> otherPricesTotals = new HashMap<String, OrderTotalItem>();

		ShippingConfiguration shippingConfiguration = null;

		BigDecimal grandTotal = new BigDecimal(0);
		grandTotal.setScale(2, RoundingMode.HALF_UP);

		//price by item
		/**
		 * qty * price
		 * subtotal
		 */
		BigDecimal subTotal = new BigDecimal(0);
		subTotal.setScale(2, RoundingMode.HALF_UP);
		for (ShoppingCartEntryItem item : summary.getProducts()) {

			BigDecimal st = item.getItemPrice().multiply(new BigDecimal(item.getQuantity()));
			item.setSubTotal(st);
			subTotal = subTotal.add(st);
			//Other prices
			FinalPrice finalPrice = pricingService.calculateProductPrice(item.getProduct(), Lists.newArrayList(), customer);
			if (finalPrice != null) {
				List<FinalPrice> otherPrices = finalPrice.getAdditionalPrices();
				if (otherPrices != null) {
					for (FinalPrice price : otherPrices) {
						if (!price.isDefaultPrice()) {
							OrderTotalItem itemSubTotal = otherPricesTotals.get(price.getProductPrice().getCode());

							if (itemSubTotal == null) {
								itemSubTotal = new OrderTotalItem();
								//itemSubTotal.setText(Constants.OT_ITEM_PRICE_MODULE_CODE);
								itemSubTotal.setTitle(Constants.OT_ITEM_PRICE_MODULE_CODE);
								itemSubTotal.setOrderTotalCode(price.getProductPrice().getCode());
								itemSubTotal.setOrderTotalType(OrderTotalTypeEnum.PRODUCT);
								itemSubTotal.setSortOrder(0);
								otherPricesTotals.put(price.getProductPrice().getCode(), itemSubTotal);
							}

							BigDecimal orderTotalValue = itemSubTotal.getValue();
							if (orderTotalValue == null) {
								orderTotalValue = new BigDecimal(0);
								orderTotalValue.setScale(2, RoundingMode.HALF_UP);
							}

							orderTotalValue = orderTotalValue.add(price.getFinalPrice());
							itemSubTotal.setValue(orderTotalValue);
							subTotal = subTotal.add(price.getFinalPrice());
						}
					}
				}
			}

		}

		//only in order page, otherwise invokes too many processing
//		if (OrderSummaryType.ORDERTOTAL.name().equals(summary.getOrderSummaryType().name())) {
//
//			//Post processing order total variation modules for sub total calculation - drools, custom modules
//			//may affect the sub total
//			OrderTotalVariation orderTotalVariation = orderTotalService.findOrderTotalVariation(summary, customer, store, language);
//
//			int currentCount = 10;
//
//			if (CollectionUtils.isNotEmpty(orderTotalVariation.getVariations())) {
//				for (OrderTotalItem variation : orderTotalVariation.getVariations()) {
//					variation.setSortOrder(currentCount++);
//					orderTotals.add(variation);
//					subTotal = subTotal.subtract(variation.getValue());
//				}
//			}
//
//		}


		totalSummary.setSubTotal(subTotal);
		grandTotal = grandTotal.add(subTotal);

		OrderTotalItem orderTotalSubTotal = new OrderTotalItem();
		orderTotalSubTotal.setOrderTotalType(OrderTotalTypeEnum.SUBTOTAL);
		orderTotalSubTotal.setOrderTotalCode("order.total.subtotal");
		orderTotalSubTotal.setTitle(Constants.OT_SUBTOTAL_MODULE_CODE);
		//orderTotalSubTotal.setText("order.total.subtotal");
		orderTotalSubTotal.setSortOrder(5);
		orderTotalSubTotal.setValue(subTotal);

		orderTotals.add(orderTotalSubTotal);


		//shipping
		if (summary.getShippingSummary() != null) {


			OrderTotalItem shippingSubTotal = new OrderTotalItem();
			shippingSubTotal.setOrderTotalType(OrderTotalTypeEnum.SHIPPING);
			shippingSubTotal.setOrderTotalCode("order.total.shipping");
			shippingSubTotal.setTitle(Constants.OT_SHIPPING_MODULE_CODE);
			//shippingSubTotal.setText("order.total.shipping");
			shippingSubTotal.setSortOrder(100);

			orderTotals.add(shippingSubTotal);

			if (!summary.getShippingSummary().isFreeShipping()) {
				shippingSubTotal.setValue(summary.getShippingSummary().getShipping());
				grandTotal = grandTotal.add(summary.getShippingSummary().getShipping());
			} else {
				shippingSubTotal.setValue(new BigDecimal(0));
				grandTotal = grandTotal.add(new BigDecimal(0));
			}

		}

		//tax
//		List<TaxItem> taxes = taxService.calculateTax(summary, customer, store, language);
//		if (taxes != null && taxes.size() > 0) {
//			BigDecimal totalTaxes = new BigDecimal(0);
//			totalTaxes.setScale(2, RoundingMode.HALF_UP);
//			int taxCount = 200;
//			for (TaxItem tax : taxes) {
//
//				OrderTotalItem taxLine = new OrderTotalItem();
//				taxLine.setModule(Constants.OT_TAX_MODULE_CODE);
//				taxLine.setOrderTotalType(OrderTotalTypeEnum.TAX);
//				taxLine.setOrderTotalCode(tax.getLabel());
//				taxLine.setSortOrder(taxCount);
//				taxLine.setTitle(Constants.OT_TAX_MODULE_CODE);
//				taxLine.setText(tax.getLabel());
//				taxLine.setValue(tax.getItemPrice());
//
//				totalTaxes = totalTaxes.add(tax.getItemPrice());
//				orderTotals.add(taxLine);
//				//grandTotal=grandTotal.add(tax.getItemPrice());
//
//				taxCount++;
//
//			}
//			grandTotal = grandTotal.add(totalTaxes);
//			totalSummary.setTaxTotal(totalTaxes);
//		}

		// grand total
		OrderTotalItem orderTotal = new OrderTotalItem();
		orderTotal.setOrderTotalType(OrderTotalTypeEnum.TOTAL);
		orderTotal.setOrderTotalCode("order.total.total");
		orderTotal.setTitle(Constants.OT_TOTAL_MODULE_CODE);
		//orderTotal.setText("order.total.total");
		orderTotal.setSortOrder(500);
		orderTotal.setValue(grandTotal);
		orderTotals.add(orderTotal);

		totalSummary.setTotal(grandTotal);
		totalSummary.setTotals(orderTotals);
		return totalSummary;

	}


	@Override
	public OrderTotalSummary caculateOrderTotal(final OrderSummary orderSummary, final CustomerItem customer, final MerchantStoreItem store, final LocaleItem language) {
		Validate.notNull(orderSummary, "OrderItem summary cannot be null");
		Validate.notNull(orderSummary.getProducts(), "OrderItem summary.products cannot be null");
		Validate.notNull(store, "MerchantStoreItem cannot be null");
		Validate.notNull(customer, "CustomerItem cannot be null");

		try {
			return caculateOrder(orderSummary, customer, store, language);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}


	@Override
	public OrderTotalSummary caculateOrderTotal(final OrderSummary orderSummary, final MerchantStoreItem store, final LocaleItem language) {
		Validate.notNull(orderSummary, "OrderItem summary cannot be null");
		Validate.notNull(orderSummary.getProducts(), "OrderItem summary.products cannot be null");
		Validate.notNull(store, "MerchantStoreItem cannot be null");

		try {
			return caculateOrder(orderSummary, null, store, language);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	private OrderTotalSummary caculateShoppingCart(final ShoppingCartItem shoppingCart, final CustomerItem customer, final MerchantStoreItem store, final LocaleItem language) {


		OrderSummary orderSummary = new OrderSummary();
		orderSummary.setOrderSummaryType(OrderSummaryType.SHOPPINGCART);

		List<ShoppingCartEntryItem> itemsSet = new ArrayList<ShoppingCartEntryItem>(shoppingCart.getLineItems());
		orderSummary.setProducts(itemsSet);


		return this.caculateOrder(orderSummary, customer, store, language);

	}


	/**
	 * <p>Method will be used to calculate Shopping cart total as well will update price for each
	 * line items.
	 * </p>
	 *
	 * @param shoppingCart
	 * @param customer
	 * @param store
	 * @param language
	 * @return {@link OrderTotalSummary}
	 * @
	 */
	@Override
	public OrderTotalSummary calculateShoppingCartTotal(
			final ShoppingCartItem shoppingCart, final CustomerItem customer, final MerchantStoreItem store,
			final LocaleItem language) {
		Validate.notNull(shoppingCart, "OrderItem summary cannot be null");
		Validate.notNull(customer, "Customery cannot be null");
		Validate.notNull(store, "MerchantStoreItem cannot be null.");
		try {
			return caculateShoppingCart(shoppingCart, customer, store, language);
		} catch (Exception e) {
			LOGGER.error("Error while calculating shopping cart total" + e);
			throw new RuntimeException(e);
		}

	}


	/**
	 * <p>Method will be used to calculate Shopping cart total as well will update price for each
	 * line items.
	 * </p>
	 *
	 * @param shoppingCart
	 * @param store
	 * @param language
	 * @return {@link OrderTotalSummary}
	 * @
	 */
	@Override
	public OrderTotalSummary calculateShoppingCartTotal(
			final ShoppingCartItem shoppingCart, final MerchantStoreItem store, final LocaleItem language) {
		Validate.notNull(shoppingCart, "OrderItem summary cannot be null");
		Validate.notNull(store, "MerchantStoreItem cannot be null");

		return caculateShoppingCart(shoppingCart, null, store, language);
	}

	@Override
	public ByteArrayOutputStream generateInvoice(final MerchantStoreItem store, final OrderItem order, final LocaleItem language) {

		Validate.notNull(order.getOrderProducts(), "OrderItem products cannot be null");
		Validate.notNull(order.getOrderTotal(), "OrderItem totals cannot be null");

		try {
			ByteArrayOutputStream stream = invoiceModule.createInvoice(store, order, language);
			return stream;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}


	}

	@Override
	public OrderItem getOrder(final UUID orderId) {
		return getByUUID(orderId);
	}



/*    @Override
    public List<OrderItem> listByStore(final MerchantStoreItem merchantStore) {
        return listByField(Order_.merchant, merchantStore);
    }*/

	@Override
	public OrderList listByStore(final MerchantStoreItem store, final OrderCriteria criteria) {

		return orderDao.listByStore(store, criteria);
	}

	@Override
	public OrderList getOrders(final OrderCriteria criteria) {
		return orderDao.getOrders(criteria);
	}


	@Override
	public void saveOrUpdate(final OrderItem order) {
		super.save(order);
	}


}
