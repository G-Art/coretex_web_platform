package com.coretex.shop.store.controller.country.facade;

import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.core.LocaleItem;
import com.coretex.items.core.LocaleItem;
import com.coretex.shop.model.references.ReadableCountry;

import java.util.List;

public interface CountryFacade {
	List<ReadableCountry> getListCountryZones(LocaleItem language, MerchantStoreItem merchantStore);
}
