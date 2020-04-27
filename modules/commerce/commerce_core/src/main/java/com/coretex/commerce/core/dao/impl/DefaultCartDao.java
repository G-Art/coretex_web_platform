package com.coretex.commerce.core.dao.impl;

import com.coretex.commerce.core.dao.CartDao;
import com.coretex.core.activeorm.dao.DefaultGenericDao;
import com.coretex.items.cx_core.CartItem;
import com.coretex.items.cx_core.CustomerItem;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Stream;

@Component
public class DefaultCartDao extends DefaultGenericDao<CartItem> implements CartDao {
	public DefaultCartDao() {
		super(CartItem.ITEM_TYPE);
	}

	@Override
	public Stream<CartItem> getCartsForCustomer(CustomerItem customerItem){
		return findReactive(Map.of(CartItem.CUSTOMER, customerItem));
	}
}
