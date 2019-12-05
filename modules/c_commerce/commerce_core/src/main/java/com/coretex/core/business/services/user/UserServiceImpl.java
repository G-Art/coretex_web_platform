package com.coretex.core.business.services.user;

import java.util.List;
import java.util.UUID;


import com.coretex.core.business.repositories.user.UserDao;
import com.coretex.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.coretex.core.model.common.Criteria;
import com.coretex.core.model.common.GenericEntityList;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.UserItem;


public class UserServiceImpl extends SalesManagerEntityServiceImpl<UserItem>
		implements UserService {


	private UserDao userDao;

	public UserServiceImpl(UserDao userDao) {
		super(userDao);
		this.userDao = userDao;
	}


	@Override
	public UserItem getByUserName(String userName) {
		return userDao.findByUserName(userName);
	}

	@Override
	public List<UserItem> listUser()  {
		try {
			return userDao.findAll();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<UserItem> listByStore(MerchantStoreItem store)  {
		try {
			return userDao.findByStore(store.getUuid());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}


	@Override
	@Deprecated
	public void saveOrUpdate(UserItem user) {
		userDao.save(user);
	}


	@Override
	public UserItem findByStore(UUID userId, String storeCode) {
		return userDao.findByUserAndStore(userId, storeCode);
	}

	@Override
	public UserItem findByLoginCredentials(String loginOrEmail) {
		return userDao.findByCredentials(loginOrEmail);
	}


}
