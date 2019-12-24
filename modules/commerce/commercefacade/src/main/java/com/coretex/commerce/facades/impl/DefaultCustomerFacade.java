package com.coretex.commerce.facades.impl;

import com.coretex.commerce.data.minimal.MinimalCustomerData;
import com.coretex.commerce.facades.CustomerFacade;
import com.coretex.commerce.mapper.GenericDataMapper;
import com.coretex.commerce.mapper.minimal.MinimalCustomerDataMapper;
import com.coretex.commerce.core.services.PageableService;
import com.coretex.core.business.services.customer.CustomerService;
import com.coretex.items.commerce_core_model.CustomerItem;
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
}
