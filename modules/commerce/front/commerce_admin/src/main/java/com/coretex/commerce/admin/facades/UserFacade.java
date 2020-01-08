package com.coretex.commerce.admin.facades;

import com.coretex.commerce.data.minimal.MinimalUserData;
import com.coretex.commerce.data.UserData;
import com.coretex.items.cx_core.UserItem;
import com.coretex.commerce.facades.PageableDataTableFacade;

import java.util.UUID;

public interface UserFacade extends PageableDataTableFacade<UserItem, MinimalUserData> {

	UserData getCurrentUser();

	UserData getUserByUUID(UUID uuid);

	UserData getUserByLogin(String login);

	UserData getUserByLoginOrEmail(String value);

	void saveUser(UserData userData);

}
