package com.coretex.commerce.core.services.impl;

import com.coretex.commerce.core.dao.StoreDao;
import com.coretex.commerce.core.services.AbstractGenericItemService;
import com.coretex.commerce.core.services.StoreService;
import com.coretex.items.cx_core.StoreItem;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DefaultStoreService extends AbstractGenericItemService<StoreItem> implements StoreService {

	public DefaultStoreService(StoreDao storeDao) {
		super(storeDao);
	}

	@Override
	public StoreItem getByCode(String code) {
		return getRepository().findSingle(Map.of(StoreItem.CODE, code), true);
	}

	@Override
	public StoreItem getByDomain(String domain) {
		return getRepository().findSingle(Map.of(StoreItem.DOMAIN_NAME, domain), false);
	}

	@Override
	public boolean existByCode(String code) {
		return getByCode(code) != null;
	}
}
