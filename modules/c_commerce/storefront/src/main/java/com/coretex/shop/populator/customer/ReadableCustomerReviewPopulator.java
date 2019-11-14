package com.coretex.shop.populator.customer;

import com.coretex.core.business.exception.ConversionException;
import com.coretex.core.populators.AbstractDataPopulator;
import com.coretex.items.commerce_core_model.CustomerReviewItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.shop.model.customer.ReadableCustomer;
import com.coretex.shop.model.customer.ReadableCustomerReview;
import com.coretex.shop.utils.DateUtil;

public class ReadableCustomerReviewPopulator extends AbstractDataPopulator<CustomerReviewItem, ReadableCustomerReview> {

	@Override
	public ReadableCustomerReview populate(CustomerReviewItem source, ReadableCustomerReview target, MerchantStoreItem store,
										   LanguageItem language) throws ConversionException {

		try {

			if (target == null) {
				target = new ReadableCustomerReview();
			}

			if (source.getReviewDate() != null) {
				target.setDate(DateUtil.formatDate(source.getReviewDate()));
			}


			ReadableCustomer reviewed = new ReadableCustomer();
			reviewed.setUuid(source.getReviewedCustomer().getUuid());
			reviewed.setFirstName(source.getReviewedCustomer().getBilling().getFirstName());
			reviewed.setLastName(source.getReviewedCustomer().getBilling().getLastName());


			target.setUuid(source.getUuid());
			target.setCustomerId(source.getCustomer().getUuid());
			target.setReviewedCustomer(reviewed);
			target.setRating(source.getReviewRating());


		} catch (Exception e) {
			throw new ConversionException("Cannot populate ReadableCustomerReview", e);
		}


		return target;

	}

	@Override
	protected ReadableCustomerReview createTarget() {
		// TODO Auto-generated method stub
		return null;
	}

}
