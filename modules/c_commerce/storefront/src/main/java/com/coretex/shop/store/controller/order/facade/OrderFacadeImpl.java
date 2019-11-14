package com.coretex.shop.store.controller.order.facade;

import com.coretex.core.business.constants.Constants;
import com.coretex.core.business.exception.ConversionException;
import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.business.services.catalog.product.PricingService;
import com.coretex.core.business.services.catalog.product.ProductService;
import com.coretex.core.business.services.catalog.product.attribute.ProductAttributeService;
import com.coretex.core.business.services.catalog.product.file.DigitalProductService;
import com.coretex.core.business.services.customer.CustomerService;
import com.coretex.core.business.services.customer.attribute.CustomerOptionService;
import com.coretex.core.business.services.customer.attribute.CustomerOptionValueService;
import com.coretex.core.business.services.order.OrderService;
import com.coretex.core.business.services.reference.country.CountryService;
import com.coretex.core.business.services.reference.currency.CurrencyService;
import com.coretex.core.business.services.reference.language.LanguageService;
import com.coretex.core.business.services.reference.zone.ZoneService;
import com.coretex.core.business.services.shipping.DeliveryService;
import com.coretex.core.business.services.shipping.ShippingQuoteService;
import com.coretex.core.business.services.shipping.ShippingService;
import com.coretex.core.business.services.shoppingcart.ShoppingCartService;
import com.coretex.core.business.services.system.EmailService;
import com.coretex.core.business.services.user.GroupService;
import com.coretex.core.business.utils.CoreConfiguration;
import com.coretex.core.model.order.OrderCriteria;
import com.coretex.core.model.order.OrderList;
import com.coretex.core.model.order.OrderSummary;
import com.coretex.core.model.order.OrderTotalSummary;
import com.coretex.core.model.payments.Payment;
import com.coretex.core.model.shipping.ShippingProduct;
import com.coretex.core.model.shipping.ShippingQuote;
import com.coretex.core.model.shipping.ShippingSummary;
import com.coretex.enums.commerce_core_model.OrderStatusEnum;
import com.coretex.items.commerce_core_model.BillingItem;
import com.coretex.items.commerce_core_model.CustomerItem;
import com.coretex.items.commerce_core_model.DeliveryItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.OrderAttributeItem;
import com.coretex.items.commerce_core_model.OrderItem;
import com.coretex.items.commerce_core_model.OrderProductItem;
import com.coretex.items.commerce_core_model.OrderStatusHistoryItem;
import com.coretex.items.commerce_core_model.OrderTotalItem;
import com.coretex.items.commerce_core_model.ProductAvailabilityItem;
import com.coretex.items.commerce_core_model.ProductItem;
import com.coretex.items.commerce_core_model.ShoppingCartEntryItem;
import com.coretex.items.commerce_core_model.ShoppingCartItem;
import com.coretex.items.commerce_core_model.TransactionItem;
import com.coretex.items.core.CountryItem;
import com.coretex.shop.model.customer.PersistableCustomer;
import com.coretex.shop.model.customer.ReadableCustomer;
import com.coretex.shop.model.order.OrderEntity;
import com.coretex.shop.model.order.PersistableOrder;
import com.coretex.shop.model.order.PersistableOrderApi;
import com.coretex.shop.model.order.PersistableOrderProduct;
import com.coretex.shop.model.order.ReadableOrder;
import com.coretex.shop.model.order.ReadableOrderList;
import com.coretex.shop.model.order.ReadableOrderProduct;
import com.coretex.shop.model.order.ShopOrder;
import com.coretex.shop.model.order.total.OrderTotal;
import com.coretex.shop.model.order.transaction.ReadableTransaction;
import com.coretex.shop.populator.customer.CustomerPopulator;
import com.coretex.shop.populator.customer.PersistableCustomerPopulator;
import com.coretex.shop.populator.order.OrderProductPopulator;
import com.coretex.shop.populator.order.PersistableOrderApiPopulator;
import com.coretex.shop.populator.order.ReadableOrderPopulator;
import com.coretex.shop.populator.order.ReadableOrderProductPopulator;
import com.coretex.shop.populator.order.ShoppingCartItemPopulator;
import com.coretex.shop.populator.order.transaction.PersistablePaymentPopulator;
import com.coretex.shop.populator.order.transaction.ReadableTransactionPopulator;
import com.coretex.shop.store.controller.customer.facade.CustomerFacade;
import com.coretex.shop.store.controller.shoppingCart.facade.ShoppingCartFacade;
import com.coretex.shop.utils.EmailTemplatesUtils;
import com.coretex.shop.utils.ImageFilePath;
import com.coretex.shop.utils.LabelUtils;
import com.coretex.shop.utils.LocaleUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service("orderFacade")
public class OrderFacadeImpl implements OrderFacade {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(OrderFacadeImpl.class);


