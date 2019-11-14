package com.coretex.shop.populator.customer;

import java.util.Date;

import com.coretex.items.commerce_core_model.CustomerItem;
import com.coretex.items.commerce_core_model.CustomerReviewItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import org.apache.commons.lang3.Validate;

import com.coretex.core.business.exception.ConversionException;
import com.coretex.core.business.services.customer.CustomerService;
import com.coretex.core.business.services.reference.language.LanguageService;
import com.coretex.core.populators.AbstractDataPopulator;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.shop.model.customer.PersistableCustomerReview;
import com.coretex.shop.utils.DateUtil;

public class PersistableCustomerReviewPopulator extends AbstractDataPopulator<PersistableCustomerReview, CustomerReviewItem> {

	private CustomerService customerService;

	private LanguageService languageService;

	public LanguageService getLanguageService() {
		return languageService;
	}

	public void setLanguageService(LanguageService languageService) {
		this.languageService = languageService;
	}

	@Override
	public CustomerReviewItem populate(PersistableCustomerReview source, CustomerReviewItem target, MerchantStoreItem store,
									   LanguageItem language) throws ConversionException {

		Validate.notNull(customerService, "customerService cannot be null");
		Validate.notNull(languageService, "languageService cannot be null");
		Validate.notNull(source.getRating(), "Rating cannot bot be null");

		try {

			if (target == null) {
				target = new CustomerReviewItem();
			}

			if (source.getDate() == null) {
				String date = DateUtil.formatDate(new Date());
				source.setDate(date);
			}
			target.setReviewDate(DateUtil.getDate(source.getDate()));

			if (source.getUuid() != null) {
				source.setUuid(null);
			} else {
				target.setUuid(source.getUuid());
			}


			CustomerItem reviewer = customerService.getById(source.getCustomerId());
			CustomerItem reviewed = customerService.getById(source.getReviewedCustomer());

			target.setReviewRating(source.getRating());

			target.setCustomer(reviewer);
			target.setReviewedCustomer(reviewed);

			LanguageItem lang = languageService.getByCode(language.getCode());
			if (lang == null) {
				throw new ConversionException("Invalid language code, use iso codes (en, fr ...)");
			}

			target.setName("-");

		} catch (Exception e) {
			throw new ConversionException("Cannot populate CustomerReviewItem", e);
		}


		return target;
	}

	@Override
	protected CustomerReviewItem createTarget() {
		// TODO Auto-generated method stub
		return null;
	}

	public CustomerService getCustomerService() {
		return customerService;
	}

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}

}
