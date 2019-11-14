package com.coretex.core.business.services.system;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.business.services.common.generic.SalesManagerEntityService;
import com.coretex.core.model.system.ModuleConfig;
import com.coretex.items.commerce_core_model.IntegrationModuleItem;

public interface ModuleConfigurationService extends
		SalesManagerEntityService<IntegrationModuleItem> {

	List<IntegrationModuleItem> getIntegrationModules(String module);

	IntegrationModuleItem getByCode(String moduleCode);

	void createOrUpdateModule(String json) throws ServiceException;

	Map<String, ModuleConfig> moduleConfigs(IntegrationModuleItem integrationModule);

	Map<String, String> getDetails(IntegrationModuleItem integrationModule);

	Set<String> getRegionsSet(IntegrationModuleItem integrationModule);

}
