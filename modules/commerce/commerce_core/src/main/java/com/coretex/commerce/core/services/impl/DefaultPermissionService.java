package com.coretex.commerce.core.services.impl;

import com.coretex.commerce.core.dao.PermissionDao;
import com.coretex.commerce.core.services.AbstractGenericItemService;
import com.coretex.commerce.core.services.PermissionService;
import com.coretex.items.cx_core.PermissionItem;
import org.springframework.stereotype.Service;

@Service
public class DefaultPermissionService extends AbstractGenericItemService<PermissionItem> implements PermissionService {
	public DefaultPermissionService(PermissionDao repository) {
		super(repository);
	}
}
