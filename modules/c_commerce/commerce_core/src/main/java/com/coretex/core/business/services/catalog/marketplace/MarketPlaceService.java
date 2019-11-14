package com.coretex.core.business.services.catalog.marketplace;

import com.coretex.core.business.exception.ServiceException;
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
	 * @throws ServiceException
	 */
	MarketPlaceItem create(MerchantStoreItem store, String code) throws ServiceException;

	/**
	 * Fetch a specific marketplace
	 *
	 * @param store
	 * @param code
	 * @return MarketPlaceItem
	 * @throws ServiceException
	 */
	MarketPlaceItem getByCode(MerchantStoreItem store, String code) throws ServiceException;


}
