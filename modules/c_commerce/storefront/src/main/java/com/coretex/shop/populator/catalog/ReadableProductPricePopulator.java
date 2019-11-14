package com.coretex.shop.populator.catalog;

import com.coretex.items.commerce_core_model.ProductPriceItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import org.apache.commons.lang3.Validate;

import com.coretex.core.business.exception.ConversionException;
import com.coretex.core.business.services.catalog.product.PricingService;
import com.coretex.core.populators.AbstractDataPopulator;
import com.coretex.core.model.catalog.product.price.FinalPrice;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.shop.model.catalog.product.ReadableProductPrice;


public class ReadableProductPricePopulator extends
		AbstractDataPopulator<ProductPriceItem, ReadableProductPrice> {


	private PricingService pricingService;

	public PricingService getPricingService() {
		return pricingService;
	}

	public void setPricingService(PricingService pricingService) {
		this.pricingService = pricingService;
	}

	@Override
	public ReadableProductPrice populate(ProductPriceItem source,
										 ReadableProductPrice target, MerchantStoreItem store, LanguageItem language)
			throws ConversionException {
		Validate.notNull(pricingService, "pricingService must be set");
		Validate.notNull(source.getProductAvailability(), "productPrice.availability cannot be null");
		Validate.notNull(source.getProductAvailability().getProduct(), "productPrice.availability.product cannot be null");

		try {

			FinalPrice finalPrice = pricingService.calculateProductPrice(source.getProductAvailability().getProduct());

			target.setOriginalPrice(pricingService.getDisplayAmount(source.getProductPriceAmount(), store));
			if (finalPrice.isDiscounted()) {
				target.setDiscounted(true);
				target.setFinalPrice(pricingService.getDisplayAmount(source.getProductPriceSpecialAmount(), store));
			} else {
				target.setFinalPrice(pricingService.getDisplayAmount(finalPrice.getOriginalPrice(), store));
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
