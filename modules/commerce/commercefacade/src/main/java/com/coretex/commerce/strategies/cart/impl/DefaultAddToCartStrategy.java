package com.coretex.commerce.strategies.cart.impl;

import com.coretex.commerce.core.services.CartCalculationService;
import com.coretex.commerce.core.services.OrderEntryService;
import com.coretex.commerce.strategies.cart.AddToCartStrategy;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Resource;

@Component
public class DefaultAddToCartStrategy implements AddToCartStrategy {

	@Resource
	private OrderEntryService orderEntryService;

	@Resource
	private CartCalculationService cartCalculationService;

	@Override
	public void addToCart(CartParameter cartParameter) {
		Assert.notNull(cartParameter.getCart(), "Cart must not be null");
		Assert.notNull(cartParameter.getProduct(), "Product must not be null");

		var cart = cartParameter.getCart();
		var product = cartParameter.getProduct();

		if (cartParameter.getQuantity()>0){
			if (cart.getEntries().isEmpty()) {
				var orderEntryItem = orderEntryService.newOrderEntry(cart, product, cartParameter.getQuantity());
				orderEntryItem.setCalculated(false);
				cart.getEntries().add(orderEntryItem);
			} else {
				var orderEntryItem = orderEntryService.findOrCreateOrderEntry(cart, product);
				orderEntryItem.setQuantity(orderEntryItem.getQuantity() + cartParameter.getQuantity());
				orderEntryItem.setCalculated(false);
				cart.getEntries().add(orderEntryItem);
			}
		} else {
			orderEntryService.removeEntity(cart, product);
		}

		cartCalculationService.calculate(cart);

	}
}
