package com.coretex.commerce.facades;

import com.coretex.commerce.data.minimal.MinimalCustomerData;
import com.coretex.commerce.data.requests.RegisterRequest;
import com.coretex.commerce.data.requests.RegisterResponse;
import com.coretex.items.cx_core.CustomerItem;

public interface CustomerFacade extends PageableDataTableFacade<CustomerItem, MinimalCustomerData> {

	boolean isEmailExist(String email);

	RegisterResponse register(RegisterRequest registerRequest, String domain);

}
