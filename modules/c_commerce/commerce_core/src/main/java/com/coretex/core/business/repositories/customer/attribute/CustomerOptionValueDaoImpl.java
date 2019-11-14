package com.coretex.core.business.repositories.customer.attribute;

import com.coretex.core.activeorm.dao.DefaultGenericDao;
import com.coretex.items.commerce_core_model.CustomerOptionValueItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class CustomerOptionValueDaoImpl extends DefaultGenericDao<CustomerOptionValueItem> implements CustomerOptionValueDao {


	public CustomerOptionValueDaoImpl() {
		super(CustomerOptionValueItem.ITEM_TYPE);
	}

	@Override
	public CustomerOptionValueItem findOne(UUID id) {
		return find(id);
	}

	@Override
	public CustomerOptionValueItem findByCode(UUID merchantId, String code) {
		return null;
	}

	@Override
	public List<CustomerOptionValueItem> findByStore(UUID merchantId, UUID languageId) {
		return null;
	}
}
