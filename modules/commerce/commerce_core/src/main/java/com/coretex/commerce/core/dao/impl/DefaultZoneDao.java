package com.coretex.commerce.core.dao.impl;

import com.coretex.commerce.core.dao.ZoneDao;
import com.coretex.core.activeorm.dao.DefaultGenericDao;
import com.coretex.items.cx_core.ZoneItem;
import org.springframework.stereotype.Component;

@Component
public class DefaultZoneDao extends DefaultGenericDao<ZoneItem> implements ZoneDao {
	public DefaultZoneDao() {
		super(ZoneItem.ITEM_TYPE);
	}
}
