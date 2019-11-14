package com.coretex.shop.store.controller.zone.facade;

import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.shop.model.references.ReadableZone;

import java.util.List;

public interface ZoneFacade {

	List<ReadableZone> getZones(String countryCode, LanguageItem language, MerchantStoreItem merchantStore);

}