	@Resource
	private OrderService orderService;
	@Resource
	private ProductService productService;
	@Resource
	private ProductAttributeService productAttributeService;
	@Resource
	private ShoppingCartService shoppingCartService;
	@Resource
	private DigitalProductService digitalProductService;
	@Resource
	private CustomerService customerService;
	@Resource
	private ShippingService shippingService;
	@Resource
	private CustomerFacade customerFacade;
	@Resource
	private PricingService pricingService;
	@Resource
	private ShoppingCartFacade shoppingCartFacade;
	@Resource
	private CurrencyService currencyService;
	@Resource
	private ShippingQuoteService shippingQuoteService;
	@Resource
	private CoreConfiguration coreConfiguration;

	@Resource
	private CustomerOptionValueService customerOptionValueService;

	@Resource
	private CustomerOptionService customerOptionService;


	@Resource
	private LanguageService languageService;


	@Resource
	private CountryService countryService;

	@Resource
	private GroupService groupService;

	@Resource
	private ZoneService zoneService;

	@Resource
	private EmailTemplatesUtils emailTemplatesUtils;

	@Resource
	@Qualifier("img")
	private ImageFilePath imageUtils;


	@Override
	public ShopOrder initializeOrder(MerchantStoreItem store, CustomerItem customer,
									 ShoppingCartItem shoppingCart, LanguageItem language) throws Exception {

		//assert not null shopping cart items

		ShopOrder order = new ShopOrder();

		OrderStatusEnum orderStatus = OrderStatusEnum.ORDERED;
		order.setOrderStatus(orderStatus);

		if (customer == null) {
			customer = this.initEmptyCustomer(store);
		}

		PersistableCustomer persistableCustomer = persistableCustomer(customer, store, language);
		order.setCustomer(persistableCustomer);

		//keep list of shopping cart items for core price calculation
		List<ShoppingCartEntryItem> items = new ArrayList<ShoppingCartEntryItem>(shoppingCart.getLineItems());
		order.setShoppingCartItems(items);

		return order;
	}


	@Override
	public OrderTotalSummary calculateOrderTotal(MerchantStoreItem store,
												 ShopOrder order, LanguageItem language) throws Exception {


		CustomerItem customer = customerFacade.getCustomerModel(order.getCustomer(), store, language);
		OrderTotalSummary summary = this.calculateOrderTotal(store, customer, order, language);
		this.setOrderTotals(order, summary);
		return summary;
	}

	@Override
	public OrderTotalSummary calculateOrderTotal(MerchantStoreItem store,
												 PersistableOrder order, LanguageItem language) throws Exception {

		List<PersistableOrderProduct> orderProducts = order.getOrderProductItems();

		ShoppingCartItemPopulator populator = new ShoppingCartItemPopulator();
		populator.setProductAttributeService(productAttributeService);
		populator.setProductService(productService);
		populator.setShoppingCartService(shoppingCartService);

		List<ShoppingCartEntryItem> items = new ArrayList<ShoppingCartEntryItem>();
		for (PersistableOrderProduct orderProduct : orderProducts) {
			ShoppingCartEntryItem item = populator.populate(orderProduct, new ShoppingCartEntryItem(), store, language);
			items.add(item);
		}


		CustomerItem customer = customer(order.getCustomer(), store, language);

		OrderTotalSummary summary = this.calculateOrderTotal(store, customer, order, language);

		return summary;
	}

