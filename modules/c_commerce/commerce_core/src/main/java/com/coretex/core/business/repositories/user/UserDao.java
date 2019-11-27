package com.coretex.core.business.repositories.user;

import java.util.List;
import java.util.UUID;

import com.coretex.items.commerce_core_model.UserItem;
import com.coretex.core.activeorm.dao.Dao;

public interface UserDao extends Dao<UserItem>{

	UserItem findByUserName(String userName);

	UserItem findByCredentials(String loginOrEmail);

	UserItem findOne(UUID id);

	List<UserItem> findAll();

	List<UserItem> findByStore(UUID storeId);

	UserItem findByUserAndStore(UUID userId, String storeCode);

}
