package com.coretex.commerce.core.dao;

import com.coretex.core.activeorm.dao.Dao;
import com.coretex.items.cx_core.StoreItem;

public interface StoreDao extends Dao<StoreItem> {
	StoreItem findByCode(String code);
}
