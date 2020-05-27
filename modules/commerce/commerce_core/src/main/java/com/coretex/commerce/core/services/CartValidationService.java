package com.coretex.commerce.core.services;

import com.coretex.commerce.core.dto.CartValidationResult;
import com.coretex.items.cx_core.CartItem;

public interface CartValidationService {
	CartValidationResult validate(CartItem cartItem);
}
