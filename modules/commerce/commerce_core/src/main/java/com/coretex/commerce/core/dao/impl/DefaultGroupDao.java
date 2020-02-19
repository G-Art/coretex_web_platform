package com.coretex.commerce.core.dao.impl;

import com.coretex.commerce.core.dao.GroupDao;
import com.coretex.core.activeorm.dao.DefaultGenericDao;
import com.coretex.enums.cx_core.GroupTypeEnum;
import com.coretex.items.cx_core.GroupItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class DefaultGroupDao extends DefaultGenericDao<GroupItem> implements GroupDao {
	public DefaultGroupDao() {
		super(GroupItem.ITEM_TYPE);
	}

	@Override
	public List<GroupItem> findByType(GroupTypeEnum type) {
		return find(Map.of(GroupItem.GROUP_TYPE, type));
	}
}
