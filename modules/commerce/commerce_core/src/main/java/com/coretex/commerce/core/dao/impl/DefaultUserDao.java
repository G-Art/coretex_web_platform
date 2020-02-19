package com.coretex.commerce.core.dao.impl;

import com.coretex.commerce.core.dao.UserDao;
import com.coretex.core.activeorm.dao.DefaultGenericDao;
import com.coretex.items.cx_core.UserItem;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DefaultUserDao extends DefaultGenericDao<UserItem> implements UserDao {
	public DefaultUserDao() {
		super(UserItem.ITEM_TYPE);
	}

	@Override
	public UserItem findByCredentials(String loginOrEmail) {
		String query = "SELECT u.* FROM " + UserItem.ITEM_TYPE +" AS u " +
				"WHERE u."+UserItem.LOGIN+" = :login OR u."+UserItem.EMAIL+" = :email";
		var result = getSearchService().<UserItem>search(query, Map.of("login", loginOrEmail, "email", loginOrEmail)).getResult();
		if(result.isEmpty()){
			return null;
		}
		return result.iterator().next();
	}
}
