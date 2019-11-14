package com.coretex.core.business.repositories.system;

import java.util.List;

import com.coretex.items.commerce_core_model.IntegrationModuleItem;
import com.coretex.core.activeorm.dao.Dao;

public interface ModuleConfigurationDao extends Dao<IntegrationModuleItem> {

	List<IntegrationModuleItem> findByModule(String moduleName);

	IntegrationModuleItem findByCode(String code);


}
