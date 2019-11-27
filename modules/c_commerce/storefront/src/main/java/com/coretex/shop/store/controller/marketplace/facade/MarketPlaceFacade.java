package com.coretex.shop.store.controller.marketplace.facade;

import com.coretex.items.core.LocaleItem;
import com.coretex.shop.model.marketplace.ReadableMarketPlace;

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
	ReadableMarketPlace get(String store, LocaleItem lang);

}
