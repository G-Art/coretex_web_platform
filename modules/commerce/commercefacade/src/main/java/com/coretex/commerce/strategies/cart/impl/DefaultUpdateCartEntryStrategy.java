package com.coretex.commerce.strategies.cart.impl;

import com.coretex.commerce.core.services.CartCalculationService;
import com.coretex.commerce.core.services.OrderEntryService;
import com.coretex.commerce.strategies.cart.UpdateCartEntryStrategy;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Resource;

@Component
public class DefaultUpdateCartEntryStrategy implements UpdateCartEntryStrategy {

	@Resource
	private OrderEntryService orderEntryService;

	@Resource
	private CartCalculationService cartCalculationService;

	@Override
	public void updateCart(CartParameter cartParameter) {
		Assert.notNull(cartParameter.getCart(), "Cart must not be null");
		Assert.notNull(cartParameter.getEntryItem(), "Order entry must not be null");

		var cart = cartParameter.getCart();
		var entry = cartParameter.getEntryItem();

		if (cartParameter.getQuantity()>0){
				entry.setQuantity(cartParameter.getQuantity());
				entry.setCalculated(false);
		} else {
			orderEntryService.removeEntity(cart, entry);
		}

		cartCalculationService.calculate(cart);

	}
}
