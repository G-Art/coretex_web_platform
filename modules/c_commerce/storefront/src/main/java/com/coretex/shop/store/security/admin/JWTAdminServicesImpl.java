package com.coretex.shop.store.security.admin;

import com.coretex.core.business.services.user.UserService;
import com.coretex.items.commerce_core_model.GroupItem;
import com.coretex.items.commerce_core_model.PermissionItem;
import com.coretex.items.commerce_core_model.UserItem;
import com.coretex.shop.constants.Constants;
import com.coretex.shop.store.security.user.JWTUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Service("jwtAdminDetailsService")
public class JWTAdminServicesImpl implements UserDetailsService {

	private static final Logger LOGGER = LoggerFactory.getLogger(JWTAdminServicesImpl.class);

	@Resource
	private UserService userService;

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
		Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

			LOGGER.debug("Loading user by user id: {}", userName);

		UserItem user = userService.getByUserName(userName);

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


		return userDetails(userName, user, authorities);
	}

}
