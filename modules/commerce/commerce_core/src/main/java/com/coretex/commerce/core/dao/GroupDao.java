package com.coretex.commerce.core.dao;

import com.coretex.core.activeorm.dao.Dao;
import com.coretex.enums.cx_core.GroupTypeEnum;
import com.coretex.items.cx_core.GroupItem;

import java.util.List;

public interface GroupDao extends Dao<GroupItem> {

	List<GroupItem> findByType(GroupTypeEnum groupType);
}
