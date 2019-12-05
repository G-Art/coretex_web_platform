package com.coretex.core.business.services.shipping;


import com.coretex.core.model.shipping.ShippingConfiguration;
import com.coretex.core.model.shipping.ShippingMetaData;
import com.coretex.core.model.system.IntegrationConfiguration;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.core.CountryItem;
import com.coretex.items.core.LocaleItem;

import java.util.List;


public interface ShippingService {

	/**
	 * Returns a list of supported countries (ship to country list) configured by merchant
	 * when the merchant configured shipping National and has saved a list of ship to country
	 * from the list
	 *
	 * @param store
	 * @return
	 * @
	 */
	List<String> getSupportedCountries(MerchantStoreItem store)
			;

	void setSupportedCountries(MerchantStoreItem store,
							   List<String> countryCodes) ;


	/**
	 * Adds a Shipping configuration
	 *
	 * @param configuration
	 * @param store
	 * @
	 */
	void saveShippingQuoteModuleConfiguration(IntegrationConfiguration configuration,
											  MerchantStoreItem store) ;

	/**
	 * ShippingType (NATIONAL, INTERNATIONSL)
	 * ShippingBasisType (SHIPPING, BILLING)
	 * ShippingPriceOptionType (ALL, LEAST, HIGHEST)
	 * Packages
	 * Handling
	 *
	 * @param store
	 * @return
	 * @
	 */
	ShippingConfiguration getShippingConfiguration(MerchantStoreItem store)
			;

	/**
	 * Saves ShippingConfiguration for a given MerchantStoreItem
	 *
	 * @param shippingConfiguration
	 * @param store
	 * @
	 */
	void saveShippingConfiguration(ShippingConfiguration shippingConfiguration,
								   MerchantStoreItem store) ;

	void removeShippingQuoteModuleConfiguration(String moduleCode,
												MerchantStoreItem store) ;


	/**
	 * Returns a list of supported countries (ship to country list) configured by merchant
	 * If the merchant configured shipping National, then only store country will be in the list
	 * If the merchant configured shipping International, then the list of accepted country is returned
	 * from the list
	 *
	 * @param store
	 * @param language
	 * @return
	 * @
	 */
	List<CountryItem> getShipToCountryList(MerchantStoreItem store, LocaleItem language)
			;

	/**
	 * Returns shipping metadata and how shipping is configured for a given store
	 *
	 * @param store
	 * @return
	 * @
	 */
	ShippingMetaData getShippingMetaData(MerchantStoreItem store) ;

	/**
	 * Based on merchant configurations will return if tax must be calculated on shipping
	 *
	 * @param store
	 * @return
	 * @
	 */
	boolean hasTaxOnShipping(MerchantStoreItem store) ;


}