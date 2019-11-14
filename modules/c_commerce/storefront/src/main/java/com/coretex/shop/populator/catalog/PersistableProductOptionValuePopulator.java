package com.coretex.shop.populator.catalog;

import com.coretex.core.business.exception.ConversionException;
import com.coretex.core.business.services.reference.language.LanguageService;
import com.coretex.core.populators.AbstractDataPopulator;
import com.coretex.items.commerce_core_model.ProductOptionValueItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.shop.model.catalog.product.attribute.PersistableProductOptionValue;
import com.coretex.shop.model.catalog.product.attribute.ProductOptionValueDescription;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.Validate;


/**
 * Converts a PersistableProductOptionValue to
 * a ProductOptionValueItem model object
 *
 * @author Carl Samson
 */
public class PersistableProductOptionValuePopulator extends
		AbstractDataPopulator<PersistableProductOptionValue, ProductOptionValueItem> {


	private LanguageService languageService;

	public LanguageService getLanguageService() {
		return languageService;
	}

	public void setLanguageService(LanguageService languageService) {
		this.languageService = languageService;
	}

	@Override
	public ProductOptionValueItem populate(PersistableProductOptionValue source,
										   ProductOptionValueItem target, MerchantStoreItem store, LanguageItem language)
			throws ConversionException {

		Validate.notNull(languageService, "Requires to set LanguageService");


		try {


			target.setMerchantStore(store);
			target.setProductOptionValueSortOrder(source.getOrder());
			target.setCode(source.getCode());

			if (!CollectionUtils.isEmpty(source.getDescriptions())) {
				for (ProductOptionValueDescription desc : source.getDescriptions()) {
					LanguageItem lang = languageService.getByCode(desc.getLanguage());
					if (lang == null) {
						throw new ConversionException("LanguageItem is null for code " + desc.getLanguage() + " use language ISO code [en, fr ...]");
					}
					target.setName(desc.getName(), languageService.toLocale(lang, store));
					target.setTitle(desc.getTitle(), languageService.toLocale(lang, store));
				}
			}

		} catch (Exception e) {
			throw new ConversionException(e);
		}


		return target;
	}

	@Override
	protected ProductOptionValueItem createTarget() {
		return null;
	}

}
