package com.coretex.core.business.repositories.customer.attribute;

import com.coretex.core.activeorm.dao.DefaultGenericDao;
import com.coretex.items.commerce_core_model.CustomerOptionSetItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class CustomerOptionSetDaoImpl extends DefaultGenericDao<CustomerOptionSetItem> implements CustomerOptionSetDao {


	public CustomerOptionSetDaoImpl() {
		super(CustomerOptionSetItem.ITEM_TYPE);
	}

	@Override
	public CustomerOptionSetItem findOne(UUID id) {
		return find(id);
	}

	@Override
	public List<CustomerOptionSetItem> findByOptionId(UUID merchantStoreId, UUID id) {
		return null;
	}

	@Override
	public List<CustomerOptionSetItem> findByOptionValueId(UUID merchantStoreId, UUID id) {
		return null;
	}

	@Override
	public List<CustomerOptionSetItem> findByStore(UUID merchantStoreId, UUID languageId) {
		return null;
	}
}
