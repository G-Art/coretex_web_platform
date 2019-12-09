package com.coretex.core.business.utils;

import com.coretex.items.commerce_core_model.OrderProductItem;

public class ProductUtils {

	public static String buildOrderProductDisplayName(OrderProductItem orderProduct) {

		String pName = orderProduct.getProductName();

		return "[" + pName + "]";

	}

}
