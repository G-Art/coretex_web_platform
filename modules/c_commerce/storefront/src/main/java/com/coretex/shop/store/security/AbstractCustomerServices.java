package com.coretex.shop.store.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import com.coretex.items.commerce_core_model.CustomerItem;
import com.coretex.items.commerce_core_model.PermissionItem;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


import com.coretex.core.business.services.customer.CustomerService;
import com.coretex.core.business.services.user.GroupService;
import com.coretex.core.business.services.user.PermissionService;
import com.coretex.items.commerce_core_model.GroupItem;
import com.coretex.shop.admin.security.SecurityDataAccessException;
import com.coretex.shop.constants.Constants;

public abstract class AbstractCustomerServices implements UserDetailsService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractCustomerServices.class);

	protected CustomerService customerService;
	protected PermissionService permissionService;
	protected GroupService groupService;

	public final static String ROLE_PREFIX = "ROLE_";//Spring Security 4

	public AbstractCustomerServices(
			CustomerService customerService,
			PermissionService permissionService,
			GroupService groupService) {

		this.customerService = customerService;
		this.permissionService = permissionService;
		this.groupService = groupService;
	}

	protected abstract UserDetails userDetails(String userName, CustomerItem customer, Collection<GrantedAuthority> authorities);


	public UserDetails loadUserByUsername(String userName)
			throws UsernameNotFoundException, DataAccessException {
		CustomerItem user = null;
		Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

		try {

			LOGGER.debug("Loading user by user id: {}", userName);

			user = customerService.getByNick(userName);

			if (user == null) {
				//return null;
				throw new UsernameNotFoundException("UserItem " + userName + " not found");
			}


			GrantedAuthority role = new SimpleGrantedAuthority(ROLE_PREFIX + Constants.PERMISSION_CUSTOMER_AUTHENTICATED);//required to login
			authorities.add(role);

			List<UUID> groupsId = new ArrayList<>();
			List<GroupItem> groups = user.getGroups();
			for (GroupItem group : groups) {
				for (PermissionItem permission : group.getPermissions()) {
					GrantedAuthority auth = new SimpleGrantedAuthority(permission.getPermissionName());
					authorities.add(auth);
				}
			}


		} catch (Exception e) {
			LOGGER.error("Exception while querrying customer", e);
			throw new SecurityDataAccessException("Cannot authenticate customer", e);
		}

		return userDetails(userName, user, authorities);

	}

}
