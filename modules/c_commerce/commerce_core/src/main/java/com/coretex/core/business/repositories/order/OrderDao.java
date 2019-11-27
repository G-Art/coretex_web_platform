package com.coretex.core.business.repositories.order;

import com.coretex.core.activeorm.dao.Dao;
import com.coretex.items.commerce_core_model.OrderItem;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface OrderDao extends Dao<OrderItem>, OrderRepositoryCustom {

	List<OrderItem> findByPeriod(Date from, Date to);

	List<OrderItem> findByPeriod(Date from);

	Map getOrderStatistic();
	Map getOrderStatistic(Date from);
}
