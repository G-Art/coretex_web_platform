package com.coretex.commerce.facades;

import com.coretex.commerce.data.CartData;
import com.coretex.items.cx_core.CartItem;

import java.util.UUID;

public interface CartFacade extends PageableDataTableFacade<CartItem, CartData> {

	CartData getByUUID(UUID uuid);

	CartData createCart();

	CartData updateCart(CartData cartData, UUID uuid, Integer quantity);

	CartData addToCart(CartData cartData, UUID product, Integer quantity);
}
