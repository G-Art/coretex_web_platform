
package com.coretex.shop.populator.manufacturer;

import com.coretex.core.business.exception.ConversionException;
import com.coretex.core.business.services.reference.language.LanguageService;
import com.coretex.core.populators.AbstractDataPopulator;
import com.coretex.items.commerce_core_model.ManufacturerItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.shop.model.catalog.manufacturer.ManufacturerDescription;
import com.coretex.shop.model.catalog.manufacturer.PersistableManufacturer;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.Validate;


/**
 * @author Carl Samson
 */


public class PersistableManufacturerPopulator extends AbstractDataPopulator<PersistableManufacturer, ManufacturerItem> {


	private LanguageService languageService;

	@Override
	public ManufacturerItem populate(PersistableManufacturer source,
									 ManufacturerItem target, MerchantStoreItem store, LanguageItem language)
			throws ConversionException {

		Validate.notNull(languageService, "Requires to set LanguageService");

		try {

			target.setMerchantStore(store);
			target.setCode(source.getCode());

			if (!CollectionUtils.isEmpty(source.getDescriptions())) {
				for (ManufacturerDescription description : source.getDescriptions()) {

					LanguageItem lang = languageService.getByCode(description.getLanguage());
					if (lang == null) {
						throw new ConversionException("LanguageItem is null for code " + description.getLanguage() + " use language ISO code [en, fr ...]");
					}
					target.setDescription(description.getDescription(), languageService.toLocale(lang, store));
					target.setName(description.getName(), languageService.toLocale(lang, store));
				}
			}

		} catch (Exception e) {
			throw new ConversionException(e);
		}


		return target;
	}

	@Override
	protected ManufacturerItem createTarget() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setLanguageService(LanguageService languageService) {
		this.languageService = languageService;
	}

	public LanguageService getLanguageService() {
		return languageService;
	}


}
