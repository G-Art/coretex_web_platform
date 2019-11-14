package com.coretex.shop.store.security;

import java.util.Collection;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.coretex.core.business.services.customer.CustomerService;
import com.coretex.core.business.services.user.GroupService;
import com.coretex.core.business.services.user.PermissionService;
import com.coretex.items.commerce_core_model.CustomerItem;
import com.coretex.shop.store.security.user.CustomerDetails;


/**
 * @author casams1
 * http://stackoverflow.com/questions/5105776/spring-security-with
 * -custom-user-details
 */
@Service("customerDetailsService")
public class CustomerServicesImpl extends AbstractCustomerServices {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerServicesImpl.class);


	private CustomerService customerService;
	private PermissionService permissionService;
	private GroupService groupService;

	public CustomerServicesImpl(CustomerService customerService, PermissionService permissionService, GroupService groupService) {
		super(customerService, permissionService, groupService);
		this.customerService = customerService;
		this.permissionService = permissionService;
		this.groupService = groupService;
	}

	@Override
	protected UserDetails userDetails(String userName, CustomerItem customer, Collection<GrantedAuthority> authorities) {

		CustomerDetails authUser = new CustomerDetails(userName, customer.getPassword(), true, true,
				true, true, authorities);

		authUser.setEmail(customer.getEmail());
		authUser.setId(customer.getUuid());

		return authUser;
	}


}
