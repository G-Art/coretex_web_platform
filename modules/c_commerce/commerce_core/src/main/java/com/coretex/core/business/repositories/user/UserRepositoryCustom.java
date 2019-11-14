package com.coretex.core.business.repositories.user;

import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.model.common.Criteria;
import com.coretex.core.model.common.GenericEntityList;
import com.coretex.items.commerce_core_model.UserItem;

public interface UserRepositoryCustom {

	GenericEntityList<UserItem> listByCriteria(Criteria criteria) throws ServiceException;

}
