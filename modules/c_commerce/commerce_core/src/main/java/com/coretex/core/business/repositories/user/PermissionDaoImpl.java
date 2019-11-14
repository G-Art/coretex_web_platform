package com.coretex.core.business.repositories.user;

import com.coretex.core.activeorm.dao.DefaultGenericDao;
import com.coretex.core.model.user.PermissionCriteria;
import com.coretex.core.model.user.PermissionList;
import com.coretex.items.commerce_core_model.PermissionItem;
import com.coretex.relations.commerce_core_model.PermissionGroupRelation;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Component
public class PermissionDaoImpl extends DefaultGenericDao<PermissionItem> implements PermissionDao {


	public PermissionDaoImpl() {
		super(PermissionItem.ITEM_TYPE);
	}

	@Override
	public PermissionItem findOne(UUID id) {
		return find(id);
	}

	@Override
	public List<PermissionItem> findAll() {
		return find();
	}

	@Override
	public List<PermissionItem> findByGroups(Set<UUID> groupIds) {
		String query = "SELECT p.* FROM " + PermissionItem.ITEM_TYPE +" AS p " +
				"LEFT JOIN " + PermissionGroupRelation.ITEM_TYPE + "AS pgr ON (pgr.source = p.uuid) " +
				"WHERE pgr.target in (:groups)";
		return getSearchService().<PermissionItem>search(query, Map.of("groups", groupIds)).getResult();
	}

	@Override
	public PermissionList listByCriteria(PermissionCriteria criteria) {
		return null;
	}
}
