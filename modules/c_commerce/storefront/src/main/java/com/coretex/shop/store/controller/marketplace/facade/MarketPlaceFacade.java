package com.coretex.shop.store.controller.marketplace.facade;

import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.enums.commerce_core_model.OptinTypeEnum;
import com.coretex.shop.model.marketplace.ReadableMarketPlace;
import com.coretex.shop.model.system.ReadableOptin;

/**
 * Builds market places objects for shop and REST api
 *
 * @author c.samson
 */
public interface MarketPlaceFacade {


	/**
	 * Get a MarketPlaceItem from store code
	 *
	 * @param store
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	ReadableMarketPlace get(String store, LanguageItem lang);

	/**
	 * Finds an optin by merchant and type
	 *
	 * @param store
	 * @param type
	 * @return
	 * @throws Exception
	 */
	ReadableOptin findByMerchantAndType(MerchantStoreItem store, OptinTypeEnum type);

}
