package com.coretex.core.business.repositories.shoppingcart;

import com.coretex.core.activeorm.dao.DefaultGenericDao;
import com.coretex.items.commerce_core_model.ShoppingCartItem;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
public class ShoppingCartDaoImpl extends DefaultGenericDao<ShoppingCartItem> implements ShoppingCartDao{

	public ShoppingCartDaoImpl() {
		super(ShoppingCartItem.ITEM_TYPE);
	}

	@Override
	public ShoppingCartItem findOne(UUID id) {
		return find(id);
	}

	@Override
	public ShoppingCartItem findByCode(String code) {
		return findSingle(Map.of(ShoppingCartItem.SHOPPING_CART_CODE, code), true);
	}

	@Override
	public ShoppingCartItem findById(UUID merchantId, UUID id) {
		return findSingle(Map.of(ShoppingCartItem.MERCHANT_STORE, merchantId, ShoppingCartItem.UUID, id), true);
	}

	@Override
	public ShoppingCartItem findByCode(UUID merchantId, String code) {
		return findSingle(Map.of(ShoppingCartItem.MERCHANT_STORE, merchantId, ShoppingCartItem.SHOPPING_CART_CODE, code), true);
	}

	@Override
	public ShoppingCartItem findByCustomer(UUID customerId) {
		return findSingle(Map.of(ShoppingCartItem.CUSTOMER_ID, customerId), true);
	}
}
