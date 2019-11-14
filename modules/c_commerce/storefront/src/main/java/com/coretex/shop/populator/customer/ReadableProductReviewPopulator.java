package com.coretex.shop.populator.customer;

import com.coretex.core.business.exception.ConversionException;
import com.coretex.core.populators.AbstractDataPopulator;
import com.coretex.items.commerce_core_model.CustomerReviewItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.shop.model.customer.ReadableCustomerReview;

public class ReadableProductReviewPopulator extends AbstractDataPopulator<CustomerReviewItem, ReadableCustomerReview> {

	@Override
	public ReadableCustomerReview populate(CustomerReviewItem source, ReadableCustomerReview target, MerchantStoreItem store,
										   LanguageItem language) throws ConversionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ReadableCustomerReview createTarget() {
		// TODO Auto-generated method stub
		return null;
	}

}
