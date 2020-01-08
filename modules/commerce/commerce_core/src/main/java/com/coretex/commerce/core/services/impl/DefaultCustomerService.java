package com.coretex.commerce.core.services.impl;

import com.coretex.commerce.core.dao.CustomerDao;
import com.coretex.commerce.core.services.AbstractGenericItemService;
import com.coretex.commerce.core.services.CustomerService;
import com.coretex.items.cx_core.CustomerItem;
import org.springframework.stereotype.Service;

@Service
public class DefaultCustomerService extends AbstractGenericItemService<CustomerItem> implements CustomerService {

	private CustomerDao customerDao;

	public DefaultCustomerService(CustomerDao repository) {
		super(repository);
		this.customerDao = repository;
	}

	@Override
	public boolean isEmailExist(String email) {
		return customerDao.isEmailExist(email);
	}

	@Override
	public CustomerItem getByEmail(String email) {
		return customerDao.getByEmail(email);
	}
}
