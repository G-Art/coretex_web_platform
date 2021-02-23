package com.coretex.commerce.facades;

import com.coretex.commerce.data.StoreData;
import com.coretex.commerce.data.minimal.MinimalStoreData;
import com.coretex.items.cx_core.StoreItem;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface StoreFacade extends PageableDataTableFacade<StoreItem, MinimalStoreData> {

	StoreData getByCode(String code);
	StoreData getByDomain(String domain);
	Flux<StoreData> getAll();

	StoreData getByUuid(UUID uuid);

	void delete(UUID uuid);
}
