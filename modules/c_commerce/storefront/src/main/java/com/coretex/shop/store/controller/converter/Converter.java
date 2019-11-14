package com.coretex.shop.store.controller.converter;

import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.LanguageItem;

public interface Converter<S, T> {

	T convert(S source, MerchantStoreItem store, LanguageItem language);

}
