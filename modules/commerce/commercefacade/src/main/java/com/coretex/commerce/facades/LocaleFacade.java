package com.coretex.commerce.facades;

import com.coretex.commerce.data.LocaleData;
import com.coretex.commerce.data.StoreData;
import com.coretex.commerce.data.minimal.MinimalLocaleData;
import com.coretex.items.core.LocaleItem;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface LocaleFacade extends PageableDataTableFacade<LocaleItem, MinimalLocaleData> {
	LocaleData getByIso(String iso);

	Flux<LocaleData> getByStore(UUID uuid);

	Flux<LocaleData> getByStore(StoreData storeData);

	Flux<LocaleData> getAll();
}
