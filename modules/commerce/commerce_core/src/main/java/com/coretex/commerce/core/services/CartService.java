package com.coretex.commerce.core.services;

import com.coretex.items.cx_core.CartItem;
import com.coretex.items.cx_core.CustomerItem;
import reactor.core.publisher.Flux;

public interface CartService extends GenericItemService<CartItem> {
	Flux<CartItem> getCartsForCustomer(CustomerItem customerItem);

	void merge(CartItem mainCart, CartItem slaveCart);

	void placeOrder(CartItem cartItem);
}
