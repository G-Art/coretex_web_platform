package com.coretex.shop.populator.order;

import com.coretex.core.business.exception.ConversionException;
import com.coretex.core.business.services.catalog.product.ProductService;
import com.coretex.core.business.services.catalog.product.attribute.ProductAttributeService;
import com.coretex.core.business.services.customer.CustomerService;
import com.coretex.core.business.services.reference.country.CountryService;
import com.coretex.core.business.services.reference.currency.CurrencyService;
import com.coretex.core.business.services.reference.zone.ZoneService;
import com.coretex.core.populators.AbstractDataPopulator;
import com.coretex.items.commerce_core_model.CustomerItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.OrderItem;
import com.coretex.items.commerce_core_model.OrderTotalItem;
import com.coretex.items.commerce_core_model.OrderProductItem;
import com.coretex.enums.commerce_core_model.OrderStatusEnum;
import com.coretex.items.commerce_core_model.OrderStatusHistoryItem;
import com.coretex.items.core.CountryItem;
import com.coretex.items.cx_core.CurrencyItem;
import com.coretex.items.core.LocaleItem;
import com.coretex.items.cx_core.ZoneItem;
import com.coretex.shop.model.customer.PersistableCustomer;
import com.coretex.shop.model.order.PersistableOrder;
import com.coretex.shop.model.order.PersistableOrderProduct;
import com.coretex.shop.model.order.total.OrderTotal;
import com.coretex.shop.utils.LocaleUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class PersistableOrderPopulator extends
		AbstractDataPopulator<PersistableOrder, OrderItem> {

	private CustomerService customerService;
	private CountryService countryService;
	private CurrencyService currencyService;


	private ZoneService zoneService;
	private ProductService productService;
	private ProductAttributeService productAttributeService;

	@Override
	public OrderItem populate(PersistableOrder source, OrderItem target,
							  MerchantStoreItem store, LocaleItem language) throws ConversionException {


		Validate.notNull(productService, "productService must be set");
		Validate.notNull(productAttributeService, "productAttributeService must be set");
		Validate.notNull(customerService, "customerService must be set");
		Validate.notNull(countryService, "countryService must be set");
		Validate.notNull(zoneService, "zoneService must be set");
		Validate.notNull(currencyService, "currencyService must be set");

		try {


			Map<String, CountryItem> countriesMap = countryService.getCountriesMap(language);
			Map<String, ZoneItem> zonesMap = zoneService.getZones(language);
			/** customer **/
			PersistableCustomer customer = source.getCustomer();
			if (customer != null) {
				if (customer.getUuid() != null) {
					CustomerItem modelCustomer = customerService.getByUUID(customer.getUuid());
					if (modelCustomer == null) {
						throw new ConversionException("CustomerItem id " + customer.getUuid() + " does not exists");
					}
					if (!modelCustomer.getMerchantStore().getUuid().equals(store.getUuid())) {
						throw new ConversionException("CustomerItem id " + customer.getUuid() + " does not exists for store " + store.getCode());
					}
					target.setCustomerId(modelCustomer.getUuid());
					target.setBilling(modelCustomer.getBilling());
					target.setDelivery(modelCustomer.getDelivery());
					target.setCustomerEmailAddress(source.getCustomer().getEmailAddress());


				}
			}

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

			target.setCurrency(currency);
			target.setDatePurchased(source.getDatePurchased());
			target.setCurrencyValue(new BigDecimal(0));
			target.setMerchant(store);
			target.setStatus(source.getOrderStatus());
			target.setCustomerAgreement(source.isCustomerAgreed());
			target.setConfirmedAddress(source.isConfirmedAddress());
			if (source.getPreviousOrderStatus() != null) {
				List<OrderStatusEnum> orderStatusList = source.getPreviousOrderStatus();
				for (OrderStatusEnum status : orderStatusList) {
					OrderStatusHistoryItem statusHistory = new OrderStatusHistoryItem();
					statusHistory.setStatus(status);
					statusHistory.setOrder(target);
					target.getOrderHistory().add(statusHistory);
				}
			}

			if (!StringUtils.isBlank(source.getComments())) {
				OrderStatusHistoryItem statusHistory = new OrderStatusHistoryItem();
				statusHistory.setStatus(null);
				statusHistory.setOrder(target);
				statusHistory.setComments(source.getComments());
				target.getOrderHistory().add(statusHistory);
			}

			List<PersistableOrderProduct> products = source.getOrderProductItems();
			if (CollectionUtils.isEmpty(products)) {
				throw new ConversionException("Requires at least 1 PersistableOrderProduct");
			}
			com.coretex.shop.populator.order.PersistableOrderProductPopulator orderProductPopulator = new PersistableOrderProductPopulator();
			orderProductPopulator.setProductAttributeService(productAttributeService);
			orderProductPopulator.setProductService(productService);

			for (PersistableOrderProduct orderProduct : products) {
				OrderProductItem modelOrderProduct = new OrderProductItem();
				orderProductPopulator.populate(orderProduct, modelOrderProduct, store, language);
				target.getOrderProducts().add(modelOrderProduct);
			}

			List<OrderTotal> orderTotals = source.getTotals();
			if (CollectionUtils.isNotEmpty(orderTotals)) {
				for (OrderTotal total : orderTotals) {
					OrderTotalItem totalModel = new OrderTotalItem();
					totalModel.setOrder(target);
					totalModel.setOrderTotalCode(total.getCode());
					totalModel.setTitle(total.getTitle());
					totalModel.setValue(total.getValue());
					target.getOrderTotal().add(totalModel);
				}
			}

		} catch (Exception e) {
			throw new ConversionException(e);
		}


		return target;
	}

	@Override
	protected OrderItem createTarget() {
		return null;
	}

	public void setProductService(ProductService productService) {
		this.productService = productService;
	}

	public ProductService getProductService() {
		return productService;
	}

	public void setProductAttributeService(ProductAttributeService productAttributeService) {
		this.productAttributeService = productAttributeService;
	}

	public ProductAttributeService getProductAttributeService() {
		return productAttributeService;
	}

	public CustomerService getCustomerService() {
		return customerService;
	}

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}

	public CountryService getCountryService() {
		return countryService;
	}

	public void setCountryService(CountryService countryService) {
		this.countryService = countryService;
	}

	public CurrencyService getCurrencyService() {
		return currencyService;
	}

	public void setCurrencyService(CurrencyService currencyService) {
		this.currencyService = currencyService;
	}

	public ZoneService getZoneService() {
		return zoneService;
	}

	public void setZoneService(ZoneService zoneService) {
		this.zoneService = zoneService;
	}

}
