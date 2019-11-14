package com.coretex.shop.admin.security;

import com.coretex.core.business.services.merchant.MerchantStoreService;
import com.coretex.core.business.services.user.GroupService;
import com.coretex.core.business.services.user.PermissionService;
import com.coretex.core.business.services.user.UserService;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.GroupItem;
import com.coretex.enums.commerce_core_model.GroupTypeEnum;
import com.coretex.items.commerce_core_model.PermissionItem;
import com.coretex.items.commerce_core_model.UserItem;
import com.coretex.shop.constants.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.inject.Named;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;


/**
 * @author casams1
 * http://stackoverflow.com/questions/5105776/spring-security-with
 * -custom-user-details
 */
@Service("userDetailsService")
public class UserServicesImpl implements WebUserServices {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserServicesImpl.class);

	private static final String DEFAULT_INITIAL_PASSWORD = "nimda";

	@Resource
	private UserService userService;


	@Resource
	private MerchantStoreService merchantService;

	@Resource
	@Named("passwordEncoder")
	private PasswordEncoder passwordEncoder;


	@Resource
	protected PermissionService permissionService;

	@Resource
	protected GroupService groupService;

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


	public void createDefaultAdmin() throws Exception {

		//TODO create all groups and permissions

		MerchantStoreItem store = merchantService.getByCode(com.coretex.core.business.constants.Constants.DEFAULT_STORE);

		String password = passwordEncoder.encode(DEFAULT_INITIAL_PASSWORD);

		List<GroupItem> groups = groupService.listGroup(GroupTypeEnum.ADMIN);

		UserItem user = new UserItem();
		user.setAdminName("admin");
		user.setPassword(password);
		user.setEmail("admin@coretex.com");
		user.setFirstName("Administrator");
		user.setLastName("UserItem");
		user.setActive(true);

		for (GroupItem group : groups) {
			if (group.getGroupName().equals(Constants.GROUP_SUPERADMIN) || group.getGroupName().equals(Constants.GROUP_ADMIN)) {
				user.getGroups().add(group);
			}
		}

		user.setMerchantStore(store);
		userService.create(user);


	}


}
