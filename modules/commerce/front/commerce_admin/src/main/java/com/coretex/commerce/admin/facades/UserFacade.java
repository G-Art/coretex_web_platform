package com.coretex.commerce.admin.facades;

import com.coretex.commerce.admin.data.UserData;

import java.util.UUID;

public interface UserFacade extends PageableDataTableFacade<UserData> {

	UserData getCurrentUser();

	UserData getUserByUUID(UUID uuid);

	UserData getUserByLogin(String login);

	UserData getUserByLoginOrEmail(String value);

	void saveUser(UserData userData);

}