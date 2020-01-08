package com.coretex.commerce.core.dao;

import com.coretex.core.activeorm.dao.Dao;
import com.coretex.items.cx_core.CustomerItem;

public interface CustomerDao extends Dao<CustomerItem> {
	CustomerItem getByEmail(String email);

	boolean isEmailExist(String email);
}
