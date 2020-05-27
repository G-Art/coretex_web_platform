package com.coretex.commerce.core.services.impl;

import com.coretex.commerce.core.dto.CartValidationResult;
import com.coretex.commerce.core.services.CartValidationService;
import com.coretex.items.cx_core.CartItem;
import org.springframework.stereotype.Component;

@Component
public class DefaultCartValidationService implements CartValidationService {

	@Override
	public CartValidationResult validate(CartItem cartItem){
		return new CartValidationResult();
	}
}
