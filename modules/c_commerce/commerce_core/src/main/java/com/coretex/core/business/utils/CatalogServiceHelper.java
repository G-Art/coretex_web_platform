package com.coretex.core.business.utils;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import com.coretex.core.business.constants.Constants;
import com.coretex.items.commerce_core_model.ProductItem;
import com.coretex.items.commerce_core_model.ProductAttributeItem;
import com.coretex.items.commerce_core_model.ProductAvailabilityItem;


public class CatalogServiceHelper {


	/**
	 * Overwrites the availability in order to return 1 price / region
	 *
	 * @param product
	 * @param locale
	 */
	public static void setToAvailability(ProductItem product, Locale locale) {

		Set<ProductAvailabilityItem> availabilities = product.getAvailabilities();

		ProductAvailabilityItem defaultAvailability = null;
		ProductAvailabilityItem localeAvailability = null;

		for (ProductAvailabilityItem availability : availabilities) {

			if (availability.getRegion().equals(Constants.ALL_REGIONS)) {
				defaultAvailability = availability;
			}
			if (availability.getRegion().equals(locale.getCountry())) {
				localeAvailability = availability;
			}

		}

		if (defaultAvailability != null || localeAvailability != null) {
			Set<ProductAvailabilityItem> productAvailabilities = new HashSet<ProductAvailabilityItem>();
			if (defaultAvailability != null) {
				productAvailabilities.add(defaultAvailability);
			}
			if (localeAvailability != null) {
				productAvailabilities.add(localeAvailability);
			}
			product.setAvailabilities(productAvailabilities);
		}

	}

}
