package com.coretex.core.business.repositories.customer.attribute;

import java.util.List;
import java.util.UUID;

import com.coretex.items.commerce_core_model.CustomerOptionValueItem;
import com.coretex.core.activeorm.dao.Dao;

public interface CustomerOptionValueDao extends Dao<CustomerOptionValueItem> {


	//	@Query("select o from CustomerOptionValueItem o join fetch o.merchantStore om left join fetch o.descriptions od where o.id = ?1")
	CustomerOptionValueItem findOne(UUID id);

	//	@Query("select o from CustomerOptionValueItem o join fetch o.merchantStore om left join fetch o.descriptions od where om.id = ?1 and o.code = ?2")
	CustomerOptionValueItem findByCode(UUID merchantId, String code);

	//	@Query("select o from CustomerOptionValueItem o join fetch o.merchantStore om left join fetch o.descriptions od where om.id = ?1 and od.language.id = ?2")
	List<CustomerOptionValueItem> findByStore(UUID merchantId, UUID languageId);

}
