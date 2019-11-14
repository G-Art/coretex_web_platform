package com.coretex.shop.store.controller.items.facade;

import java.util.List;
import java.util.UUID;

import com.coretex.items.commerce_core_model.ProductItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.shop.model.catalog.product.ReadableProductList;

public interface ProductItemsFacade {

	/**
	 * List items attached to a ManufacturerItem
	 *
	 * @param store
	 * @param language
	 * @param manufacturerId
	 * @return
	 */
	ReadableProductList listItemsByManufacturer(MerchantStoreItem store, LanguageItem language, UUID manufacturerId, int startCount, int maxCount) throws Exception;

	/**
	 * List product items by id
	 *
	 * @param store
	 * @param language
	 * @param ids
	 * @param startCount
	 * @param maxCount
	 * @return
	 * @throws Exception
	 */
	ReadableProductList listItemsByIds(MerchantStoreItem store, LanguageItem language, List<UUID> ids, int startCount, int maxCount) throws Exception;


	/**
	 * List products created in a group, for instance FEATURED group
	 *
	 * @param group
	 * @param store
	 * @param language
	 * @return
	 * @throws Exception
	 */
	ReadableProductList listItemsByGroup(String group, MerchantStoreItem store, LanguageItem language) throws Exception;

	/**
	 * Add product to a group
	 *
	 * @param product
	 * @param group
	 * @param store
	 * @param language
	 * @return
	 * @throws Exception
	 */
	ReadableProductList addItemToGroup(ProductItem product, String group, MerchantStoreItem store, LanguageItem language) throws Exception;

	/**
	 * Removes a product from a group
	 *
	 * @param product
	 * @param group
	 * @param store
	 * @param language
	 * @return
	 * @throws Exception
	 */
	ReadableProductList removeItemFromGroup(ProductItem product, String group, MerchantStoreItem store, LanguageItem language) throws Exception;


}
