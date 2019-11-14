package com.coretex.core.business.repositories.system;

import java.util.List;
import java.util.UUID;

import com.coretex.core.activeorm.dao.Dao;

import com.coretex.items.commerce_core_model.OptinItem;
import com.coretex.enums.commerce_core_model.OptinTypeEnum;

public interface OptinDao extends Dao<OptinItem> {

	//	@Query("select distinct o from OptinItem as o  left join fetch o.merchant om where om.id = ?1")
	List<OptinItem> findByMerchant(UUID storeId);


	//	@Query("select distinct o from OptinItem as o  left join fetch o.merchant om where om.id = ?1 and o.optinType = ?2")
	OptinItem findByMerchantAndType(UUID storeId, OptinTypeEnum optinTyle);

	//	@Query("select distinct o from OptinItem as o  left join fetch o.merchant om where om.id = ?1 and o.code = ?2")
	OptinItem findByMerchantAndCode(UUID storeId, String code);

}
