package com.coretex.core.business.repositories.order.orderproduct;

import java.util.List;
import java.util.UUID;

import com.coretex.items.commerce_core_model.OrderProductDownloadItem;
import com.coretex.core.activeorm.dao.Dao;

public interface OrderProductDownloadDao extends Dao<OrderProductDownloadItem> {

	//	@Query("select o from OrderProductDownloadItem o left join fetch o.orderProduct op join fetch op.order opo join fetch opo.merchant opon where o.id = ?1")
	OrderProductDownloadItem findOne(UUID id);

	//	@Query("select o from OrderProductDownloadItem o left join fetch o.orderProduct op join fetch op.order opo join fetch opo.merchant opon where opo.id = ?1")
	List<OrderProductDownloadItem> findByOrderId(UUID id);

}
