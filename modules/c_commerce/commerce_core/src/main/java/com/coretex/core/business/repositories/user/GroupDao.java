package com.coretex.core.business.repositories.user;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.coretex.items.commerce_core_model.GroupItem;
import com.coretex.core.activeorm.dao.Dao;

import com.coretex.enums.commerce_core_model.GroupTypeEnum;

public interface GroupDao extends Dao<GroupItem> {


	GroupItem findById(UUID id);

	//	@Query("select distinct g from GroupItem as g left join fetch g.permissions perms order by g.id")
	List<GroupItem> findAll();

	//	@Query("select distinct g from GroupItem as g left join fetch g.permissions perms where perms.id in (?1) ")
	List<GroupItem> findByPermissions(Set<Integer> permissionIds);

	//	@Query("select distinct g from GroupItem as g left join fetch g.permissions perms where g.id in (?1) ")
	List<GroupItem> findByIds(Set<UUID> groupIds);

	//	@Query("select distinct g from GroupItem as g left join fetch g.permissions perms where g.groupName in (?1) ")
	List<GroupItem> findByNames(List<String> groupeNames);

	//	@Query("select distinct g from GroupItem as g left join fetch g.permissions perms where g.groupType = ?1")
	List<GroupItem> findByType(GroupTypeEnum type);

	GroupItem findByGroupName(String name);
}
