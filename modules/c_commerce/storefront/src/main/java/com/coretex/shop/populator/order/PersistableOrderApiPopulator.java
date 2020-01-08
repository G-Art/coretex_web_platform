package com.coretex.shop.populator.order;

import com.coretex.core.business.exception.ConversionException;
import com.coretex.core.business.services.catalog.product.ProductService;
import com.coretex.core.business.services.catalog.product.attribute.ProductAttributeService;
import com.coretex.core.business.services.reference.currency.CurrencyService;
import com.coretex.core.business.services.shoppingcart.ShoppingCartService;
import com.coretex.core.populators.AbstractDataPopulator;
import com.coretex.enums.commerce_core_model.OrderChannelEnum;
import com.coretex.enums.commerce_core_model.OrderStatusEnum;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.OrderItem;
import com.coretex.items.commerce_core_model.OrderStatusHistoryItem;
import com.coretex.items.core.LocaleItem;
import com.coretex.items.cx_core.CurrencyItem;
import com.coretex.shop.model.order.PersistableOrderApi;
import com.coretex.shop.utils.LocaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

public class PersistableOrderApiPopulator extends AbstractDataPopulator<PersistableOrderApi, OrderItem> {


	private CurrencyService currencyService;
	private ShoppingCartService shoppingCartService;
	private ProductService productService;
	private ProductAttributeService productAttributeService;


	@Override
	public OrderItem populate(PersistableOrderApi source, OrderItem target, MerchantStoreItem store, LocaleItem language)
			throws ConversionException {


		Validate.notNull(currencyService, "currencyService must be set");
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


			target.setCustomerId(customerId);

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
