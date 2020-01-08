package com.coretex.commerce.facades.impl;

import com.coretex.commerce.core.services.CustomerService;
import com.coretex.commerce.data.minimal.MinimalCustomerData;
import com.coretex.commerce.data.requests.RegisterRequest;
import com.coretex.commerce.data.requests.RegisterResponse;
import com.coretex.commerce.facades.CustomerFacade;
import com.coretex.commerce.mapper.GenericDataMapper;
import com.coretex.commerce.mapper.minimal.MinimalCustomerDataMapper;
import com.coretex.commerce.core.services.PageableService;
import com.coretex.items.cx_core.CustomerItem;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component("customerFacade")
public class DefaultCustomerFacade implements CustomerFacade {

	@Resource
	private CustomerService customerService;

	@Resource
	private MinimalCustomerDataMapper minimalCustomerDataMapper;

	@Override
	public PageableService<CustomerItem> getPageableService() {
		return customerService;
	}

	@Override
	public GenericDataMapper<CustomerItem, MinimalCustomerData> getDataMapper() {
		return minimalCustomerDataMapper;
	}

	@Override
	public boolean isEmailExist(String email) {
		return customerService.isEmailExist(email);
	}

	@Override
	public RegisterResponse register(RegisterRequest registerRequest) {
		if(!isEmailExist(registerRequest.getEmail())){
			var customer = new CustomerItem();

			customer.setEmail(registerRequest.getEmail());
			customer.setFirstName(registerRequest.getFirstName());
			customer.setLastName(registerRequest.getLastName());
			customer.setPassword(registerRequest.getEncodedPassword());

			customerService.save(customer);
		}
		return new RegisterResponse();
	}
}
