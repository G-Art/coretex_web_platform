package com.coretex.core.business.services.user;

import java.util.List;
import java.util.UUID;


import com.coretex.core.business.services.common.generic.SalesManagerEntityService;
import com.coretex.core.model.common.Criteria;
import com.coretex.core.model.common.GenericEntityList;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.UserItem;


public interface UserService extends SalesManagerEntityService<UserItem> {

	UserItem getByUserName(String userName);

	List<UserItem> listUser() ;

	@Deprecated
	void saveOrUpdate(UserItem user);

	List<UserItem> listByStore(MerchantStoreItem store) ;

	UserItem findByStore(UUID userId, String storeCode) ;

	UserItem findByLoginCredentials(String loginOrEmail);
}
