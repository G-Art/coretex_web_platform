package com.coretex.commerce.core.dao;

import com.coretex.core.activeorm.dao.Dao;
import com.coretex.items.cx_core.OrderItem;

import java.util.Date;
import java.util.Map;

public interface OrderDao extends Dao<OrderItem> {

	Map getOrderStatistic();
	Map getOrderStatistic(Date from);
}
