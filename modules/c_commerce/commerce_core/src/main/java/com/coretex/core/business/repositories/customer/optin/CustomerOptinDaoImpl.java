package com.coretex.core.business.repositories.customer.optin;

import com.coretex.core.activeorm.dao.Dao;
import com.coretex.core.activeorm.dao.DefaultGenericDao;
import com.coretex.items.commerce_core_model.CustomerOptinItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class CustomerOptinDaoImpl extends DefaultGenericDao<CustomerOptinItem> implements CustomerOptinDao {

	public CustomerOptinDaoImpl() {
		super(CustomerOptinItem.ITEM_TYPE);
	}

	@Override
	public CustomerOptinItem findByMerchantAndCodeAndEmail(UUID storeId, String code, String email) {
		return null;
	}
}
