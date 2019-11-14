package com.coretex.core.business.services.system;

import java.util.List;

import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.business.services.common.generic.SalesManagerEntityService;
import com.coretex.enums.commerce_core_model.MerchantConfigurationTypeEnum;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.core.model.system.MerchantConfig;
import com.coretex.items.commerce_core_model.MerchantConfigurationItem;

public interface MerchantConfigurationService extends
		SalesManagerEntityService<MerchantConfigurationItem> {

	MerchantConfigurationItem getMerchantConfiguration(String key, MerchantStoreItem store) throws ServiceException;

	void saveOrUpdate(MerchantConfigurationItem entity) throws ServiceException;

	List<MerchantConfigurationItem> listByStore(MerchantStoreItem store)
			throws ServiceException;

	List<MerchantConfigurationItem> listByType(MerchantConfigurationTypeEnum type,
											   MerchantStoreItem store) throws ServiceException;

	MerchantConfig getMerchantConfig(MerchantStoreItem store)
			throws ServiceException;

	void saveMerchantConfig(MerchantConfig config, MerchantStoreItem store)
			throws ServiceException;

}
