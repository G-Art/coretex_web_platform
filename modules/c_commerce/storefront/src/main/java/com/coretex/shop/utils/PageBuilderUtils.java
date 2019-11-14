package com.coretex.shop.utils;

import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.shop.constants.Constants;
import com.coretex.shop.store.controller.ControllerConstants;

public class PageBuilderUtils {

	public static String build404(MerchantStoreItem store) {
		return new StringBuilder().append(ControllerConstants.Tiles.Pages.notFound).append(".").append(store.getStoreTemplate()).toString();
	}

	public static String buildHomePage(MerchantStoreItem store) {
		return "redirect:" + Constants.SHOP_URI;
	}

}
