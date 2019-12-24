package com.coretex.commerce.core.services;

import com.coretex.items.cx_core.StoreItem;

public interface StoreService extends GenericItemService<StoreItem> {

	StoreItem getByCode(String code);

	StoreItem getByDomain(String domain);

	boolean existByCode(String code);


}
