package com.coretex.core.business.repositories.user;

import com.coretex.core.activeorm.dao.DefaultGenericDao;
import com.coretex.enums.commerce_core_model.GroupTypeEnum;
import com.coretex.items.commerce_core_model.GroupItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Component
public class GroupDaoImpl extends DefaultGenericDao<GroupItem> implements GroupDao {

	public GroupDaoImpl() {
		super(GroupItem.ITEM_TYPE);
	}

	@Override
	public GroupItem findById(UUID id) {
		return find(id);
	}

	@Override
	public List<GroupItem> findAll() {
		return null;
	}

	@Override
	public List<GroupItem> findByPermissions(Set<Integer> permissionIds) {
		return null;
	}

	@Override
	public List<GroupItem> findByIds(Set<UUID> groupIds) {
		return null;
	}

	@Override
	public List<GroupItem> findByNames(List<String> groupeNames) {
		return null;
	}

	@Override
	public List<GroupItem> findByType(GroupTypeEnum type) {
		return find(Map.of(GroupItem.GROUP_TYPE, type));
	}

	@Override
	public GroupItem findByGroupName(String name) {
		return null;
	}
}
