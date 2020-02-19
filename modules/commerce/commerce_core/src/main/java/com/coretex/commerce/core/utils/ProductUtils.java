package com.coretex.commerce.core.utils;

import com.coretex.items.cx_core.StoreItem;

public class ProductUtils {

	public final static String SLASH = "/";
	public final static String SMALL_IMAGE = "SMALL";
	public final static String LARGE_IMAGE = "LARGE";
	public final static String PRODUCTS_URI = "/products";

	public static String buildProductSmallImageUtils(StoreItem store, String sku, String imageName) {
		return buildProductImageUtils(store, sku, imageName, SMALL_IMAGE);
	}

	public static String buildProductLargeImageUtils(StoreItem store, String sku, String imageName) {
		return buildProductImageUtils(store, sku, imageName, LARGE_IMAGE);
	}

	public static String buildProductImageUtils(StoreItem store, String sku, String imageName, String size) {
		return "/static" + PRODUCTS_URI + SLASH + store.getCode() + SLASH +
				sku + SLASH + size + SLASH + imageName;
	}
}
