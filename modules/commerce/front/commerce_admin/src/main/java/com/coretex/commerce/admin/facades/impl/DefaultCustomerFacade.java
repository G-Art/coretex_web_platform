package com.coretex.commerce.admin.facades.impl;

import com.coretex.commerce.admin.data.minimal.MinimalCustomerData;
import com.coretex.commerce.admin.facades.CustomerFacade;
import com.coretex.commerce.admin.mapper.GenericDataMapper;
import com.coretex.commerce.admin.mapper.minimal.MinimalCustomerDataMapper;
import com.coretex.core.business.services.common.generic.PageableEntityService;
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
	public PageableEntityService<CustomerItem> getPageableService() {
		return customerService;
	}

	@Override
	public GenericDataMapper<CustomerItem, MinimalCustomerData> getDataMapper() {
		return minimalCustomerDataMapper;
	}
}
