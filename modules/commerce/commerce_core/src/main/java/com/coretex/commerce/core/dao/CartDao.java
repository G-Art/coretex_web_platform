package com.coretex.commerce.core.dao;

import com.coretex.core.activeorm.dao.Dao;
import com.coretex.items.cx_core.CartItem;
import com.coretex.items.cx_core.CustomerItem;

import java.util.stream.Stream;

public interface CartDao extends Dao<CartItem> {


	Stream<CartItem> getCartsForCustomer(CustomerItem customerItem);
}
