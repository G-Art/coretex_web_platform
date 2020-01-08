package com.coretex.core.business.repositories.user;

import com.coretex.core.activeorm.dao.Dao;
import com.coretex.items.cx_core.UserItem;

import java.util.List;
import java.util.UUID;

public interface UserDao extends Dao<UserItem>{

	UserItem findByUserName(String userName);

	UserItem findByCredentials(String loginOrEmail);

	UserItem findOne(UUID id);

	List<UserItem> findAll();

	List<UserItem> findByStore(UUID storeId);

	UserItem findByUserAndStore(UUID userId, String storeCode);

}
