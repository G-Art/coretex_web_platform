package com.coretex.commerce.core.services;

import com.coretex.enums.cx_core.GroupTypeEnum;
import com.coretex.items.cx_core.GroupItem;

import java.util.List;

public interface GroupService extends GenericItemService<GroupItem> {

	GroupItem findByName(String name);

	List<GroupItem> listGroup(GroupTypeEnum groupType);
}