	@Override
	public OrderTotalSummary calculateOrderTotal(MerchantStoreItem store, CustomerItem customer, PersistableOrder order, LanguageItem language) throws Exception {

		OrderTotalSummary orderTotalSummary = null;

		OrderSummary summary = new OrderSummary();


		if (order instanceof ShopOrder) {
			ShopOrder o = (ShopOrder) order;
			summary.setProducts(o.getShoppingCartItems());

			if (o.getShippingSummary() != null) {
				summary.setShippingSummary(o.getShippingSummary());
			}
			orderTotalSummary = orderService.caculateOrderTotal(summary, customer, store, language);
		} else {
			//need Set of ShoppingCartEntryItem
			//PersistableOrder not implemented
			throw new Exception("calculateOrderTotal not yet implemented for PersistableOrder");
		}

		return orderTotalSummary;

	}


	private PersistableCustomer persistableCustomer(CustomerItem customer, MerchantStoreItem store, LanguageItem language) throws Exception {

		PersistableCustomerPopulator customerPopulator = new PersistableCustomerPopulator();
		PersistableCustomer persistableCustomer = customerPopulator.populate(customer, new PersistableCustomer(), store, language);
		return persistableCustomer;

	}

	private CustomerItem customer(PersistableCustomer customer, MerchantStoreItem store, LanguageItem language) throws Exception {
		CustomerPopulator populator = new CustomerPopulator();
		populator.setCountryService(countryService);
		populator.setCustomerOptionService(customerOptionService);
		populator.setCustomerOptionValueService(customerOptionValueService);
		populator.setLanguageService(languageService);
		populator.setZoneService(zoneService);
		populator.setGroupService(groupService);
		CustomerItem cust = populator.populate(customer, new CustomerItem(), store, language);
		return cust;

	}

	private void setOrderTotals(OrderEntity order, OrderTotalSummary summary) {

		List<OrderTotal> totals = new ArrayList<OrderTotal>();
		List<OrderTotalItem> orderTotals = summary.getTotals();
		for (OrderTotalItem t : orderTotals) {
			OrderTotal total = new OrderTotal();
			total.setCode(t.getOrderTotalCode());
			total.setTitle(t.getTitle());
			total.setValue(t.getValue());
			totals.add(total);
		}

		order.setTotals(totals);

	}


	/**
	 * Submitted object must be valided prior to the invocation of this method
	 */
	@Override
	public OrderItem processOrder(ShopOrder order, CustomerItem customer, MerchantStoreItem store,
								  LanguageItem language) throws ServiceException {

		return this.processOrderModel(order, customer, null, store, language);

	}

	@Override
	public OrderItem processOrder(ShopOrder order, CustomerItem customer, TransactionItem transaction, MerchantStoreItem store,
								  LanguageItem language) throws ServiceException {

		return this.processOrderModel(order, customer, transaction, store, language);

	}

