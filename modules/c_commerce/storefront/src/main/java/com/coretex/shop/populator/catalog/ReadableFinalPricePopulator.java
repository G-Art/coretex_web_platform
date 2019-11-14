package com.coretex.shop.populator.catalog;

import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import org.apache.commons.lang3.Validate;

import com.coretex.core.business.exception.ConversionException;
import com.coretex.core.business.services.catalog.product.PricingService;
import com.coretex.core.populators.AbstractDataPopulator;
import com.coretex.core.model.catalog.product.price.FinalPrice;
import com.coretex.shop.model.catalog.product.ReadableProductPrice;

public class ReadableFinalPricePopulator extends
		AbstractDataPopulator<FinalPrice, ReadableProductPrice> {


	private PricingService pricingService;

	public PricingService getPricingService() {
		return pricingService;
	}

	public void setPricingService(PricingService pricingService) {
		this.pricingService = pricingService;
	}

	@Override
	public ReadableProductPrice populate(FinalPrice source,
										 ReadableProductPrice target, MerchantStoreItem store, LanguageItem language)
			throws ConversionException {
		Validate.notNull(pricingService, "pricingService must be set");

		try {

			target.setOriginalPrice(pricingService.getDisplayAmount(source.getOriginalPrice(), store));
			if (source.isDiscounted()) {
				target.setDiscounted(true);
				target.setFinalPrice(pricingService.getDisplayAmount(source.getDiscountedPrice(), store));
			} else {
				target.setFinalPrice(pricingService.getDisplayAmount(source.getFinalPrice(), store));
			}

		} catch (Exception e) {
			throw new ConversionException("Exception while converting to ReadableProductPrice", e);
		}


		return target;
	}

	@Override
	protected ReadableProductPrice createTarget() {
		// TODO Auto-generated method stub
		return null;
	}

}
