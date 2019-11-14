package com.coretex.shop.store.security.admin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import com.coretex.items.commerce_core_model.PermissionItem;
import com.coretex.items.commerce_core_model.UserItem;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.business.services.user.GroupService;
import com.coretex.core.business.services.user.PermissionService;
import com.coretex.core.business.services.user.UserService;
import com.coretex.items.commerce_core_model.GroupItem;
import com.coretex.shop.admin.security.SecurityDataAccessException;
import com.coretex.shop.constants.Constants;
import com.coretex.shop.store.security.user.JWTUser;


@Service("jwtAdminDetailsService")
public class JWTAdminServicesImpl implements UserDetailsService {

	private static final Logger LOGGER = LoggerFactory.getLogger(JWTAdminServicesImpl.class);


	@Resource
	private UserService userService;
	@Resource
	private PermissionService permissionService;
	@Resource
	private GroupService groupService;

	public final static String ROLE_PREFIX = "ROLE_";//Spring Security 4


	private UserDetails userDetails(String userName, UserItem user, Collection<GrantedAuthority> authorities) {


		return new JWTUser(
				user.getUuid(),
				userName,
				user.getFirstName(),
				user.getLastName(),
				user.getEmail(),
				user.getPassword(),
				authorities,
				true,
				user.getUpdateDate()
		);
	}

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		UserItem user = null;
		Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

		try {

			LOGGER.debug("Loading user by user id: {}", userName);

			user = userService.getByUserName(userName);

			if (user == null) {
				//return null;
				throw new UsernameNotFoundException("UserItem " + userName + " not found");
			}

			GrantedAuthority role = new SimpleGrantedAuthority(ROLE_PREFIX + Constants.PERMISSION_AUTHENTICATED);//required to login
			authorities.add(role);

			List<GroupItem> groups = user.getGroups();
			for (GroupItem group : groups) {
				for (PermissionItem permission : group.getPermissions()) {
					GrantedAuthority auth = new SimpleGrantedAuthority(permission.getPermissionName());
					authorities.add(auth);
				}
			}

		} catch (ServiceException e) {
			LOGGER.error("Exception while querrying customer", e);
			throw new SecurityDataAccessException("Cannot authenticate customer", e);
		}

		return userDetails(userName, user, authorities);
	}

}
