package com.coretex.shop.store.controller.optin;

import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.shop.model.system.ReadableOptin;

public interface OptinFacade {

	ReadableOptin create(ReadableOptin readableOptin, MerchantStoreItem merchantStore, LanguageItem language);
}
