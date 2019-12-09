package com.coretex.commerce.admin.controllers.user;

import com.coretex.commerce.admin.controllers.PageableDataTableAbstractController;
import com.coretex.commerce.admin.data.minimal.MinimalCustomerData;
import com.coretex.commerce.admin.facades.CustomerFacade;
import com.coretex.commerce.admin.facades.PageableDataTableFacade;
import com.coretex.items.commerce_core_model.CustomerItem;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;

@Controller
@RequestMapping("/customer")
public class CustomerController extends PageableDataTableAbstractController<MinimalCustomerData> {

	@Resource
	private CustomerFacade customerFacade;

	@RequestMapping(path = "", method = RequestMethod.GET)
	public String getCustomers() {
		return "customer/customers";
	}

	@Override
	protected PageableDataTableFacade<CustomerItem, MinimalCustomerData> getPageableFacade() {
		return customerFacade;
	}
}
