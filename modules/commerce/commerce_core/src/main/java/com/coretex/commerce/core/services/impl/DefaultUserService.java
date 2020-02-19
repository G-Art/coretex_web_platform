package com.coretex.commerce.core.services.impl;

import com.coretex.commerce.core.dao.UserDao;
import com.coretex.commerce.core.services.AbstractGenericItemService;
import com.coretex.commerce.core.services.UserService;
import com.coretex.items.cx_core.UserItem;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DefaultUserService extends AbstractGenericItemService<UserItem> implements UserService {

	private UserDao repository;

	public DefaultUserService(UserDao repository) {
		super(repository);
		this.repository = repository;
	}

	@Override
	public UserItem findByLoginCredentials(String loginOrEmail) {
		return repository.findByCredentials(loginOrEmail);
	}

	@Override
	public UserItem getByUserName(String login) {
		return repository.findSingle(Map.of(UserItem.LOGIN, login), true);
	}
}
