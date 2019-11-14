package com.coretex.shop.populator.customer;

import com.coretex.core.business.exception.ConversionException;
import com.coretex.core.business.services.reference.language.LanguageService;
import com.coretex.core.populators.AbstractDataPopulator;
import com.coretex.items.commerce_core_model.CustomerOptionValueItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.shop.model.customer.attribute.CustomerOptionValueDescription;
import com.coretex.shop.model.customer.attribute.PersistableCustomerOptionValue;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.Validate;

public class PersistableCustomerOptionValuePopulator extends
		AbstractDataPopulator<PersistableCustomerOptionValue, CustomerOptionValueItem> {


	private LanguageService languageService;

	@Override
	public CustomerOptionValueItem populate(PersistableCustomerOptionValue source,
											CustomerOptionValueItem target, MerchantStoreItem store, LanguageItem language)
			throws ConversionException {


		Validate.notNull(languageService, "Requires to set LanguageService");


		try {

			target.setCode(source.getCode());
			target.setMerchantStore(store);
			target.setSortOrder(source.getOrder());

			if (!CollectionUtils.isEmpty(source.getDescriptions())) {
				for (CustomerOptionValueDescription desc : source.getDescriptions()) {
					LanguageItem lang = languageService.getByCode(desc.getLanguage());
					if (lang == null) {
						throw new ConversionException("LanguageItem is null for code " + desc.getLanguage() + " use language ISO code [en, fr ...]");
					}
					target.setName(desc.getName());
					target.setTitle(desc.getTitle());
				}
			}

		} catch (Exception e) {
			throw new ConversionException(e);
		}
		return target;
	}

	@Override
	protected CustomerOptionValueItem createTarget() {
		return null;
	}

	public void setLanguageService(LanguageService languageService) {
		this.languageService = languageService;
	}

	public LanguageService getLanguageService() {
		return languageService;
	}

}
