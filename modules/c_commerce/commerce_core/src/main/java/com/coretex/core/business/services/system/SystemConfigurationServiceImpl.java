package com.coretex.core.business.services.system;

import org.springframework.stereotype.Service;


import com.coretex.core.business.repositories.system.SystemConfigurationDao;
import com.coretex.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.coretex.items.commerce_core_model.SystemConfigurationItem;

@Service("systemConfigurationService")
public class SystemConfigurationServiceImpl extends
		SalesManagerEntityServiceImpl<SystemConfigurationItem> implements
		SystemConfigurationService {


	private SystemConfigurationDao systemConfigurationReposotory;

	public SystemConfigurationServiceImpl(
			SystemConfigurationDao systemConfigurationReposotory) {
		super(systemConfigurationReposotory);
		this.systemConfigurationReposotory = systemConfigurationReposotory;
	}

	public SystemConfigurationItem getByKey(String key)  {
		return systemConfigurationReposotory.findByKey(key);
	}


}
