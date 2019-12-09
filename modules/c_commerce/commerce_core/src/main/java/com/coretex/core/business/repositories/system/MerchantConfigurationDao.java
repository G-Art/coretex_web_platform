package com.coretex.core.business.repositories.system;

import com.coretex.core.activeorm.dao.Dao;
import com.coretex.items.commerce_core_model.MerchantConfigurationItem;

import java.util.List;
import java.util.UUID;

public interface MerchantConfigurationDao extends Dao<MerchantConfigurationItem> {



	//	@Query("select m from MerchantConfigurationItem m join fetch m.merchantStore ms where ms.id=?1")
	List<MerchantConfigurationItem> findByMerchantStore(UUID id);

	//	@Query("select m from MerchantConfigurationItem m join fetch m.merchantStore ms where ms.id=?1 and m.key=?2")
	MerchantConfigurationItem findByMerchantStoreAndKey(UUID id, String key);

}
