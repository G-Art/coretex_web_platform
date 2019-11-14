package com.coretex.core.business.repositories.payments;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.coretex.core.activeorm.dao.Dao;
import com.coretex.items.commerce_core_model.TransactionItem;

public interface TransactionDao extends Dao<TransactionItem> {

	//	@Query("select t from TransactionItem t join fetch t.order to where to.id = ?1")
	List<TransactionItem> findByOrder(UUID orderId);

	//	@Query("select t from TransactionItem t join fetch t.order to left join fetch to.orderAttributes toa left join fetch to.orderProducts too left join fetch to.orderTotal toot left join fetch to.orderHistory tood where to is not null and t.transactionDate BETWEEN :from AND :to")
	List<TransactionItem> findByDates(
			Date startDate,
			Date endDate);
}
