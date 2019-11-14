package com.coretex.core.business.repositories.system;

import com.coretex.core.activeorm.dao.DefaultGenericDao;
import com.coretex.items.commerce_core_model.IntegrationModuleItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ModuleConfigurationDaoImpl extends DefaultGenericDao<IntegrationModuleItem> implements ModuleConfigurationDao{

	public ModuleConfigurationDaoImpl() {
		super(IntegrationModuleItem.ITEM_TYPE);
	}

	@Override
	public List<IntegrationModuleItem> findByModule(String moduleName) {
		return find(Map.of(IntegrationModuleItem.MODULE, moduleName));
	}

	@Override
	public IntegrationModuleItem findByCode(String code) {
		return findSingle(Map.of(IntegrationModuleItem.CODE, code), true);
	}
}
