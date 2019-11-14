package com.coretex.core.business.utils;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import com.coretex.core.business.constants.Constants;
import com.coretex.items.commerce_core_model.ProductItem;
import com.coretex.items.commerce_core_model.ProductAttributeItem;
import com.coretex.items.commerce_core_model.ProductOptionItem;
import com.coretex.items.commerce_core_model.ProductOptionValueItem;
import com.coretex.items.commerce_core_model.ProductAvailabilityItem;


public class CatalogServiceHelper {

	/**
	 * Filters descriptions and set the appropriate language
	 *
	 * @param p
	 * @param language
	 */
	public static void setToLanguage(ProductItem p, int language) {


		Set<ProductAttributeItem> attributes = p.getAttributes();
		if (attributes != null) {

			for (ProductAttributeItem attribute : attributes) {

//				ProductOptionItem po = attribute.getProductOption();
//				Set<ProductOptionDescription> spod = po.getDescriptions();
//				if(spod!=null) {
//					Set<ProductOptionDescription> podDescriptions = new HashSet<ProductOptionDescription>();
//					for(ProductOptionDescription pod : spod) {
//						//System.out.println("    ProductOptionDescription : " + pod.getProductOptionName());
//						if(pod.getLanguage().getId()==language) {
//							podDescriptions.add(pod);
//						}
//					}
//					po.setDescriptions(podDescriptions);
//				}

				ProductOptionValueItem pov = attribute.getProductOptionValue();


//				Set<ProductOptionValueDescription> spovd = pov.getDescriptions();
//				if(spovd!=null) {
//					Set<ProductOptionValueDescription> povdDescriptions = new HashSet();
//					for(ProductOptionValueDescription povd : spovd) {
//						if(povd.getLanguage().getId()==language) {
//							povdDescriptions.add(povd);
//						}
//					}
//					pov.setDescriptions(povdDescriptions);
//				}

			}
		}

	}

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
