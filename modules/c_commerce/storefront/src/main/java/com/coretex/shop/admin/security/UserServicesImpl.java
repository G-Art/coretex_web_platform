package com.coretex.shop.admin.security;

import com.coretex.core.business.services.merchant.MerchantStoreService;
import com.coretex.core.business.services.user.GroupService;
import com.coretex.core.business.services.user.UserService;
import com.coretex.items.commerce_core_model.GroupItem;
import com.coretex.items.commerce_core_model.PermissionItem;
import com.coretex.items.cx_core.UserItem;
import com.coretex.shop.constants.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ResourceLoader;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * @author casams1
 * http://stackoverflow.com/questions/5105776/spring-security-with
 * -custom-user-details
 */
@Service("userDetailsService")
public class UserServicesImpl implements WebUserServices {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserServicesImpl.class);

	private static final String DEFAULT_INITIAL_PASSWORD = "nimda";
	private static final String DEFAULT_ADMIN_INITIAL_PASSWORD = "goodmood";

	@Resource
	private UserService userService;


	@Resource
	private MerchantStoreService merchantService;

	@Resource
	@Qualifier("passwordEncoder")
	private PasswordEncoder passwordEncoder;

	@Resource
	protected GroupService groupService;

	@Resource
	private ResourceLoader resourceLoader;

	@Resource
	private ObjectMapper jacksonObjectMapper;

	public final static String ROLE_PREFIX = "ROLE_";//Spring Security 4


	public UserDetails loadUserByUsername(String userName)
			throws UsernameNotFoundException, DataAccessException {

		UserItem user = null;
		Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

		try {

			user = userService.getByUserName(userName);

			if (user == null) {
				return null;
			}

			GrantedAuthority role = new SimpleGrantedAuthority(ROLE_PREFIX + Constants.PERMISSION_AUTHENTICATED);//required to login
			authorities.add(role);

			List<GroupItem> groups = user.getGroups();
			List<PermissionItem> permissions = new ArrayList<>();
			for (GroupItem group : groups) {

				permissions.addAll(group.getPermissions());

			}

			for (PermissionItem permission : permissions) {
				GrantedAuthority auth = new SimpleGrantedAuthority(ROLE_PREFIX + permission.getPermissionName());
				authorities.add(auth);
			}

		} catch (Exception e) {
			LOGGER.error("Exception while querrying user", e);
			throw new SecurityDataAccessException("Exception while querrying user", e);
		}


		User secUser = new User(userName, user.getPassword(), user.getActive(), true,
				true, true, authorities);
		return secUser;
	}

}
