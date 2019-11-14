package com.coretex.shop.store.security;

import java.util.Collection;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.coretex.core.business.services.customer.CustomerService;
import com.coretex.core.business.services.user.GroupService;
import com.coretex.core.business.services.user.PermissionService;
import com.coretex.items.commerce_core_model.CustomerItem;
import com.coretex.shop.store.security.user.JWTUser;


@Service("jwtCustomerDetailsService")
public class JWTCustomerServicesImpl extends AbstractCustomerServices {


	public JWTCustomerServicesImpl(CustomerService customerService, PermissionService permissionService, GroupService groupService) {
		super(customerService, permissionService, groupService);
		this.customerService = customerService;
		this.permissionService = permissionService;
		this.groupService = groupService;
	}

	@Override
	protected UserDetails userDetails(String userName, CustomerItem customer, Collection<GrantedAuthority> authorities) {


		return new JWTUser(
				customer.getUuid(),
				userName,
				customer.getBilling().getFirstName(),
				customer.getBilling().getLastName(),
				customer.getEmail(),
				customer.getPassword(),
				authorities,
				true,
				customer.getUpdateDate()
		);
	}

}
