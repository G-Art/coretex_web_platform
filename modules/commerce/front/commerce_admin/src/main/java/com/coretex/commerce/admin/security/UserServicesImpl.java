package com.coretex.commerce.admin.security;

import com.coretex.commerce.admin.Constants;
import com.coretex.commerce.core.services.GroupService;
import com.coretex.commerce.core.services.StoreService;
import com.coretex.commerce.core.services.UserService;
import com.coretex.enums.cx_core.GroupTypeEnum;
import com.coretex.items.cx_core.GroupItem;
import com.coretex.items.cx_core.PermissionItem;
import com.coretex.items.cx_core.StoreItem;
import com.coretex.items.cx_core.UserItem;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ResourceLoader;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;


@Service("userDetailsService")
public class UserServicesImpl implements WebUserServices {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserServicesImpl.class);

	private static final String DEFAULT_INITIAL_PASSWORD = "nimda";
	private static final String DEFAULT_ADMIN_INITIAL_PASSWORD = "goodmood";

	@Resource
	private UserService userService;

	@Resource
	private StoreService storeService;

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

			user = userService.findByLoginCredentials(userName);

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
			LOGGER.error("Exception while querying user", e);
			throw new SecurityDataAccessException("Exception while querying user", e);
		}


		var secUser = new PrincipalUser(userName, user.getPassword(), user.getActive(), true,
				true, true, authorities);

		secUser.setEmail(user.getEmail());
		secUser.setFirstName(user.getFirstName());
		secUser.setLastName(user.getLastName());
		secUser.setId(user.getUuid());
		return secUser;
	}


	public void createDefaultAdmin()  {

		var s = storeService.getByCode(com.coretex.commerce.core.constants.Constants.DEFAULT_STORE);

		String password = passwordEncoder.encode(DEFAULT_INITIAL_PASSWORD);

		List<GroupItem> groups = groupService.listGroup(GroupTypeEnum.ADMIN);

		UserItem user = new UserItem();
		user.setLogin("admin");
		user.setPassword(password);
		user.setEmail("admin@coretex.com");
		user.setFirstName("Administrator");
		user.setLastName("UserItem");
		user.setActive(true);

		for (GroupItem group : groups) {
			if (group.getGroupName().equals(Constants.GROUP_SUPERADMIN)
					|| group.getGroupName().equals(Constants.GROUP_ADMIN)) {
				user.getGroups().add(group);
			}
		}

		user.setStore(s);
		userService.create(user);

		creteAdmins(s);

	}

	private void creteAdmins(StoreItem store) {
		org.springframework.core.io.Resource permissionXML = resourceLoader.getResource("classpath:/permission/admins.json");

		try {
			InputStream xmlSource = permissionXML.getInputStream();
			List<GroupItem> groups = groupService.listGroup(GroupTypeEnum.ADMIN);
			Map admins = jacksonObjectMapper.readValue(xmlSource, Map.class);
			List<Map> employee = (List<Map>) admins.get("employee");
			String password = passwordEncoder.encode(DEFAULT_ADMIN_INITIAL_PASSWORD);
			employee.forEach(map -> {
				UserItem user = new UserItem();
				user.setLogin((String) map.get("name"));
				user.setPassword(password);
				user.setEmail((String) map.get("email"));
				user.setFirstName((String) map.get("firstName"));
				user.setActive((Boolean) map.get("active"));
				List<String> groupList = (List<String>) map.get("groups");
				for (GroupItem group : groups) {
					if (groupList.contains(group.getGroupName())) {
						user.getGroups().add(group);
					}
				}
				user.setStore(store);
				userService.save(user);
			});

		} catch (IOException e) {
			LOGGER.error("admins.json read exception", e);
		}

	}


}
