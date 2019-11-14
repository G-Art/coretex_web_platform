package com.coretex.core.business.services.user;

import java.util.List;
import java.util.UUID;

import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.business.services.common.generic.SalesManagerEntityService;
import com.coretex.items.commerce_core_model.GroupItem;
import com.coretex.items.commerce_core_model.PermissionItem;
import com.coretex.core.model.user.PermissionCriteria;
import com.coretex.core.model.user.PermissionList;


public interface PermissionService extends SalesManagerEntityService<PermissionItem> {

	List<PermissionItem> getByName();

	List<PermissionItem> listPermission() throws ServiceException;

	PermissionItem getById(UUID permissionId);

	List<PermissionItem> getPermissions(List<UUID> groupIds) throws ServiceException;

	void deletePermission(PermissionItem permission) throws ServiceException;

	PermissionList listByCriteria(PermissionCriteria criteria) throws ServiceException;

	void removePermission(PermissionItem permission, GroupItem group) throws ServiceException;

}
