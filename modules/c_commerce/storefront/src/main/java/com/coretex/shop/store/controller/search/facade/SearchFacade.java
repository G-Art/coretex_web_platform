package com.coretex.shop.store.controller.search.facade;

import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.core.LocaleItem;
import com.coretex.items.core.LocaleItem;
import com.coretex.core.model.search.SearchResponse;
import com.coretex.shop.model.ValueList;
import com.coretex.shop.model.catalog.SearchProductList;
import com.coretex.shop.model.catalog.SearchProductRequest;

/**
 * Different services for searching and indexing data
 *
 * @author c.samson
 */
public interface SearchFacade {


	/**
	 * This utility method will re-index all products in the catalogue
	 *
	 * @param store
	 * @throws Exception
	 */
	void indexAllData(MerchantStoreItem store) throws Exception;

	/**
	 * Produces a search request against elastic search
	 *
	 * @param searchRequest
	 * @return
	 * @throws Exception
	 */
	SearchProductList search(MerchantStoreItem store, LocaleItem language, SearchProductRequest searchRequest);

	/**
	 * Copy sm-core search response to a simple readable format populated with corresponding products
	 *
	 * @param searchResponse
	 * @return
	 */
	SearchProductList copySearchResponse(SearchResponse searchResponse, MerchantStoreItem store, int start, int count, LocaleItem language) throws Exception;

	/**
	 * List of keywords / autocompletes for a given word being typed
	 *
	 * @param query
	 * @param store
	 * @param language
	 * @return
	 * @throws Exception
	 */
	ValueList autocompleteRequest(String query, MerchantStoreItem store, LocaleItem language);
}
