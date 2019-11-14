package com.coretex.core.business.repositories.customer.attribute;

import java.util.List;
import java.util.UUID;

import com.coretex.core.activeorm.dao.Dao;

import com.coretex.items.commerce_core_model.CustomerAttributeItem;

public interface CustomerAttributeDao extends Dao<CustomerAttributeItem> {


	//	@Query("select a from CustomerAttributeItem a left join fetch a.customerOption aco left join fetch a.customerOptionValue acov left join fetch aco.descriptions acod left join fetch acov.descriptions acovd where a.id = ?1")
	CustomerAttributeItem findOne(UUID id);

	//	@Query("select a from CustomerAttributeItem a join fetch a.customer ac left join fetch a.customerOption aco join fetch aco.merchantStore acom left join fetch a.customerOptionValue acov left join fetch aco.descriptions acod left join fetch acov.descriptions acovd where acom.id = ?1 and ac.id = ?2 and aco.id = ?3")
	CustomerAttributeItem findByOptionId(UUID merchantId, UUID customerId, UUID id);

	//	@Query("select a from CustomerAttributeItem a join fetch a.customer ac left join fetch a.customerOption aco join fetch aco.merchantStore acom left join fetch a.customerOptionValue acov left join fetch aco.descriptions acod left join fetch acov.descriptions acovd where acom.id = ?1 and aco.id = ?2")
	List<CustomerAttributeItem> findByOptionId(UUID merchantId, UUID id);

	//	@Query("select distinct a from CustomerAttributeItem a join fetch a.customer ac left join fetch a.customerOption aco join fetch aco.merchantStore acom left join fetch a.customerOptionValue acov left join fetch aco.descriptions acod left join fetch acov.descriptions acovd where acom.id = ?1 and ac.id = ?2")
	List<CustomerAttributeItem> findByCustomerId(UUID merchantId, UUID customerId);

	//	@Query("select a from CustomerAttributeItem a join fetch a.customer ac left join fetch a.customerOption aco join fetch aco.merchantStore acom left join fetch a.customerOptionValue acov left join fetch aco.descriptions acod left join fetch acov.descriptions acovd where acom.id = ?1 and acov.id = ?2")
	List<CustomerAttributeItem> findByOptionValueId(UUID merchantId, UUID Id);
}
