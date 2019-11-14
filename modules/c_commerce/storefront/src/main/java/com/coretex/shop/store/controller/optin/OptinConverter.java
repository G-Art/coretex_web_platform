package com.coretex.shop.store.controller.optin;

import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.items.commerce_core_model.LanguageItem;

public interface OptinConverter<S, T> {

	T convertTo(S source, MerchantStoreItem merchantStore, LanguageItem language);

	S convertFrom(T source, MerchantStoreItem merchantStore, LanguageItem language);
}
