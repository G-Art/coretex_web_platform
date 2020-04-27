package com.coretex.commerce.delivery.api.actions;

import com.coretex.items.cx_core.CartItem;

import java.util.Map;

public interface AddDeliveryInfoAction extends TypeAction {
	void execute(CartItem cartItem, Map<String, Object> info);
}
