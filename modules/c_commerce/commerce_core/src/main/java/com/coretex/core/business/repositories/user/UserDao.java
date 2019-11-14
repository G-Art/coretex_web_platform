package com.coretex.core.business.repositories.user;

import java.util.List;
import java.util.UUID;

import com.coretex.items.commerce_core_model.UserItem;
import com.coretex.core.activeorm.dao.Dao;

public interface UserDao extends Dao<UserItem>, UserRepositoryCustom {

	//	@Query("select distinct u from UserItem as u left join fetch u.groups ug join fetch u.merchantStore um left join fetch u.defaultLanguage ul where u.adminName = ?1")
	UserItem findByUserName(String userName);

	//	@Query("select distinct u from UserItem as u left join fetch u.groups ug join fetch u.merchantStore um left join fetch u.defaultLanguage ul where u.id = ?1")
	UserItem findOne(UUID id);

	//	@Query("select distinct u from UserItem as u left join fetch u.groups ug join fetch u.merchantStore um left join fetch u.defaultLanguage ul order by u.id")
	List<UserItem> findAll();

	//	@Query("select distinct u from UserItem as u left join fetch u.groups ug join fetch u.merchantStore um left join fetch u.defaultLanguage ul where um.id = ?1 order by u.id")
	List<UserItem> findByStore(UUID storeId);

	//	@Query("select u from UserItem as u left join fetch u.groups ug join fetch u.merchantStore um left join fetch u.defaultLanguage ul where u.id= ?1 and um.code = ?2 order by u.id")
	UserItem findByUserAndStore(UUID userId, String storeCode);
}
