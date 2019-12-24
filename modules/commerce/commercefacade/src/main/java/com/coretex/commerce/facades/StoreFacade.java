package com.coretex.commerce.facades;

import com.coretex.commerce.data.StoreData;
import com.coretex.commerce.data.minimal.MinimalStoreData;
import com.coretex.items.cx_core.StoreItem;

import java.util.stream.Stream;

public interface StoreFacade extends PageableDataTableFacade<StoreItem, MinimalStoreData> {

	StoreData getByCode(String code);
	StoreData getByDomain(String domain);
	Stream<StoreData> getAll();
}
