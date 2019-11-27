package com.coretex.shop.store.controller.zone.facade;

import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.core.LocaleItem;
import com.coretex.shop.model.references.ReadableZone;

import java.util.List;

public interface ZoneFacade {

	List<ReadableZone> getZones(String countryCode, LocaleItem language, MerchantStoreItem merchantStore);

}
