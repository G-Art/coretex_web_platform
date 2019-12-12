package com.coretex.commerce.core.dao.impl;

import com.coretex.commerce.core.dao.StoreDao;
import com.coretex.core.activeorm.dao.DefaultGenericDao;
import com.coretex.items.cx_core.StoreItem;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DefaultStoreDao extends DefaultGenericDao<StoreItem> implements StoreDao {

	public DefaultStoreDao() {
		super(StoreItem.ITEM_TYPE);
	}

	@Override
	public StoreItem findByCode(String code){
		return findSingle(Map.of(StoreItem.CODE, code), false);
	}
}
