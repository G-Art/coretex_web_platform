package com.coretex.shop.store.controller.security.facade;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import javax.annotation.Resource;

import com.coretex.items.commerce_core_model.GroupItem;
import org.springframework.stereotype.Service;
import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.business.services.user.GroupService;
import com.coretex.core.business.services.user.PermissionService;
import com.coretex.core.model.user.PermissionCriteria;
import com.coretex.core.model.user.PermissionList;
import com.coretex.shop.model.security.ReadablePermission;
import com.coretex.shop.store.api.exception.ServiceRuntimeException;

@Service("securityFacade")
public class SecurityFacadeImpl implements SecurityFacade {

	@Resource
	private PermissionService permissionService;

	@Resource
	private GroupService groupService;

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public List<ReadablePermission> getPermissions(List<String> groups) {

		List<GroupItem> userGroups = null;
		try {
			userGroups = groupService.listGroupByNames(groups);


			// TODO if groups == null

			List<UUID> ids = new ArrayList<>();
			for (GroupItem g : userGroups) {
				ids.add(g.getUuid());
			}

			PermissionCriteria criteria = new PermissionCriteria();
			criteria.setGroupIds(new HashSet(ids));

			PermissionList permissions = permissionService.listByCriteria(criteria);
			throw new ServiceRuntimeException("Not implemented");
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

}
