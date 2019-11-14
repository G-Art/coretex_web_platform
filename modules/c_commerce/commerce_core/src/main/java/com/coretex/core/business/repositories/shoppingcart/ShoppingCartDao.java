package com.coretex.core.business.repositories.shoppingcart;

import com.coretex.items.commerce_core_model.ShoppingCartItem;
import com.coretex.core.activeorm.dao.Dao;

import java.util.UUID;

public interface ShoppingCartDao extends Dao<ShoppingCartItem> {

	//	@Query("select c from ShoppingCartItem c left join fetch c.lineItems cl left join fetch cl.attributes cla join fetch c.merchantStore cm where c.id = ?1")
	ShoppingCartItem findOne(UUID id);

	//	@Query("select c from ShoppingCartItem c left join fetch c.lineItems cl left join fetch cl.attributes cla join fetch c.merchantStore cm where c.shoppingCartCode = ?1")
	ShoppingCartItem findByCode(String code);

	//	@Query("select c from ShoppingCartItem c left join fetch c.lineItems cl left join fetch cl.attributes cla join fetch c.merchantStore cm where cm.id = ?1 and c.id = ?2")
	ShoppingCartItem findById(UUID merchantId, UUID id);

	//	@Query("select c from ShoppingCartItem c left join fetch c.lineItems cl left join fetch cl.attributes cla join fetch c.merchantStore cm where cm.id = ?1 and c.shoppingCartCode = ?2")
	ShoppingCartItem findByCode(UUID merchantId, String code);

	//	@Query("select c from ShoppingCartItem c left join fetch c.lineItems cl left join fetch cl.attributes cla join fetch c.merchantStore cm where c.customerId = ?1")
	ShoppingCartItem findByCustomer(UUID customerId);

}
