package com.coretex.commerce.core.dao.impl;

import com.coretex.commerce.core.dao.ManufacturerDao;
import com.coretex.core.activeorm.dao.DefaultGenericDao;
import com.coretex.items.cx_core.ManufacturerItem;
import org.springframework.stereotype.Component;

@Component
public class DefaultManufacturerDao extends DefaultGenericDao<ManufacturerItem> implements ManufacturerDao {
	public DefaultManufacturerDao() {
		super(ManufacturerItem.ITEM_TYPE);
	}
}
