package com.coretex.shop.utils;

import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.cx_core.ManufacturerItem;
import com.coretex.items.cx_core.ProductItem;
import com.coretex.items.cx_core.StoreItem;

public interface ImageFilePath {

	/**
	 * Context path configured in shopizer-properties.xml
	 *
	 * @return
	 */
	String getContextPath();


	String getBasePath();

	/**
	 * Builds a static content image file path that can be used by image servlet
	 * utility for getting the physical image
	 *
	 * @param store
	 * @param imageName
	 * @return
	 */
	String buildStaticImageUtils(MerchantStoreItem store, String imageName);

	/**
	 * Builds a static content image file path that can be used by image servlet
	 * utility for getting the physical image by specifying the image type
	 *
	 * @param store
	 * @param imageName
	 * @return
	 */
	String buildStaticImageUtils(MerchantStoreItem store, String type, String imageName);

	/**
	 * Builds a manufacturer image file path that can be used by image servlet
	 * utility for getting the physical image
	 *
	 * @param store
	 * @param manufacturer
	 * @param imageName
	 * @return
	 */
	String buildManufacturerImageUtils(MerchantStoreItem store, ManufacturerItem manufacturer, String imageName);

	/**
	 * Builds a product image file path that can be used by image servlet
	 * utility for getting the physical image
	 *
	 * @param store
	 * @param product
	 * @param imageName
	 * @return
	 */
	String buildProductImageUtils(MerchantStoreItem store, ProductItem product, String imageName);

	String buildProductImageUtils(StoreItem store, ProductItem product, String imageName);

	String buildProductImageUtils(ProductItem product, String imageName);

	/**
	 * Builds a default product image file path that can be used by image servlet
	 * utility for getting the physical image
	 *
	 * @param store
	 * @param sku
	 * @param imageName
	 * @return
	 */
	String buildProductImageUtils(MerchantStoreItem store, String sku, String imageName);

	/**
	 * Builds a large product image file path that can be used by the image servlet
	 *
	 * @param store
	 * @param sku
	 * @param imageName
	 * @return
	 */
	String buildLargeProductImageUtils(MerchantStoreItem store, String sku, String imageName);


	/**
	 * Builds a merchant store logo path
	 *
	 * @param store
	 * @return
	 */
	String buildStoreLogoFilePath(MerchantStoreItem store);

	/**
	 * Builds product property image url path
	 *
	 * @param store
	 * @param imageName
	 * @return
	 */
	String buildProductPropertyImageUtils(MerchantStoreItem store, String imageName);


	/**
	 * Builds static file path
	 *
	 * @param store
	 * @param fileName
	 * @return
	 */
	String buildStaticContentFilePath(MerchantStoreItem store, String fileName);


}
