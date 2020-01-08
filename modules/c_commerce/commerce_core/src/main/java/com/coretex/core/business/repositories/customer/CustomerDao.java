package com.coretex.core.business.repositories.customer;

import com.coretex.core.activeorm.dao.Dao;
import com.coretex.items.cx_core.CustomerItem;

import java.util.List;
import java.util.UUID;

public interface CustomerDao extends Dao<CustomerItem>, CustomerRepositoryCustom {


	//	@Query("select c from CustomerItem c join fetch c.merchantStore cm left join fetch c.defaultLanguage cl left join fetch c.attributes ca left join fetch ca.customerOption cao left join fetch ca.customerOptionValue cav left join fetch cao.descriptions caod left join fetch cav.descriptions left join fetch c.groups where c.id = ?1")
	CustomerItem findOne(UUID id);

	CustomerItem findByEmail(String email);

	//	@Query("select distinct c from CustomerItem c join fetch c.merchantStore cm left join fetch c.defaultLanguage cl left join fetch c.attributes ca left join fetch ca.customerOption cao left join fetch ca.customerOptionValue cav left join fetch cao.descriptions caod left join fetch cav.descriptions left join fetch c.groups  where c.billing.firstName = ?1")
	List<CustomerItem> findByName(String name);

	//	@Query("select distinct c from CustomerItem c join fetch c.merchantStore cm left join fetch c.defaultLanguage cl left join fetch c.attributes ca left join fetch ca.customerOption cao left join fetch ca.customerOptionValue cav left join fetch cao.descriptions caod left join fetch cav.descriptions left join fetch c.groups  where cm.id = ?1")
	List<CustomerItem> findByStore(UUID storeId);
}