	private OrderItem processOrderModel(ShopOrder order, CustomerItem customer, TransactionItem transaction, MerchantStoreItem store,
										LanguageItem language) throws ServiceException {

		try {

			OrderItem modelOrder = new OrderItem();
			modelOrder.setDatePurchased(new Date());
			modelOrder.setBilling(customer.getBilling());
			modelOrder.setDelivery(customer.getDelivery());
			modelOrder.setPaymentModuleCode(order.getPaymentModule());
			modelOrder.setCustomerAgreement(order.isCustomerAgreed());
			modelOrder.setLocale(LocaleUtils.getLocale(store).toString());//set the store locale based on the country for order $ formatting

			List<ShoppingCartEntryItem> shoppingCartItems = order.getShoppingCartItems();
			Set<OrderProductItem> orderProducts = new LinkedHashSet<OrderProductItem>();

			if (!StringUtils.isBlank(order.getComments())) {
				OrderStatusHistoryItem statusHistory = new OrderStatusHistoryItem();
				statusHistory.setStatus(OrderStatusEnum.ORDERED);
				statusHistory.setOrder(modelOrder);
				statusHistory.setDateAdded(new Date());
				statusHistory.setComments(order.getComments());
				modelOrder.getOrderHistory().add(statusHistory);
			}

			OrderProductPopulator orderProductPopulator = new OrderProductPopulator();
			orderProductPopulator.setDigitalProductService(digitalProductService);
			orderProductPopulator.setProductAttributeService(productAttributeService);
			orderProductPopulator.setProductService(productService);
			orderProductPopulator.setPricingService(pricingService);

			for (ShoppingCartEntryItem item : shoppingCartItems) {

				/**
				 * Before processing order quantity of item must be > 0
				 */

				ProductItem product = item.getProduct();
				if (product == null) {
					throw new ServiceException(ServiceException.EXCEPTION_INVENTORY_MISMATCH);
				}

				for (ProductAvailabilityItem availability : product.getAvailabilities()) {
					if (availability.getRegion().equals(Constants.ALL_REGIONS)) {
						int qty = availability.getProductQuantity();
						if (qty < item.getQuantity()) {
							throw new ServiceException(ServiceException.EXCEPTION_INVENTORY_MISMATCH);
						}
					}
				}


				OrderProductItem orderProduct = new OrderProductItem();
				orderProduct = orderProductPopulator.populate(item, orderProduct, store, language);
				orderProduct.setOrder(modelOrder);
				orderProducts.add(orderProduct);
			}

			modelOrder.setOrderProducts(orderProducts);

			OrderTotalSummary summary = order.getOrderTotalSummary();
			List<OrderTotalItem> totals = summary.getTotals();

			//re-order totals
			Collections.sort(
					totals,
					new Comparator<OrderTotalItem>() {
						public int compare(OrderTotalItem x, OrderTotalItem y) {
							if (x.getSortOrder() == y.getSortOrder())
								return 0;
							return x.getSortOrder() < y.getSortOrder() ? -1 : 1;
						}

					});

			Set<OrderTotalItem> modelTotals = new LinkedHashSet<OrderTotalItem>();
			for (OrderTotalItem total : totals) {
				total.setOrder(modelOrder);
				modelTotals.add(total);
			}

			modelOrder.setOrderTotal(modelTotals);
			modelOrder.setTotal(order.getOrderTotalSummary().getTotal());

			//order misc objects
			modelOrder.setCurrency(store.getCurrency());
			modelOrder.setMerchant(store);


			//customer object
			orderCustomer(customer, modelOrder, language);

			//populate shipping information
			if (!StringUtils.isBlank(order.getShippingModule())) {
				modelOrder.setShippingModuleCode(order.getShippingModule());
			}

			modelOrder.setPaymentModuleCode(order.getPaymentModule());

			if (transaction != null) {
				orderService.processOrder(modelOrder, customer, order.getShoppingCartItems(), summary, null, store);
			} else {
				orderService.processOrder(modelOrder, customer, order.getShoppingCartItems(), summary, null, transaction, store);
			}


			return modelOrder;

		} catch (ServiceException se) {//may be invalid credit card
			throw se;
		} catch (Exception e) {
			throw new ServiceException(e);
		}

	}

	private void orderCustomer(CustomerItem customer, OrderItem order, LanguageItem language) throws Exception {

		//populate customer
		order.setBilling(customer.getBilling());
		order.setDelivery(customer.getDelivery());
		order.setCustomerEmailAddress(customer.getEmail());
		order.setCustomerId(customer.getUuid());


	}


	@Override
	public CustomerItem initEmptyCustomer(MerchantStoreItem store) {

		CustomerItem customer = new CustomerItem();
		BillingItem billing = new BillingItem();
		billing.setCountry(store.getCountry());
		billing.setZone(store.getZone());
		billing.setState(store.getStoreStateProvince());
		/** empty postal code for initial quote **/
		//billing.setPostalCode(store.getStorepostalcode());
		customer.setBilling(billing);

		DeliveryItem delivery = new DeliveryItem();
		delivery.setCountry(store.getCountry());
		delivery.setZone(store.getZone());
		delivery.setState(store.getStoreStateProvince());
		/** empty postal code for initial quote **/
		//delivery.setPostalCode(store.getStorepostalcode());
		customer.setDelivery(delivery);

		return customer;
	}


