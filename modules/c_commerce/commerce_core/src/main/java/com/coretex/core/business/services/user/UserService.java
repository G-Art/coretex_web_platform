package com.coretex.core.business.services.user;

import java.util.List;
import java.util.UUID;

import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.business.services.common.generic.SalesManagerEntityService;
import com.coretex.core.model.common.Criteria;
import com.coretex.core.model.common.GenericEntityList;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.UserItem;


public interface UserService extends SalesManagerEntityService<UserItem> {

	UserItem getByUserName(String userName) throws ServiceException;

	List<UserItem> listUser() throws ServiceException;

	/**
	 * Create or update a UserItem
	 *
	 * @param user
	 * @throws ServiceException
	 */
	void saveOrUpdate(UserItem user) throws ServiceException;

	List<UserItem> listByStore(MerchantStoreItem store) throws ServiceException;

	UserItem findByStore(UUID userId, String storeCode) throws ServiceException;

	GenericEntityList<UserItem> listByCriteria(Criteria criteria) throws ServiceException;


}
