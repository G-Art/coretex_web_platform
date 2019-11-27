package com.coretex.shop.store.controller.converter;

import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.core.LocaleItem;

public interface Converter<S, T> {

	T convert(S source, MerchantStoreItem store, LocaleItem language);

}
