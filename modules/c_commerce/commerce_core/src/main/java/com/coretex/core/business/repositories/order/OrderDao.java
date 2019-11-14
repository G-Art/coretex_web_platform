package com.coretex.core.business.repositories.order;

import com.coretex.core.activeorm.dao.Dao;
import com.coretex.items.commerce_core_model.OrderItem;

import java.util.UUID;

public interface OrderDao extends Dao<OrderItem>, OrderRepositoryCustom {

	//    @Query("select o from OrderItem o join fetch o.orderProducts op left join fetch o.orderAttributes oa join fetch o.orderTotal ot left join fetch o.orderHistory oh left join fetch op.downloads opd left join fetch op.orderAttributes opa left join fetch op.prices opp where o.id = ?1")
	OrderItem findOne(UUID id);
}
