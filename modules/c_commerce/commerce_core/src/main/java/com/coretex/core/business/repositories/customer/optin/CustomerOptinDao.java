package com.coretex.core.business.repositories.customer.optin;

import java.util.List;
import java.util.UUID;

import com.coretex.items.commerce_core_model.CustomerOptinItem;
import com.coretex.core.activeorm.dao.Dao;

public interface CustomerOptinDao extends Dao<CustomerOptinItem> {

	//	@Query("select distinct c from CustomerOptinItem as c left join fetch c.optin o join fetch o.merchant om where om.id = ?1 and o.code = ?2 and c.email = ?3")
	CustomerOptinItem findByMerchantAndCodeAndEmail(UUID storeId, String code, String email);
}
