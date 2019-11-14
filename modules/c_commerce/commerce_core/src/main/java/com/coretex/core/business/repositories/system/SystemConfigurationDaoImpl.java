package com.coretex.core.business.repositories.system;

import com.coretex.core.activeorm.dao.DefaultGenericDao;
import com.coretex.items.commerce_core_model.SystemConfigurationItem;
import org.springframework.stereotype.Component;

@Component
public class SystemConfigurationDaoImpl extends DefaultGenericDao<SystemConfigurationItem> implements SystemConfigurationDao{

	public SystemConfigurationDaoImpl() {
		super(SystemConfigurationItem.ITEM_TYPE);
	}

	@Override
	public SystemConfigurationItem findByKey(String key) {
		return null;
	}
}
