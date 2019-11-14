package com.coretex.shop.populator.order.transaction;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.Validate;

import com.coretex.core.business.exception.ConversionException;
import com.coretex.core.business.services.catalog.product.PricingService;
import com.coretex.core.populators.AbstractDataPopulator;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.core.model.payments.Payment;
import com.coretex.enums.commerce_core_model.PaymentTypeEnum;
import com.coretex.enums.commerce_core_model.TransactionTypeEnum;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.shop.model.order.transaction.PersistablePayment;

public class PersistablePaymentPopulator extends AbstractDataPopulator<PersistablePayment, Payment> {


	PricingService pricingService;


	@Override
	public Payment populate(PersistablePayment source, Payment target, MerchantStoreItem store, LanguageItem language)
			throws ConversionException {

		Validate.notNull(source, "PersistablePayment cannot be null");
		Validate.notNull(pricingService, "pricingService must be set");
		if (target == null) {
			target = new Payment();
		}

		try {

			target.setAmount(pricingService.getAmount(source.getAmount()));
			target.setModuleName(source.getPaymentModule());
			target.setPaymentType(PaymentTypeEnum.valueOf(source.getPaymentType()));
			target.setTransactionType(TransactionTypeEnum.valueOf(source.getTransactionType()));

			Map<String, String> metadata = new HashMap<String, String>();
			metadata.put("paymentToken", source.getPaymentToken());
			target.setPaymentMetaData(metadata);

			return target;

		} catch (Exception e) {
			throw new ConversionException(e);
		}
	}

	@Override
	protected Payment createTarget() {
		// TODO Auto-generated method stub
		return null;
	}

	public PricingService getPricingService() {
		return pricingService;
	}

	public void setPricingService(PricingService pricingService) {
		this.pricingService = pricingService;
	}

}
