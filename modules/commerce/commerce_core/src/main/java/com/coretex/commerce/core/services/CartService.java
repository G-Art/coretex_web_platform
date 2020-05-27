package com.coretex.commerce.core.services;

import com.coretex.items.cx_core.CartItem;
import com.coretex.items.cx_core.CustomerItem;

import java.util.stream.Stream;

public interface CartService extends GenericItemService<CartItem> {
	Stream<CartItem> getCartsForCustomer(CustomerItem customerItem);

	void merge(CartItem mainCart, CartItem slaveCart);

	void placeOrder(CartItem cartItem);
}
