package com.coretex.shop.store.security;

import com.coretex.core.business.services.user.GroupService;
import com.coretex.core.business.services.user.PermissionService;
import com.coretex.items.cx_core.CustomerItem;
import com.coretex.shop.store.security.user.JWTUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;


@Service("jwtCustomerDetailsService")
public class JWTCustomerServicesImpl extends AbstractCustomerServices {


	public JWTCustomerServicesImpl(PermissionService permissionService, GroupService groupService) {
		super(permissionService, groupService);
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
