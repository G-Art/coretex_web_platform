package com.coretex.commerce.core.services;

import com.coretex.items.core.LocaleItem;
import com.coretex.items.cx_core.StoreItem;

import java.util.UUID;
import java.util.stream.Stream;

public interface LocaleService extends GenericItemService<LocaleItem> {

	LocaleItem getByIso(String iso);

	Stream<LocaleItem> findForStore(UUID uuid);


	Stream<LocaleItem> findForStore(StoreItem store);
}
