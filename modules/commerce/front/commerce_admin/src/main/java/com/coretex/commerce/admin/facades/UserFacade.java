package com.coretex.commerce.admin.facades;

import com.coretex.commerce.admin.data.MinimalUserData;
import com.coretex.commerce.admin.data.UserData;
import com.coretex.items.commerce_core_model.UserItem;

import java.util.UUID;

public interface UserFacade extends PageableDataTableFacade<UserItem, MinimalUserData> {

	UserData getCurrentUser();

	UserData getUserByUUID(UUID uuid);

	UserData getUserByLogin(String login);

	UserData getUserByLoginOrEmail(String value);

	void saveUser(UserData userData);

}
