package com.coretex.core.business.services.user;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.coretex.items.commerce_core_model.GroupItem;
import org.springframework.stereotype.Service;

import com.coretex.core.business.repositories.user.GroupDao;
import com.coretex.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.coretex.enums.commerce_core_model.GroupTypeEnum;


@Service("groupService")
public class GroupServiceImpl extends SalesManagerEntityServiceImpl<GroupItem>
		implements GroupService {

	GroupDao groupDao;


	public GroupServiceImpl(GroupDao groupDao) {
		super(groupDao);
		this.groupDao = groupDao;

	}


	@Override
	public List<GroupItem> listGroup(GroupTypeEnum groupType) {
		return groupDao.findByType(groupType);
	}

	public List<GroupItem> listGroupByIds(Set<UUID> ids)  {
		try {
			return groupDao.findByIds(ids);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}


	@Override
	public GroupItem findByName(String groupName) {
		return groupDao.findByGroupName(groupName);
	}


	@Override
	public List<GroupItem> listGroupByNames(List<String> names) {
		return groupDao.findByNames(names);
	}


}
