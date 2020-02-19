package com.coretex.commerce.core.dao;

import com.coretex.core.activeorm.dao.Dao;
import com.coretex.items.cx_core.UserItem;

public interface UserDao extends Dao<UserItem> {
	UserItem findByCredentials(String loginOrEmail);
}
