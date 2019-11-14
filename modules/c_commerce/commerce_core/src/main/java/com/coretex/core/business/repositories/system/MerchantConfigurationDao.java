package com.coretex.core.business.repositories.system;

import java.util.List;
import java.util.UUID;

import com.coretex.enums.commerce_core_model.MerchantConfigurationTypeEnum;
import com.coretex.items.commerce_core_model.MerchantConfigurationItem;
import com.coretex.core.activeorm.dao.Dao;

public interface MerchantConfigurationDao extends Dao<MerchantConfigurationItem> {

	//List<MerchantConfigurationItem> findByModule(String moduleName);

	//MerchantConfigurationItem findByCode(String code);

	//	@Query("select m from MerchantConfigurationItem m join fetch m.merchantStore ms where ms.id=?1")
	List<MerchantConfigurationItem> findByMerchantStore(UUID id);

	//	@Query("select m from MerchantConfigurationItem m join fetch m.merchantStore ms where ms.id=?1 and m.key=?2")
	MerchantConfigurationItem findByMerchantStoreAndKey(UUID id, String key);

	//	@Query("select m from MerchantConfigurationItem m join fetch m.merchantStore ms where ms.id=?1 and m.merchantConfigurationType=?2")
	List<MerchantConfigurationItem> findByMerchantStoreAndType(UUID id, MerchantConfigurationTypeEnum type);
}
