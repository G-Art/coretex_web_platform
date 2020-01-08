package com.coretex.commerce.core.dao.impl;

import com.coretex.commerce.core.dao.CustomerDao;
import com.coretex.core.activeorm.dao.DefaultGenericDao;
import com.coretex.items.cx_core.CustomerItem;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DefaultCustomerDao extends DefaultGenericDao<CustomerItem> implements CustomerDao {

	public DefaultCustomerDao() {
		super(CustomerItem.ITEM_TYPE);
	}

	public CustomerItem getByEmail(String email) {
		return findSingle(Map.of(CustomerItem.EMAIL, email), true);
	}

	@Override
	public boolean isEmailExist(String email) {
		return getSearchService()
				.search("SELECT count(c.uuid) "
						+ "FROM " + CustomerItem.ITEM_TYPE + " AS c "
						+ "WHERE c." + CustomerItem.EMAIL + " = :email", Map.of(CustomerItem.EMAIL, email))
				.getCount() > 0;
	}
}
