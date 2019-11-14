package com.coretex.core.business.services.user;

import java.util.List;
import java.util.UUID;

import com.coretex.core.business.exception.ServiceException;
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
	public UserItem getByUserName(String userName) throws ServiceException {
		return userDao.findByUserName(userName);
	}

	@Override
	public List<UserItem> listUser() throws ServiceException {
		try {
			return userDao.findAll();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<UserItem> listByStore(MerchantStoreItem store) throws ServiceException {
		try {
			return userDao.findByStore(store.getUuid());
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}


	@Override
	public void saveOrUpdate(UserItem user) throws ServiceException {
		userDao.save(user);
	}


	@Override
	public UserItem findByStore(UUID userId, String storeCode) {
		return userDao.findByUserAndStore(userId, storeCode);
	}


	@Override
	public GenericEntityList<UserItem> listByCriteria(Criteria criteria) throws ServiceException {
		return userDao.listByCriteria(criteria);
	}

}
