package com.coretex.core.business.utils;

import java.util.Set;

import com.coretex.items.commerce_core_model.OrderProductAttributeItem;
import com.coretex.items.commerce_core_model.OrderProductItem;
import com.coretex.items.commerce_core_model.OrderProductAttributeItem;
import com.coretex.items.commerce_core_model.OrderProductItem;

public class ProductUtils {

	public static String buildOrderProductDisplayName(OrderProductItem orderProduct) {

		String pName = orderProduct.getProductName();
		Set<OrderProductAttributeItem> oAttributes = orderProduct.getOrderAttributes();
		StringBuilder attributeName = null;
		for (OrderProductAttributeItem oProductAttribute : oAttributes) {
			if (attributeName == null) {
				attributeName = new StringBuilder();
				attributeName.append("[");
			} else {
				attributeName.append(", ");
			}
			attributeName.append(oProductAttribute.getProductAttributeName())
					.append(": ")
					.append(oProductAttribute.getProductAttributeValueName());

		}


		StringBuilder productName = new StringBuilder();
		productName.append(pName);

		if (attributeName != null) {
			attributeName.append("]");
			productName.append(" ").append(attributeName.toString());
		}

		return productName.toString();


	}

}