	@Override
	public void refreshOrder(ShopOrder order, MerchantStoreItem store,
							 CustomerItem customer, ShoppingCartItem shoppingCart, LanguageItem language)
			throws Exception {
		if (customer == null && order.getCustomer() != null) {
			order.getCustomer().setUuid(null);//reset customer id
		}

		if (customer != null) {
			PersistableCustomer persistableCustomer = persistableCustomer(customer, store, language);
			order.setCustomer(persistableCustomer);
		}

		List<ShoppingCartEntryItem> items = new ArrayList<>(shoppingCart.getLineItems());
		order.setShoppingCartItems(items);

		return;
	}

	@Override
	public List<CountryItem> getShipToCountry(MerchantStoreItem store, LanguageItem language) throws Exception {

		return shippingService.getShipToCountryList(store, language);

	}


	/**
	 * ShippingSummary contains the subset of information
	 * of a ShippingQuote
	 */
	@Override
	public ShippingSummary getShippingSummary(ShippingQuote quote,
											  MerchantStoreItem store, LanguageItem language) {

		ShippingSummary summary = null;
		if (quote.getSelectedShippingOption() != null) {


			summary = new ShippingSummary();
			summary.setFreeShipping(quote.isFreeShipping());
			summary.setTaxOnShipping(quote.isApplyTaxOnShipping());
			summary.setHandling(quote.getHandlingFees());
			summary.setShipping(quote.getSelectedShippingOption().getOptionPrice());
			summary.setShippingOption(quote.getSelectedShippingOption().getOptionName());
			summary.setShippingModule(quote.getShippingModuleCode());
			summary.setShippingOptionCode(quote.getSelectedShippingOption().getOptionCode());

			if (quote.getDeliveryAddress() != null) {

				summary.setDeliveryAddress(quote.getDeliveryAddress());


			}


		}

		return summary;
	}

	@Override
	public void validateOrder(ShopOrder order, BindingResult bindingResult, Map<String, String> messagesResult, MerchantStoreItem store, Locale locale) throws ServiceException {


		Validate.notNull(messagesResult, "messagesResult should not be null");

	}


	@Override
	public ReadableOrderList getReadableOrderList(MerchantStoreItem store,
												  CustomerItem customer, int start, int maxCount, LanguageItem language) throws Exception {

		OrderCriteria criteria = new OrderCriteria();
		criteria.setStartIndex(start);
		criteria.setMaxCount(maxCount);
		criteria.setCustomerId(customer.getUuid());

		return this.getReadableOrderList(criteria, store, language);

	}

	@Override
	public ReadableOrderList getReadableOrderList(int start, int maxCount, String draw) throws Exception {

		OrderCriteria criteria = new OrderCriteria();
		criteria.setStartIndex(start);
		criteria.setMaxCount(maxCount);

		OrderList orderList = orderService.getOrders(criteria);

		ReadableOrderPopulator orderPopulator = new ReadableOrderPopulator();
		List<OrderItem> orders = orderList.getOrders();
		ReadableOrderList returnList = new ReadableOrderList();

		if (CollectionUtils.isEmpty(orders)) {
			returnList.setTotal(0);
			return null;
		}

		List<ReadableOrder> readableOrders = new ArrayList<ReadableOrder>();
		for (OrderItem order : orders) {
			ReadableOrder readableOrder = new ReadableOrder();
			orderPopulator.populate(order, readableOrder, null, null);
			readableOrders.add(readableOrder);

		}
		returnList.setOrders(readableOrders);
		returnList.setTotal(orderList.getTotalCount());

		returnList.setRecordsFiltered(orderList.getTotalCount());
		returnList.setRecordsTotal(orderList.getTotalCount());

		if (StringUtils.isNotEmpty(draw)) {
			returnList.setDraw(Integer.parseInt(draw));
		}
		return returnList;

	}



