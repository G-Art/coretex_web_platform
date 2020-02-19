package com.coretex.commerce.core.services;


import com.coretex.items.cx_core.UserItem;

public interface UserService extends GenericItemService<UserItem> {

	UserItem findByLoginCredentials(String loginOrEmail);

	UserItem getByUserName(String login);
}
