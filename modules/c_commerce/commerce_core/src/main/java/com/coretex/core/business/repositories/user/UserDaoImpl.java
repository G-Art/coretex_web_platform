package com.coretex.core.business.repositories.user;

import com.coretex.core.activeorm.dao.DefaultGenericDao;
import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.model.common.Criteria;
import com.coretex.core.model.common.GenericEntityList;
import com.coretex.items.commerce_core_model.UserItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class UserDaoImpl extends DefaultGenericDao<UserItem> implements UserDao {


	public UserDaoImpl() {
		super(UserItem.ITEM_TYPE);
	}

	@Override
	public UserItem findByUserName(String userName) {
		return findSingle(Map.of(UserItem.ADMIN_NAME, userName), true);
	}

	@Override
	public UserItem findOne(UUID id) {
		return find(id);
	}

	@Override
	public List<UserItem> findAll() {
		return find();
	}

	@Override
	public List<UserItem> findByStore(UUID storeId) {
		return find(Map.of(UserItem.MERCHANT_STORE, storeId));
	}

	@Override
	public UserItem findByUserAndStore(UUID userId, String storeCode) {
		return null;
	}

	@Override
	public GenericEntityList<UserItem> listByCriteria(Criteria criteria) throws ServiceException {
		return null;
	}
}
