package com.coretex.shop.populator.customer;

import com.coretex.core.business.exception.ConversionException;
import com.coretex.core.business.services.reference.language.LanguageService;
import com.coretex.core.populators.AbstractDataPopulator;
import com.coretex.items.commerce_core_model.CustomerOptionItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.shop.model.customer.attribute.CustomerOptionDescription;
import com.coretex.shop.model.customer.attribute.PersistableCustomerOption;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.StringUtils;

public class PersistableCustomerOptionPopulator extends
		AbstractDataPopulator<PersistableCustomerOption, CustomerOptionItem> {


	private LanguageService languageService;

	@Override
	public CustomerOptionItem populate(PersistableCustomerOption source,
									   CustomerOptionItem target, MerchantStoreItem store, LanguageItem language)
			throws ConversionException {


		Validate.notNull(languageService, "Requires to set LanguageService");


		try {

			target.setCode(source.getCode());
			target.setMerchantStore(store);
			target.setSortOrder(source.getOrder());
			if (!StringUtils.isBlank(source.getType())) {
				target.setCustomerOptionType(source.getType());
			} else {
				target.setCustomerOptionType("TEXT");
			}
			target.setPublicOption(true);

			if (!CollectionUtils.isEmpty(source.getDescriptions())) {
				for (CustomerOptionDescription desc : source.getDescriptions()) {
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
	protected CustomerOptionItem createTarget() {
		return null;
	}

	public void setLanguageService(LanguageService languageService) {
		this.languageService = languageService;
	}

	public LanguageService getLanguageService() {
		return languageService;
	}

}
