package com.coretex.commerce.core.services;


import com.coretex.items.cx_core.CustomerItem;

public interface CustomerService extends GenericItemService<CustomerItem> {

	boolean isEmailExist(String email);

	CustomerItem getByEmail(String email);
}
