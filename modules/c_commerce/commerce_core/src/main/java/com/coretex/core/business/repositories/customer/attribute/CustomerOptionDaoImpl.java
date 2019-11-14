package com.coretex.core.business.repositories.customer.attribute;

import com.coretex.core.activeorm.dao.Dao;
import com.coretex.core.activeorm.dao.DefaultGenericDao;
import com.coretex.items.commerce_core_model.CustomerOptinItem;
import com.coretex.items.commerce_core_model.CustomerOptionItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class CustomerOptionDaoImpl extends DefaultGenericDao<CustomerOptionItem> implements CustomerOptionDao{


	public CustomerOptionDaoImpl() {
		super(CustomerOptionItem.ITEM_TYPE);
	}

	@Override
	public CustomerOptionItem findOne(UUID id) {
		return find(id);
	}

	@Override
	public CustomerOptionItem findByCode(UUID merchantId, String code) {
		return findSingle(Map.of(CustomerOptionItem.MERCHANT_STORE, merchantId, CustomerOptionItem.CODE, code), true);
	}

	@Override
	public List<CustomerOptionItem> findByStore(UUID merchantId, UUID languageId) {
		return find(Map.of(CustomerOptionItem.MERCHANT_STORE, merchantId));
	}
}
