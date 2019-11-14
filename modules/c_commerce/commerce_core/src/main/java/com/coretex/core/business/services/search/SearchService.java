package com.coretex.core.business.services.search;

import com.coretex.core.business.exception.ServiceException;
import com.coretex.items.commerce_core_model.ProductItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.core.model.search.SearchKeywords;
import com.coretex.core.model.search.SearchResponse;

public interface SearchService {

	/**
	 * The indexing service for products. The index service must be invoked when a product is
	 * created or updated
	 *
	 * @param store
	 * @param product
	 * @throws ServiceException
	 */
	void index(MerchantStoreItem store, ProductItem product);

	/**
	 * Deletes an index in the appropriate language. Must be invoked when a product is deleted
	 *
	 * @param store
	 * @param product
	 * @throws ServiceException
	 */
	void deleteIndex(MerchantStoreItem store, ProductItem product);

	/**
	 * Similar keywords based on a a series of characters. Used in the auto-complete
	 * functionality
	 *
	 * @param collectionName
	 * @param jsonString
	 * @param entriesCount
	 * @return
	 * @throws ServiceException
	 */
	SearchKeywords searchForKeywords(String collectionName,
									 String jsonString, int entriesCount);

	/**
	 * Search products based on user entry
	 *
	 * @param store
	 * @param languageCode
	 * @param jsonString
	 * @param entriesCount
	 * @param startIndex
	 * @throws ServiceException
	 */
	SearchResponse search(MerchantStoreItem store, String languageCode, String jsonString,
						  int entriesCount, int startIndex) throws ServiceException;

	/**
	 * Initializes search service in order to avoid lazy initialization
	 */
	void initService();

}
