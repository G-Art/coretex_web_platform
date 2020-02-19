package com.coretex.commerce.core.services.impl;

import com.coretex.commerce.core.dao.GroupDao;
import com.coretex.commerce.core.services.AbstractGenericItemService;
import com.coretex.commerce.core.services.GroupService;
import com.coretex.enums.cx_core.GroupTypeEnum;
import com.coretex.items.cx_core.GroupItem;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DefaultGroupService extends AbstractGenericItemService<GroupItem> implements GroupService {

	private GroupDao repository;

	public DefaultGroupService(GroupDao repository) {
		super(repository);
		this.repository = repository;
	}

	@Override
	public GroupItem findByName(String name) {
		return repository.findSingle(Map.of(GroupItem.GROUP_NAME, name), true);
	}

	@Override
	public List<GroupItem> listGroup(GroupTypeEnum groupType) {
		return repository.findByType(groupType);
	}
}
