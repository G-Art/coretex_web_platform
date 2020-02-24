package com.coretex.commerce.core.dao.impl;

import com.coretex.commerce.core.dao.CartDao;
import com.coretex.core.activeorm.dao.DefaultGenericDao;
import com.coretex.items.cx_core.CartItem;
import org.springframework.stereotype.Component;

@Component
public class DefaultCartDao extends DefaultGenericDao<CartItem> implements CartDao {
	public DefaultCartDao() {
		super(CartItem.ITEM_TYPE);
	}

}
