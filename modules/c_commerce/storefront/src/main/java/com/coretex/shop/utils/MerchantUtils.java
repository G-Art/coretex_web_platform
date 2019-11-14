package com.coretex.shop.utils;

import java.util.Date;

import com.coretex.items.commerce_core_model.MerchantStoreItem;
import org.apache.commons.lang3.StringUtils;

public class MerchantUtils {

	public String getFooterMessage(MerchantStoreItem store, String prefix, String suffix) {

		StringBuilder footerMessage = new StringBuilder();

		if (!StringUtils.isBlank(prefix)) {
			footerMessage.append(prefix).append(" ");
		}

		Date sinceDate = null;
		String inBusinessSince = store.getDateBusinessSince();


		return null;
	}

}
