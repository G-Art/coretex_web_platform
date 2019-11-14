package com.coretex.core.business.repositories.user;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.coretex.core.activeorm.dao.Dao;

import com.coretex.items.commerce_core_model.PermissionItem;

public interface PermissionDao extends Dao<PermissionItem>, PermissionRepositoryCustom {


	//	@Query("select p from PermissionItem as p where p.id = ?1")
	PermissionItem findOne(UUID id);

	//	@Query("select p from PermissionItem as p order by p.id")
	List<PermissionItem> findAll();

	//	@Query("select distinct p from PermissionItem as p join fetch p.groups groups where groups.id in (?1)")
	List<PermissionItem> findByGroups(Set<UUID> groupIds);
}
