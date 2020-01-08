package com.coretex.shop.store.security;

import com.coretex.core.business.services.user.GroupService;
import com.coretex.core.business.services.user.PermissionService;
import com.coretex.items.cx_core.CustomerItem;
import com.coretex.shop.store.security.user.CustomerDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;


/**
 * @author casams1
 * http://stackoverflow.com/questions/5105776/spring-security-with
 * -custom-user-details
 */
@Service("customerDetailsService")
public class CustomerServicesImpl extends AbstractCustomerServices {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerServicesImpl.class);

	private PermissionService permissionService;
	private GroupService groupService;

	public CustomerServicesImpl(PermissionService permissionService, GroupService groupService) {
		super(permissionService, groupService);
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
