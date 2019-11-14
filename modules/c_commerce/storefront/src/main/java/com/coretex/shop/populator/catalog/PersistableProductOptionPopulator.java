package com.coretex.shop.populator.catalog;

import com.coretex.core.business.exception.ConversionException;
import com.coretex.core.business.services.reference.language.LanguageService;
import com.coretex.core.populators.AbstractDataPopulator;
import com.coretex.items.commerce_core_model.ProductOptionItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.shop.model.catalog.product.attribute.PersistableProductOption;
import com.coretex.shop.model.catalog.product.attribute.ProductOptionDescription;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.Validate;


public class PersistableProductOptionPopulator extends
		AbstractDataPopulator<PersistableProductOption, ProductOptionItem> {

	private LanguageService languageService;

	public LanguageService getLanguageService() {
		return languageService;
	}

	public void setLanguageService(LanguageService languageService) {
		this.languageService = languageService;
	}

	@Override
	public ProductOptionItem populate(PersistableProductOption source,
									  ProductOptionItem target, MerchantStoreItem store, LanguageItem language)
			throws ConversionException {
		Validate.notNull(languageService, "Requires to set LanguageService");


		try {


			target.setMerchantStore(store);
			target.setProductOptionSortOrder(source.getOrder());
			target.setCode(source.getCode());

			if (!CollectionUtils.isEmpty(source.getDescriptions())) {

				for (ProductOptionDescription desc : source.getDescriptions()) {
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
	protected ProductOptionItem createTarget() {
		return null;
	}

}
