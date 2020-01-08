package com.coretex.core.business.utils;

import com.coretex.core.business.constants.Constants;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.OrderProductItem;

public class ProductUtils {

	public final static String SMALL_IMAGE = "SMALL";
	public final static String PRODUCTS_URI = "/products";

	public static String buildOrderProductDisplayName(OrderProductItem orderProduct) {

		String pName = orderProduct.getProductName();

		return "[" + pName + "]";

	}

	public static String buildProductSmallImageUtils(MerchantStoreItem store, String sku, String imageName) {
		return "/static" + PRODUCTS_URI + Constants.SLASH + store.getCode() + Constants.SLASH +
				sku + Constants.SLASH + SMALL_IMAGE + Constants.SLASH + imageName;
	}
}