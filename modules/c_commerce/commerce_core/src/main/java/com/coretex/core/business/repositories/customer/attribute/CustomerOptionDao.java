package com.coretex.core.business.repositories.customer.attribute;

import java.util.List;
import java.util.UUID;

import com.coretex.items.commerce_core_model.CustomerOptionItem;
import com.coretex.core.activeorm.dao.Dao;

public interface CustomerOptionDao extends Dao<CustomerOptionItem> {


	//	@Query("select o from CustomerOptionItem o join fetch o.merchantStore om left join fetch o.descriptions od where o.id = ?1")
	CustomerOptionItem findOne(UUID id);

	//	@Query("select o from CustomerOptionItem o join fetch o.merchantStore om left join fetch o.descriptions od where om.id = ?1 and o.code = ?2")
	CustomerOptionItem findByCode(UUID merchantId, String code);

	//	@Query("select o from CustomerOptionItem o join fetch o.merchantStore om left join fetch o.descriptions od where om.id = ?1 and od.language.id = ?2")
	List<CustomerOptionItem> findByStore(UUID merchantId, UUID languageId);

}