	private ReadableOrderList populateOrderList(final OrderList orderList, final MerchantStoreItem store, final LanguageItem language) {
		List<OrderItem> orders = orderList.getOrders();
		ReadableOrderList returnList = new ReadableOrderList();
		if (CollectionUtils.isEmpty(orders)) {
			LOGGER.info("OrderItem list if empty..Returning empty list");
			returnList.setTotal(0);
			//returnList.setMessage("No results for store code " + store);
			return returnList;
		}

		ReadableOrderPopulator orderPopulator = new ReadableOrderPopulator();
		Locale locale = LocaleUtils.getLocale(language);
		orderPopulator.setLocale(locale);

		List<ReadableOrder> readableOrders = new ArrayList<ReadableOrder>();
		for (OrderItem order : orders) {
			ReadableOrder readableOrder = new ReadableOrder();
			try {
				orderPopulator.populate(order, readableOrder, store, language);
				setOrderProductList(order, locale, store, language, readableOrder);
			} catch (ConversionException ex) {
				LOGGER.error("Error while converting order to order data", ex);

			}
			readableOrders.add(readableOrder);

		}

		returnList.setTotal(orderList.getTotalCount());
		returnList.setOrders(readableOrders);
		return returnList;

	}

	private void setOrderProductList(final OrderItem order, final Locale locale, final MerchantStoreItem store, final LanguageItem language, final ReadableOrder readableOrder) throws ConversionException {
		List<ReadableOrderProduct> orderProducts = new ArrayList<ReadableOrderProduct>();
		for (OrderProductItem p : order.getOrderProducts()) {
			ReadableOrderProductPopulator orderProductPopulator = new ReadableOrderProductPopulator();
			orderProductPopulator.setLocale(locale);
			orderProductPopulator.setProductService(productService);
			orderProductPopulator.setPricingService(pricingService);
			orderProductPopulator.setimageUtils(imageUtils);
			ReadableOrderProduct orderProduct = new ReadableOrderProduct();
			orderProductPopulator.populate(p, orderProduct, store, language);

			//image

			//attributes


			orderProducts.add(orderProduct);
		}

		readableOrder.setProducts(orderProducts);
	}


	private ReadableOrderList getReadableOrderList(OrderCriteria criteria, MerchantStoreItem store, LanguageItem language) throws Exception {

		OrderList orderList = orderService.listByStore(store, criteria);

		ReadableOrderPopulator orderPopulator = new ReadableOrderPopulator();
		Locale locale = LocaleUtils.getLocale(language);
		orderPopulator.setLocale(locale);

		List<OrderItem> orders = orderList.getOrders();
		ReadableOrderList returnList = new ReadableOrderList();

		if (CollectionUtils.isEmpty(orders)) {
			returnList.setTotal(0);
			//returnList.setMessage("No results for store code " + store);
			return null;
		}

		List<ReadableOrder> readableOrders = new ArrayList<ReadableOrder>();
		for (OrderItem order : orders) {
			ReadableOrder readableOrder = new ReadableOrder();
			orderPopulator.populate(order, readableOrder, store, language);
			readableOrders.add(readableOrder);

		}

		returnList.setTotal(orderList.getTotalCount());
		return this.populateOrderList(orderList, store, language);


	}


	@Override
	public ReadableOrderList getReadableOrderList(MerchantStoreItem store,
												  int start, int maxCount, LanguageItem language) throws Exception {

		OrderCriteria criteria = new OrderCriteria();
		criteria.setStartIndex(start);
		criteria.setMaxCount(maxCount);

		return this.getReadableOrderList(criteria, store, language);
	}


