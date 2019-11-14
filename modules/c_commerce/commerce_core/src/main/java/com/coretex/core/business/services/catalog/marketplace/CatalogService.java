package com.coretex.core.business.services.catalog.marketplace;

import java.util.List;

import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.business.services.common.generic.SalesManagerEntityService;
import com.coretex.items.commerce_core_model.CatalogItem;
import com.coretex.items.commerce_core_model.MarketPlaceItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;


public interface CatalogService extends SalesManagerEntityService<CatalogItem> {


	/**
	 * Creates a new CatalogItem
	 *
	 * @param store
	 * @param code
	 * @return CatalogItem
	 * @throws ServiceException
	 */
	CatalogItem create(MerchantStoreItem store, String code) throws ServiceException;

	/**
	 * Get a list of CatalogItem associated with a MarketPlaceItem
	 *
	 * @param marketPlace
	 * @return List<CatalogItem>
	 * @throws ServiceException
	 */
	List<CatalogItem> getCatalogs(MarketPlaceItem marketPlace) throws ServiceException;


}
