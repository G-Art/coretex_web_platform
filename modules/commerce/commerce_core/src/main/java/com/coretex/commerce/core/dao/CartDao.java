package com.coretex.commerce.core.dao;

import com.coretex.core.activeorm.dao.Dao;
import com.coretex.items.cx_core.CartItem;
import com.coretex.items.cx_core.CustomerItem;
import reactor.core.publisher.Flux;

public interface CartDao extends Dao<CartItem> {


	Flux<CartItem> getCartsForCustomer(CustomerItem customerItem);
}
