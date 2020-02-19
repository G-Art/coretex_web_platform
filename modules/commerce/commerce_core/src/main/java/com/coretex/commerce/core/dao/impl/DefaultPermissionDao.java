package com.coretex.commerce.core.dao.impl;

import com.coretex.commerce.core.dao.PermissionDao;
import com.coretex.core.activeorm.dao.DefaultGenericDao;
import com.coretex.items.cx_core.PermissionItem;
import org.springframework.stereotype.Component;

@Component
public class DefaultPermissionDao extends DefaultGenericDao<PermissionItem> implements PermissionDao {
	public DefaultPermissionDao() {
		super(PermissionItem.ITEM_TYPE);
	}
}
