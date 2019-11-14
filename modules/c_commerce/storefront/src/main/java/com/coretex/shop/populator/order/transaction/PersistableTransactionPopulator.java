package com.coretex.shop.populator.order.transaction;

import com.coretex.enums.commerce_core_model.TransactionTypeEnum;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.OrderItem;
import com.coretex.items.commerce_core_model.TransactionItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import org.apache.commons.lang3.Validate;

import com.coretex.core.business.exception.ConversionException;
import com.coretex.core.business.services.catalog.product.PricingService;
import com.coretex.core.business.services.order.OrderService;
import com.coretex.core.populators.AbstractDataPopulator;
import com.coretex.enums.commerce_core_model.PaymentTypeEnum;
import com.coretex.shop.model.order.transaction.PersistableTransaction;
import com.coretex.search.utils.DateUtil;

public class PersistableTransactionPopulator extends AbstractDataPopulator<PersistableTransaction, TransactionItem> {

	private OrderService orderService;
	private PricingService pricingService;

	@Override
	public TransactionItem populate(PersistableTransaction source, TransactionItem target, MerchantStoreItem store,
									LanguageItem language) throws ConversionException {

		Validate.notNull(source, "PersistableTransaction must not be null");
		Validate.notNull(orderService, "OrderService must not be null");
		Validate.notNull(pricingService, "OrderService must not be null");

		if (target == null) {
			target = new TransactionItem();
		}


		try {


			target.setAmount(pricingService.getAmount(source.getAmount()));
			target.setDetails(source.getDetails());
			target.setPaymentType(PaymentTypeEnum.valueOf(source.getPaymentType()));
			target.setTransactionType(TransactionTypeEnum.valueOf(source.getTransactionType()));
			target.setTransactionDate(DateUtil.formatDate(source.getTransactionDate()));

			if (source.getOrderId() != null) {
				OrderItem order = orderService.getById(source.getOrderId());
/*				if(source.getCustomerId() == null) {
					throw new ConversionException("Cannot add a transaction for an OrderItem without specyfing the customer");
				}*/

				if (order == null) {
					throw new ConversionException("OrderItem with id " + source.getOrderId() + "does not exist");
				}
				target.setOrder(order);
			}

			return target;


		} catch (Exception e) {
			throw new ConversionException(e);
		}

	}

	@Override
	protected TransactionItem createTarget() {
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
