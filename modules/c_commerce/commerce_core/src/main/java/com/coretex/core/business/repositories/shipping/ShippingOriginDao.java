package com.coretex.core.business.repositories.shipping;

import com.coretex.core.activeorm.dao.Dao;

import com.coretex.items.commerce_core_model.ShippingOriginItem;

import java.util.UUID;

public interface ShippingOriginDao extends Dao<ShippingOriginItem> {

	//	@Query("select s from ShippingOriginItem as s join fetch s.merchantStore sm where sm.id = ?1")
	ShippingOriginItem findByStore(UUID storeId);
}
