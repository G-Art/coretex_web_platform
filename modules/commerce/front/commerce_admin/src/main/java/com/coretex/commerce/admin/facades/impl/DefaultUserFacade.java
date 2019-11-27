package com.coretex.commerce.admin.facades.impl;

import com.coretex.commerce.admin.data.UserData;
import com.coretex.commerce.admin.facades.UserFacade;
import com.coretex.commerce.admin.mapper.UserDataMapper;
import com.coretex.commerce.admin.security.PrincipalUser;
import com.coretex.core.business.services.user.UserService;
import com.coretex.items.commerce_core_model.UserItem;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.UUID;

@Component
public class DefaultUserFacade implements UserFacade {

	@Resource
	private UserDataMapper userDataMapper;

	@Resource
	private UserService userService;

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
}
