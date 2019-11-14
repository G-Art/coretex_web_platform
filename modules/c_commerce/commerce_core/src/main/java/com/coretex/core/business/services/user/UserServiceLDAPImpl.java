package com.coretex.core.business.services.user;

import java.util.List;
import java.util.UUID;

import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.model.common.Criteria;
import com.coretex.core.model.common.GenericEntityList;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.UserItem;


public class UserServiceLDAPImpl implements UserService {

	@Override
	public void save(UserItem entity) {
		throw new UnsupportedOperationException("Not implemented");

	}

	@Override
	public void update(UserItem entity) {
		throw new UnsupportedOperationException("Not implemented");

	}

	@Override
	public void create(UserItem entity) {
		throw new UnsupportedOperationException("Not implemented");

	}

	@Override
	public void delete(UserItem entity) {
		throw new UnsupportedOperationException("Not implemented");

	}


	@Override
	public UserItem getById(UUID id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserItem> list() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Long count() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public UserItem getByUserName(String userName) throws ServiceException {
		// TODO Auto-generated method stub
		throw new ServiceException("Not implemented");
	}

	@Override
	public List<UserItem> listUser() throws ServiceException {
		// TODO Auto-generated method stub
		throw new ServiceException("Not implemented");
	}

	@Override
	public void saveOrUpdate(UserItem user) throws ServiceException {
		throw new ServiceException("Not implemented");

	}

	@Override
	public List<UserItem> listByStore(MerchantStoreItem store) throws ServiceException {
		// TODO Auto-generated method stub
		throw new ServiceException("Not implemented");
	}


	@Override
	public UserItem findByStore(UUID userId, String storeCode) throws ServiceException {
		// TODO Auto-generated method stub
		throw new ServiceException("Not implemented");
	}

	@Override
	public GenericEntityList<UserItem> listByCriteria(Criteria criteria) throws ServiceException {
		// TODO Auto-generated method stub
		throw new ServiceException("Not implemented");
	}


}
