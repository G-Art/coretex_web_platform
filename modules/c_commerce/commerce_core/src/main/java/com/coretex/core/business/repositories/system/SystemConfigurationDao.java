package com.coretex.core.business.repositories.system;

import com.coretex.core.activeorm.dao.Dao;

import com.coretex.items.commerce_core_model.SystemConfigurationItem;

public interface SystemConfigurationDao extends Dao<SystemConfigurationItem> {


	SystemConfigurationItem findByKey(String key);

}
