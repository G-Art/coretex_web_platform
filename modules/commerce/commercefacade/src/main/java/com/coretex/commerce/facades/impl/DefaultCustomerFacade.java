package com.coretex.commerce.facades.impl;

import com.coretex.commerce.core.services.CustomerService;
import com.coretex.commerce.core.services.PageableService;
import com.coretex.commerce.core.services.StoreService;
import com.coretex.commerce.data.minimal.MinimalCustomerData;
import com.coretex.commerce.data.requests.RegisterRequest;
import com.coretex.commerce.data.requests.RegisterResponse;
import com.coretex.commerce.facades.CustomerFacade;
import com.coretex.commerce.mapper.GenericDataMapper;
import com.coretex.commerce.mapper.minimal.MinimalCustomerDataMapper;
import com.coretex.items.cx_core.CustomerItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

@Component("customerFacade")
public class DefaultCustomerFacade implements CustomerFacade {

	@Resource
	private CustomerService customerService;

	@Resource
	private StoreService storeService;

	@Resource
	private MinimalCustomerDataMapper minimalCustomerDataMapper;


	private static final Logger LOG = LoggerFactory.getLogger(DefaultCustomerFacade.class);

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
	public RegisterResponse register(RegisterRequest registerRequest, String domain) {
		var registerResponse = new RegisterResponse();
		if(!isEmailExist(registerRequest.getEmail())){
			var customer = new CustomerItem();

			var storeItem = storeService.getByDomain(domain);
			customer.setStore(storeItem);
			customer.setEmail(registerRequest.getEmail());
			customer.setFirstName(registerRequest.getFirstName());
			customer.setLastName(registerRequest.getLastName());
			customer.setPassword(registerRequest.getEncodedPassword());

			customerService.save(customer);
			LOG.info(String.format("New customer has been created [%s:%s]", domain, customer.getEmail()));
		}else {
			registerResponse.setHasErrors(true);
			registerResponse.setErrors(Map.of("CONFLICT", "Email exist."));
		}
		return registerResponse;
	}
}
