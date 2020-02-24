package com.coretex.commerce.core.services.impl;

import com.coretex.commerce.core.dao.CartDao;
import com.coretex.commerce.core.services.AbstractGenericItemService;
import com.coretex.commerce.core.services.CartService;
import com.coretex.items.cx_core.CartItem;
import org.springframework.stereotype.Service;

@Service
public class DefaultCartService extends AbstractGenericItemService<CartItem> implements CartService {

	private CartDao repository;

	public DefaultCartService(CartDao repository) {
		super(repository);
		this.repository = repository;
	}

}
