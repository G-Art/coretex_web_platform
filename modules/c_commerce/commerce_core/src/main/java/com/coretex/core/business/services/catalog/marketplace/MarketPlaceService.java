package com.coretex.core.business.services.catalog.marketplace;


import com.coretex.core.business.services.common.generic.SalesManagerEntityService;
import com.coretex.items.commerce_core_model.MarketPlaceItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;

public interface MarketPlaceService extends SalesManagerEntityService<MarketPlaceItem> {

	/**
	 * Creates a MarketPlaceItem
	 *
	 * @param store
	 * @param code
	 * @return MarketPlaceItem
	 * @
	 */
	MarketPlaceItem create(MerchantStoreItem store, String code) ;

	/**
	 * Fetch a specific marketplace
	 *
	 * @param store
	 * @param code
	 * @return MarketPlaceItem
	 * @
	 */
	MarketPlaceItem getByCode(MerchantStoreItem store, String code) ;


}
