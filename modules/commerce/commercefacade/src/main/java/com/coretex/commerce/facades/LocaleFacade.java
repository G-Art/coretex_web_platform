package com.coretex.commerce.facades;

import com.coretex.commerce.data.LocaleData;
import com.coretex.commerce.data.StoreData;
import com.coretex.commerce.data.minimal.MinimalLocaleData;
import com.coretex.items.core.LocaleItem;

import java.util.UUID;
import java.util.stream.Stream;

public interface LocaleFacade extends PageableDataTableFacade<LocaleItem, MinimalLocaleData> {
	LocaleData getByIso(String iso);

	Stream<LocaleData> getByStore(UUID uuid);

	Stream<LocaleData> getByStore(StoreData storeData);

	Stream<LocaleData> getAll();
}
