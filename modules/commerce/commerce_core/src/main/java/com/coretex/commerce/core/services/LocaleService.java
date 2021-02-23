package com.coretex.commerce.core.services;

import com.coretex.items.core.LocaleItem;
import com.coretex.items.cx_core.StoreItem;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface LocaleService extends GenericItemService<LocaleItem> {

	LocaleItem getByIso(String iso);

	Flux<LocaleItem> findForStore(UUID uuid);


	Flux<LocaleItem> findForStore(StoreItem store);
}
