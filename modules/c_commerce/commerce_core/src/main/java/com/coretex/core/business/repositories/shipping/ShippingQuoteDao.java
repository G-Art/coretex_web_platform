package com.coretex.core.business.repositories.shipping;

import java.util.List;
import java.util.UUID;

import com.coretex.items.commerce_core_model.QuoteItem;
import com.coretex.core.activeorm.dao.Dao;

public interface ShippingQuoteDao extends Dao<QuoteItem> {


	//	@Query("select q from QuoteItem as q where q.orderId = ?1")
	List<QuoteItem> findByOrder(UUID order);

}
