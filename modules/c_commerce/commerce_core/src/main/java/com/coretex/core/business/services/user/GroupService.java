package com.coretex.core.business.services.user;

import java.util.List;
import java.util.Set;
import java.util.UUID;


import com.coretex.core.business.services.common.generic.SalesManagerEntityService;
import com.coretex.items.commerce_core_model.GroupItem;
import com.coretex.enums.commerce_core_model.GroupTypeEnum;


public interface GroupService extends SalesManagerEntityService<GroupItem> {


	List<GroupItem> listGroup(GroupTypeEnum groupType);

	List<GroupItem> listGroupByIds(Set<UUID> ids) ;

	List<GroupItem> listGroupByNames(List<String> names) ;

	GroupItem findByName(String groupName);

}
