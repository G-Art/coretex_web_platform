package com.coretex.shop.populator.order.transaction;

import com.coretex.items.commerce_core_model.MerchantStoreItem;
import org.apache.commons.lang3.Validate;

import com.coretex.core.business.exception.ConversionException;
import com.coretex.core.business.services.catalog.product.PricingService;
import com.coretex.core.business.services.order.OrderService;
import com.coretex.core.populators.AbstractDataPopulator;
import com.coretex.items.commerce_core_model.TransactionItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.shop.model.order.transaction.ReadableTransaction;
import com.coretex.shop.utils.DateUtil;


public class ReadableTransactionPopulator extends AbstractDataPopulator<TransactionItem, ReadableTransaction> {


	private OrderService orderService;
	private PricingService pricingService;

	@Override
	public ReadableTransaction populate(TransactionItem source, ReadableTransaction target, MerchantStoreItem store,
										LanguageItem language) throws ConversionException {


		Validate.notNull(source, "PersistableTransaction must not be null");
		Validate.notNull(orderService, "OrderService must not be null");
		Validate.notNull(pricingService, "OrderService must not be null");

		if (target == null) {
			target = new ReadableTransaction();
		}


		try {


			target.setAmount(pricingService.getDisplayAmount(source.getAmount(), store));
			target.setDetails(source.getDetails());
			target.setPaymentType(source.getPaymentType());
			target.setTransactionType(source.getTransactionType());
			target.setTransactionDate(DateUtil.formatDate(source.getTransactionDate()));
			target.setUuid(source.getUuid());

			if (source.getOrder() != null) {
				target.setOrderId(source.getOrder().getUuid());

			}

			return target;


		} catch (Exception e) {
			throw new ConversionException(e);
		}

	}

	@Override
	protected ReadableTransaction createTarget() {
		// TODO Auto-generated method stub
		return null;
	}

	public OrderService getOrderService() {
		return orderService;
	}

	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}

	public PricingService getPricingService() {
		return pricingService;
	}

	public void setPricingService(PricingService pricingService) {
		this.pricingService = pricingService;
	}

}
