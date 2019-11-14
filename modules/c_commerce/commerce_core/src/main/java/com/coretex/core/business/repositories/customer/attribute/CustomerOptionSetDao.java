package com.coretex.core.business.repositories.customer.attribute;

import java.util.List;
import java.util.UUID;

import com.coretex.items.commerce_core_model.CustomerOptionSetItem;
import com.coretex.core.activeorm.dao.Dao;

public interface CustomerOptionSetDao extends Dao<CustomerOptionSetItem> {


	//	@Query("select c from CustomerOptionSetItem c join fetch c.customerOption co join fetch c.customerOptionValue cov join fetch co.merchantStore com left join fetch co.descriptions cod left join fetch cov.descriptions covd where c.id = ?1")
	CustomerOptionSetItem findOne(UUID id);

	//	@Query("select c from CustomerOptionSetItem c join fetch c.customerOption co join fetch c.customerOptionValue cov join fetch co.merchantStore com left join fetch co.descriptions cod left join fetch cov.descriptions covd where com.id = ?1 and co.id = ?2")
	List<CustomerOptionSetItem> findByOptionId(UUID merchantStoreId, UUID id);

	//	@Query("select c from CustomerOptionSetItem c join fetch c.customerOption co join fetch c.customerOptionValue cov join fetch co.merchantStore com left join fetch co.descriptions cod left join fetch cov.descriptions covd where com.id = ?1 and cov.id = ?2")
	List<CustomerOptionSetItem> findByOptionValueId(UUID merchantStoreId, UUID id);

	//	@Query("select c from CustomerOptionSetItem c join fetch c.customerOption co join fetch c.customerOptionValue cov join fetch co.merchantStore com left join fetch co.descriptions cod left join fetch cov.descriptions covd where com.id = ?1 and cod.language.id = ?2 and covd.language.id = ?2 order by c.sortOrder asc")
	List<CustomerOptionSetItem> findByStore(UUID merchantStoreId, UUID languageId);

}