	@Override
	public ReadableOrder getReadableOrder(UUID orderId, MerchantStoreItem store,
										  LanguageItem language) throws Exception {


		OrderItem modelOrder = orderService.getById(orderId);
		if (modelOrder == null) {
			throw new Exception("OrderItem not found with id " + orderId);
		}

		ReadableOrder readableOrder = new ReadableOrder();

		UUID customerId = modelOrder.getCustomerId();
		if (customerId != null) {
			ReadableCustomer readableCustomer = customerFacade.getCustomerById(customerId, store, language);
			if (readableCustomer == null) {
				LOGGER.warn("CustomerItem id " + customerId + " not found in order " + orderId);
			} else {
				readableOrder.setCustomer(readableCustomer);
			}
		}

		ReadableOrderPopulator orderPopulator = new ReadableOrderPopulator();
		orderPopulator.populate(modelOrder, readableOrder, store, language);

		//order products
		List<ReadableOrderProduct> orderProducts = new ArrayList<ReadableOrderProduct>();
		for (OrderProductItem p : modelOrder.getOrderProducts()) {
			ReadableOrderProductPopulator orderProductPopulator = new ReadableOrderProductPopulator();
			orderProductPopulator.setProductService(productService);
			orderProductPopulator.setPricingService(pricingService);
			orderProductPopulator.setimageUtils(imageUtils);

			ReadableOrderProduct orderProduct = new ReadableOrderProduct();
			orderProductPopulator.populate(p, orderProduct, store, language);
			orderProducts.add(orderProduct);
		}

		readableOrder.setProducts(orderProducts);

		return readableOrder;
	}

