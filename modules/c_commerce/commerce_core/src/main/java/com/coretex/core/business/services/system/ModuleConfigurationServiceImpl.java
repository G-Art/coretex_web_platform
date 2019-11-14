package com.coretex.core.business.services.system;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import com.coretex.items.commerce_core_model.IntegrationModuleItem;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.business.repositories.system.ModuleConfigurationDao;
import com.coretex.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.coretex.core.business.services.reference.loader.IntegrationModulesLoader;
import com.coretex.core.business.utils.CacheUtils;
import com.coretex.core.model.system.ModuleConfig;
import org.springframework.util.StringUtils;

@Service("moduleConfigurationService")
public class ModuleConfigurationServiceImpl extends
		SalesManagerEntityServiceImpl<IntegrationModuleItem> implements
		ModuleConfigurationService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ModuleConfigurationServiceImpl.class);

	@Resource
	private IntegrationModulesLoader integrationModulesLoader;

	private ModuleConfigurationDao moduleConfigurationDao;

	@Resource
	private CacheUtils cache;

	public ModuleConfigurationServiceImpl(
			ModuleConfigurationDao moduleConfigurationDao) {
		super(moduleConfigurationDao);
		this.moduleConfigurationDao = moduleConfigurationDao;
	}

	@Override
	public IntegrationModuleItem getByCode(String moduleCode) {
		return moduleConfigurationDao.findByCode(moduleCode);
	}


	@SuppressWarnings({"unchecked", "rawtypes"})
	@Override
	public List<IntegrationModuleItem> getIntegrationModules(String module) {


		List<IntegrationModuleItem> modules = null;
		try {

			//CacheUtils cacheUtils = CacheUtils.getInstance();
			modules = (List<IntegrationModuleItem>) cache.getFromCache("INTEGRATION_M)" + module);
			if (modules == null) {
				modules = moduleConfigurationDao.findByModule(module);

				cache.putInCache(modules, "INTEGRATION_M)" + module);
			}

		} catch (Exception e) {
			LOGGER.error("getIntegrationModules()", e);
		}
		return modules;


	}

	@Override
	public Map<String, ModuleConfig> moduleConfigs(IntegrationModuleItem integrationModule) {
		String configs = integrationModule.getConfiguration();
		if (configs != null) {

			//Map objects = mapper.readValue(config, Map.class);

			Object objConfigs = JSONValue.parse(configs);
			JSONArray arrayConfigs = (JSONArray) objConfigs;

			Map<String, ModuleConfig> moduleConfigs = new HashMap<String, ModuleConfig>();

			Iterator i = arrayConfigs.iterator();
			while (i.hasNext()) {

				Map values = (Map) i.next();
				String env = (String) values.get("env");
				ModuleConfig config = new ModuleConfig();
				config.setScheme((String) values.get("scheme"));
				config.setHost((String) values.get("host"));
				config.setPort((String) values.get("port"));
				config.setUri((String) values.get("uri"));
				config.setEnv((String) values.get("env"));
				if (values.get("config1") != null) {
					config.setConfig1((String) values.get("config1"));
				}
				if (values.get("config2") != null) {
					config.setConfig1((String) values.get("config2"));
				}

				moduleConfigs.put(env, config);


			}

			return moduleConfigs;


		}
		return Maps.newHashMap();
	}

	@Override
	public Map<String, String> getDetails(IntegrationModuleItem integrationModule) {
		String details = integrationModule.getConfigDetails();
		if (!StringUtils.isEmpty(details)) {
			return (Map<String, String>) JSONValue.parse(details);
		}
		return Maps.newHashMap();
	}

	@Override
	public Set<String> getRegionsSet(IntegrationModuleItem integrationModule) {
		String regions = integrationModule.getRegions();
		if (regions != null) {
			Object objRegions = JSONValue.parse(regions);
			JSONArray arrayRegions = (JSONArray) objRegions;
			if (!arrayRegions.isEmpty()) {
				return Sets.newHashSet(arrayRegions.iterator());
			}
		}
		return Set.of();
	}

	@Override
	public void createOrUpdateModule(String json) throws ServiceException {

		ObjectMapper mapper = new ObjectMapper();

		try {


			@SuppressWarnings("rawtypes")
			Map object = mapper.readValue(json, Map.class);

			IntegrationModuleItem module = integrationModulesLoader.loadModule(object);

			if (module != null) {
				IntegrationModuleItem m = this.getByCode(module.getCode());
				if (m != null) {
					this.delete(m);
				}
				this.create(module);
			}


		} catch (Exception e) {
			throw new ServiceException(e);
		}


	}


}
