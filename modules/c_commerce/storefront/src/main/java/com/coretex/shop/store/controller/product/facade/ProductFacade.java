package com.coretex.shop.store.controller.product.facade;

import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.core.LocaleItem;
import com.coretex.shop.model.catalog.manufacturer.ReadableManufacturer;
import com.coretex.shop.model.catalog.product.ReadableProduct;

import java.util.UUID;

public interface ProductFacade {

	/**
	 * Get a ProductItem by id and store
	 *
	 * @param store
	 * @param id
	 * @param language
	 * @return
	 * @throws Exception
	 */
	ReadableProduct getProduct(MerchantStoreItem store, UUID id, LocaleItem language) throws Exception;


	/**
	 * Get a product by sku and store
	 *
	 * @param store
	 * @param sku
	 * @param language
	 * @return
	 * @throws Exception
	 */
	ReadableProduct getProduct(MerchantStoreItem store, String sku, LocaleItem language) throws Exception;


	/**
	 * Get a ManufacturerItem by id
	 *
	 * @param id
	 * @param store
	 * @param language
	 * @return
	 * @throws Exception
	 */
	ReadableManufacturer getManufacturer(UUID id, MerchantStoreItem store, LocaleItem language) throws Exception;

}