	@Override
	public OrderItem processOrder(PersistableOrderApi order, CustomerItem customer, MerchantStoreItem store, LanguageItem language, Locale locale)
			throws ServiceException {

		PersistableOrderApiPopulator populator = new PersistableOrderApiPopulator();
		populator.setCurrencyService(currencyService);
		populator.setCustomerService(customerService);
		populator.setDigitalProductService(digitalProductService);
		populator.setProductAttributeService(productAttributeService);
		populator.setProductService(productService);
		populator.setShoppingCartService(shoppingCartService);


		try {

			OrderItem modelOrder = new OrderItem();
			populator.populate(order, modelOrder, store, language);

			UUID shoppingCartId = order.getShoppingCartId();
			ShoppingCartItem cart = shoppingCartService.getById(shoppingCartId, store);

			if (cart == null) {
				throw new ServiceException("Shopping cart with id " + shoppingCartId + " does not exist");
			}

			Set<ShoppingCartEntryItem> shoppingCartItems = cart.getLineItems();

			List<ShoppingCartEntryItem> items = new ArrayList<ShoppingCartEntryItem>(shoppingCartItems);

			Set<OrderProductItem> orderProducts = new LinkedHashSet<OrderProductItem>();

			OrderProductPopulator orderProductPopulator = new OrderProductPopulator();
			orderProductPopulator.setDigitalProductService(digitalProductService);
			orderProductPopulator.setProductAttributeService(productAttributeService);
			orderProductPopulator.setProductService(productService);

			for (ShoppingCartEntryItem item : shoppingCartItems) {
				OrderProductItem orderProduct = new OrderProductItem();
				orderProduct = orderProductPopulator.populate(item, orderProduct, store, language);
				orderProduct.setOrder(modelOrder);
				orderProducts.add(orderProduct);
			}

			modelOrder.setOrderProducts(orderProducts);

			if (order.getAttributes() != null && order.getAttributes().size() > 0) {
				Set<OrderAttributeItem> attrs = new HashSet<OrderAttributeItem>();
				for (com.coretex.shop.model.order.OrderAttribute attribute : order.getAttributes()) {
					OrderAttributeItem attr = new OrderAttributeItem();
					attr.setKey(attribute.getKey());
					attr.setValue(attribute.getValue());
					attr.setOrder(modelOrder);
					attrs.add(attr);
				}
				modelOrder.setOrderAttributes(attrs);
			}

			//requires Shipping information (need a quote id calculated)
			ShippingSummary shippingSummary = null;

			//get shipping quote if asked for
			if (order.getShippingQuote() != null) {
				shippingSummary = shippingQuoteService.getShippingSummary(order.getShippingQuote(), store);
				if (shippingSummary != null) {
					modelOrder.setShippingModuleCode(shippingSummary.getShippingModule());
				}
			}

			//requires OrderItem Totals, this needs recalculation and then compare total with the amount sent as part
			//of process order request. If totals does not match, an error should be thrown.

			OrderTotalSummary orderTotalSummary = null;

			OrderSummary orderSummary = new OrderSummary();
			orderSummary.setShippingSummary(shippingSummary);
			List<ShoppingCartEntryItem> itemsSet = new ArrayList<ShoppingCartEntryItem>(cart.getLineItems());
			orderSummary.setProducts(itemsSet);

			orderTotalSummary = orderService.caculateOrderTotal(orderSummary, customer, store, language);

			if (order.getPayment().getAmount() == null) {
				throw new ConversionException("Requires Payment.amount");
			}

			String submitedAmount = order.getPayment().getAmount();


			BigDecimal calculatedAmount = orderTotalSummary.getTotal();
			String strCalculatedTotal = pricingService.getStringAmount(calculatedAmount, store);

			//compare both prices
			if (!submitedAmount.equals(strCalculatedTotal)) {
				throw new ConversionException("Payment.amount does not match what the system has calculated " + strCalculatedTotal + " please recalculate the order and submit again");
			}

			modelOrder.setTotal(calculatedAmount);
			List<OrderTotalItem> totals = orderTotalSummary.getTotals();
			Set<OrderTotalItem> set = new HashSet<OrderTotalItem>();

			if (!CollectionUtils.isEmpty(totals)) {
				for (OrderTotalItem total : totals) {
					total.setOrder(modelOrder);
					set.add(total);
				}
			}
			modelOrder.setOrderTotal(set);

			PersistablePaymentPopulator paymentPopulator = new PersistablePaymentPopulator();
			paymentPopulator.setPricingService(pricingService);
			Payment paymentModel = new Payment();
			paymentPopulator.populate(order.getPayment(), paymentModel, store, language);

			modelOrder = orderService.processOrder(modelOrder, customer, items, orderTotalSummary, paymentModel, store);


			//delete cart
			try {
				shoppingCartFacade.deleteShoppingCart(cart.getShoppingCartCode(), store);
			} catch (Exception e) {
				LOGGER.error("Cannot delete cart " + cart.getUuid(), e);
			}

			if ("true".equals(coreConfiguration.getProperty("ORDER_EMAIL_API"))) {
				//send email
				try {

					//send order confirmation email to customer
					emailTemplatesUtils.sendOrderEmail(customer.getEmail(), customer, modelOrder, locale, language, store, coreConfiguration.getProperty("CONTEXT_PATH"));

					if (orderService.hasDownloadFiles(modelOrder)) {
						emailTemplatesUtils.sendOrderDownloadEmail(customer, modelOrder, store, locale, coreConfiguration.getProperty("CONTEXT_PATH"));
					}

					//send order confirmation email to merchant
					emailTemplatesUtils.sendOrderEmail(store.getStoreEmailAddress(), customer, modelOrder, locale, language, store, coreConfiguration.getProperty("CONTEXT_PATH"));


				} catch (Exception e) {
					LOGGER.error("Cannot send order confirmation email", e);
				}
			}

			return modelOrder;

		} catch (Exception e) {
			throw new ServiceException(e);
		}

	}


	@Override
	public ReadableOrderList getCapturableOrderList(MerchantStoreItem store, Date startDate, Date endDate,
													LanguageItem language) throws Exception {

		//get all transactions for the given date
		List<OrderItem> orders = orderService.getCapturableOrders(store, startDate, endDate);

		ReadableOrderPopulator orderPopulator = new ReadableOrderPopulator();
		Locale locale = LocaleUtils.getLocale(language);
		orderPopulator.setLocale(locale);

		ReadableOrderList returnList = new ReadableOrderList();

		if (CollectionUtils.isEmpty(orders)) {
			returnList.setTotal(0);
			//returnList.setMessage("No results for store code " + store);
			return null;
		}

		List<ReadableOrder> readableOrders = new ArrayList<ReadableOrder>();
		for (OrderItem order : orders) {
			ReadableOrder readableOrder = new ReadableOrder();
			orderPopulator.populate(order, readableOrder, store, language);
			readableOrders.add(readableOrder);

		}

		returnList.setTotal(orders.size());
		returnList.setOrders(readableOrders);

		return returnList;
	}


}
