package com.coretex.commerce.admin.facades.impl;

import com.coretex.commerce.admin.controllers.DataTableResults;
import com.coretex.commerce.admin.data.MinimalUserData;
import com.coretex.commerce.admin.data.UserData;
import com.coretex.commerce.admin.facades.UserFacade;
import com.coretex.commerce.admin.mapper.GenericDataMapper;
import com.coretex.commerce.admin.mapper.MinimalUserDataMapper;
import com.coretex.commerce.admin.mapper.UserDataMapper;
import com.coretex.commerce.admin.security.PrincipalUser;
import com.coretex.core.business.services.common.generic.PageableEntityService;
import com.coretex.core.business.services.user.UserService;
import com.coretex.items.commerce_core_model.UserItem;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.UUID;

@Component("userFacade")
public class DefaultUserFacade implements UserFacade {

	@Resource
	private UserDataMapper userDataMapper;

	@Resource
	private UserService userService;

	@Resource
	private MinimalUserDataMapper minimalUserDataMapper;

	@Override
	public UserData getCurrentUser() {
		var authentication = SecurityContextHolder.getContext().getAuthentication();

		var details = authentication.getDetails();
		if (details instanceof PrincipalUser) {
			if (Objects.nonNull(((PrincipalUser) details).getId())) {
				return getUserByUUID(((PrincipalUser) details).getId());
			}
			if (Objects.nonNull(((PrincipalUser) details).getEmail())) ;
			{
				return getUserByLoginOrEmail(((PrincipalUser) details).getEmail());
			}
		}
		return getUserByLogin(authentication.getName());
	}

	@Override
	public UserData getUserByUUID(UUID uuid) {
		var userItem = userService.getByUUID(uuid);
		return userDataMapper.fromItem(userItem);
	}

	@Override
	public UserData getUserByLogin(String login) {
		var userItem = userService.getByUserName(login);
		return userDataMapper.fromItem(userItem);
	}

	@Override
	public UserData getUserByLoginOrEmail(String value) {
		var userItem = userService.findByLoginCredentials(value);
		return userDataMapper.fromItem(userItem);
	}

	@Override
	public void saveUser(UserData userData) {
		UserItem userItem;

		if (Objects.nonNull(userData.getUuid())) {
			userItem = userService.getByUUID(userData.getUuid());
			userDataMapper.updateToItem(userData, userItem);
		} else {
			userItem = userDataMapper.toItem(userData);
		}

		userService.save(userItem);

		userDataMapper.updateFromItem(userItem, userData);
	}

	@Override
	public PageableEntityService<UserItem> getPageableService() {
		return userService;
	}

	@Override
	public GenericDataMapper<UserItem, MinimalUserData> getDataMapper() {
		return minimalUserDataMapper;
	}
}
