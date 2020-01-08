package com.coretex.core.model.customer;

import com.coretex.core.model.common.EntityList;
import com.coretex.items.cx_core.CustomerItem;

import java.util.List;

public class CustomerList extends EntityList {



	private static final long serialVersionUID = -3108842276158069739L;
	private List<CustomerItem> customers;

	public void setCustomers(List<CustomerItem> customers) {
		this.customers = customers;
	}

	public List<CustomerItem> getCustomers() {
		return customers;
	}

}
