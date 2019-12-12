package com.coretex.shop.populator.order;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.coretex.items.commerce_core_model.OrderItem;
import com.coretex.items.core.LocaleItem;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.StringUtils;

import com.coretex.core.business.exception.ConversionException;
import com.coretex.core.business.services.catalog.product.ProductService;
import com.coretex.core.business.services.catalog.product.attribute.ProductAttributeService;
import com.coretex.core.business.services.customer.CustomerService;
import com.coretex.core.business.services.reference.currency.CurrencyService;
import com.coretex.core.business.services.shoppingcart.ShoppingCartService;
import com.coretex.core.populators.AbstractDataPopulator;
import com.coretex.items.commerce_core_model.BillingItem;
import com.coretex.items.commerce_core_model.DeliveryItem;
import com.coretex.items.commerce_core_model.CustomerItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.enums.commerce_core_model.OrderChannelEnum;
import com.coretex.enums.commerce_core_model.OrderStatusEnum;
import com.coretex.items.commerce_core_model.OrderStatusHistoryItem;
import com.coretex.items.cx_core.CurrencyItem;
import com.coretex.shop.model.order.PersistableOrderApi;
import com.coretex.shop.utils.LocaleUtils;

public class PersistableOrderApiPopulator extends AbstractDataPopulator<PersistableOrderApi, OrderItem> {


	private CurrencyService currencyService;
	private CustomerService customerService;
	private ShoppingCartService shoppingCartService;
	private ProductService productService;
	private ProductAttributeService productAttributeService;


	@Override
	public OrderItem populate(PersistableOrderApi source, OrderItem target, MerchantStoreItem store, LocaleItem language)
			throws ConversionException {


		Validate.notNull(currencyService, "currencyService must be set");
		Validate.notNull(customerService, "customerService must be set");
		Validate.notNull(shoppingCartService, "shoppingCartService must be set");
		Validate.notNull(productService, "productService must be set");
		Validate.notNull(productAttributeService, "productAttributeService must be set");
		Validate.notNull(source.getPayment(), "Payment cannot be null");

		try {

			if (target == null) {
				target = new OrderItem();
			}

			//target.setLocale(LocaleUtils.getLocale(store));

			target.setLocale(LocaleUtils.getLocale(store).toString());


			CurrencyItem currency = null;
			try {
				currency = currencyService.getByCode(source.getCurrency());
			} catch (Exception e) {
				throw new ConversionException("CurrencyItem not found for code " + source.getCurrency());
			}

			if (currency == null) {
				throw new ConversionException("CurrencyItem not found for code " + source.getCurrency());
			}

			//CustomerItem
			UUID customerId = source.getCustomerId();
			CustomerItem customer = customerService.getByUUID(customerId);

			if (customer == null) {
				throw new ConversionException("Curstomer with id " + source.getCustomerId() + " does not exist");
			}

			target.setCustomerId(customerId);
			target.setCustomerEmailAddress(customer.getEmail());

			DeliveryItem delivery = customer.getDelivery();
			target.setDelivery(delivery);

			BillingItem billing = customer.getBilling();
			target.setBilling(billing);


			target.setDatePurchased(new Date());
			target.setCurrency(currency);
			target.setCurrencyValue(new BigDecimal(0));
			target.setMerchant(store);
			target.setChannel(OrderChannelEnum.API);
			//need this
			target.setStatus(OrderStatusEnum.ORDERED);

			target.setCustomerAgreement(source.isCustomerAgreement());
			target.setConfirmedAddress(true);//force this to true, cannot perform this activity from the API


			if (!StringUtils.isBlank(source.getComments())) {
				OrderStatusHistoryItem statusHistory = new OrderStatusHistoryItem();
				statusHistory.setStatus(null);
				statusHistory.setOrder(target);
				statusHistory.setComments(source.getComments());
				target.getOrderHistory().add(statusHistory);
			}

			return target;

		} catch (Exception e) {
			throw new ConversionException(e);
		}
	}

	@Override
	protected OrderItem createTarget() {
		// TODO Auto-generated method stub
		return null;
	}


	public CurrencyService getCurrencyService() {
		return currencyService;
	}

	public void setCurrencyService(CurrencyService currencyService) {
		this.currencyService = currencyService;
	}

	public CustomerService getCustomerService() {
		return customerService;
	}

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}

	public ShoppingCartService getShoppingCartService() {
		return shoppingCartService;
	}

	public void setShoppingCartService(ShoppingCartService shoppingCartService) {
		this.shoppingCartService = shoppingCartService;
	}

	public ProductService getProductService() {
		return productService;
	}

	public void setProductService(ProductService productService) {
		this.productService = productService;
	}

	public ProductAttributeService getProductAttributeService() {
		return productAttributeService;
	}

	public void setProductAttributeService(ProductAttributeService productAttributeService) {
		this.productAttributeService = productAttributeService;
	}

}
