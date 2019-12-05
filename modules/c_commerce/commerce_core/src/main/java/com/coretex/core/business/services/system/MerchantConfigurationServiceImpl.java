package com.coretex.core.business.services.system;

import java.util.List;

import com.coretex.enums.commerce_core_model.MerchantConfigurationTypeEnum;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.MerchantConfigurationItem;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.coretex.core.business.repositories.system.MerchantConfigurationDao;
import com.coretex.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.coretex.core.model.system.MerchantConfig;

@Service("merchantConfigurationService")
public class MerchantConfigurationServiceImpl extends
		SalesManagerEntityServiceImpl<MerchantConfigurationItem> implements
		MerchantConfigurationService {

	private MerchantConfigurationDao merchantConfigurationDao;

	public MerchantConfigurationServiceImpl(
			MerchantConfigurationDao merchantConfigurationDao) {
		super(merchantConfigurationDao);
		this.merchantConfigurationDao = merchantConfigurationDao;
	}


	@Override
	public MerchantConfigurationItem getMerchantConfiguration(String key, MerchantStoreItem store) {
		return merchantConfigurationDao.findByMerchantStoreAndKey(store.getUuid(), key);
	}

	@Override
	public List<MerchantConfigurationItem> listByStore(MerchantStoreItem store)  {
		return merchantConfigurationDao.findByMerchantStore(store.getUuid());
	}

	@Override
	public List<MerchantConfigurationItem> listByType(MerchantConfigurationTypeEnum type, MerchantStoreItem store) {
		return merchantConfigurationDao.findByMerchantStoreAndType(store.getUuid(), type);
	}

	@Override
	public void saveOrUpdate(MerchantConfigurationItem entity)  {
		super.save(entity);
	}


	@Override
	public void delete(MerchantConfigurationItem merchantConfiguration) {
		MerchantConfigurationItem config = merchantConfigurationDao.find(merchantConfiguration.getUuid());
		if (config != null) {
			super.delete(config);
		}
	}

	@Override
	public MerchantConfig getMerchantConfig(MerchantStoreItem store)  {

		MerchantConfigurationItem configuration = merchantConfigurationDao.findByMerchantStoreAndKey(store.getUuid(), MerchantConfigurationTypeEnum.CONFIG.name());

		MerchantConfig config = null;
		if (configuration != null) {
			String value = configuration.getValue();

			ObjectMapper mapper = new ObjectMapper();
			try {
				config = mapper.readValue(value, MerchantConfig.class);
			} catch (Exception e) {
				throw new RuntimeException("Cannot parse json string " + value);
			}
		}
		return config;

	}

	@Override
	public void saveMerchantConfig(MerchantConfig config, MerchantStoreItem store) {

		MerchantConfigurationItem configuration = merchantConfigurationDao.findByMerchantStoreAndKey(store.getUuid(), MerchantConfigurationTypeEnum.CONFIG.name());

		if (configuration == null) {
			configuration = new MerchantConfigurationItem();
			configuration.setMerchantStore(store);
			configuration.setKey(MerchantConfigurationTypeEnum.CONFIG.name());
			configuration.setMerchantConfigurationType(MerchantConfigurationTypeEnum.CONFIG);
			configuration.setActive(true);
		}

		String value = config.toJSONString();
		configuration.setValue(value);
		super.save(configuration);

	}


}
