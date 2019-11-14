package com.coretex.shop.populator.catalog;

import com.coretex.core.business.exception.ConversionException;
import com.coretex.core.populators.AbstractDataPopulator;
import com.coretex.items.commerce_core_model.ProductReviewItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.shop.model.catalog.product.ReadableProductReview;
import com.coretex.shop.model.customer.ReadableCustomer;
import com.coretex.shop.populator.customer.ReadableCustomerPopulator;
import com.coretex.shop.utils.DateUtil;

public class ReadableProductReviewPopulator extends
		AbstractDataPopulator<ProductReviewItem, ReadableProductReview> {

	@Override
	public ReadableProductReview populate(ProductReviewItem source,
										  ReadableProductReview target, MerchantStoreItem store, LanguageItem language)
			throws ConversionException {


		try {
			ReadableCustomerPopulator populator = new ReadableCustomerPopulator();
			ReadableCustomer customer = new ReadableCustomer();
			populator.populate(source.getCustomer(), customer, store, language);

			target.setUuid(source.getUuid());
			target.setDate(DateUtil.formatDate(source.getReviewDate()));
			target.setCustomer(customer);
			target.setRating(source.getReviewRating());
			target.setProductId(source.getProduct().getUuid());

			return target;

		} catch (Exception e) {
			throw new ConversionException("Cannot populate ProductReviewItem", e);
		}


	}

	@Override
	protected ReadableProductReview createTarget() {
		return null;
	}

}
