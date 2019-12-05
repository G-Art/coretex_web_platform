package com.coretex.core.business.services.user;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.coretex.items.commerce_core_model.PermissionItem;
import org.springframework.stereotype.Service;


import com.coretex.core.business.repositories.user.PermissionDao;
import com.coretex.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.coretex.items.commerce_core_model.GroupItem;
import com.coretex.core.model.user.PermissionCriteria;
import com.coretex.core.model.user.PermissionList;


@Service("permissionService")
public class PermissionServiceImpl extends
		SalesManagerEntityServiceImpl<PermissionItem> implements
		PermissionService {

	private PermissionDao permissionDao;


	public PermissionServiceImpl(PermissionDao permissionDao) {
		super(permissionDao);
		this.permissionDao = permissionDao;

	}

	@Override
	public List<PermissionItem> getByName() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public PermissionItem getByUUID(UUID permissionId) {
		return permissionDao.findOne(permissionId);

	}


	@Override
	public void deletePermission(PermissionItem permission)  {
		permission = this.getByUUID(permission.getUuid());//Prevents detached entity error
		permission.setGroups(null);

		this.delete(permission);
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<PermissionItem> getPermissions(List<UUID> groupIds)
			 {
		@SuppressWarnings({"unchecked", "rawtypes"})
		Set ids = new HashSet(groupIds);
		return permissionDao.findByGroups(ids);
	}

	@Override
	public PermissionList listByCriteria(PermissionCriteria criteria)
			 {
		return permissionDao.listByCriteria(criteria);
	}

	@Override
	public void removePermission(PermissionItem permission, GroupItem group)  {
		permission = this.getByUUID(permission.getUuid());//Prevents detached entity error

		permission.getGroups().remove(group);


	}

	@Override
	public List<PermissionItem> listPermission()  {
		return permissionDao.findAll();
	}


}
