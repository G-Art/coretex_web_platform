package com.coretex.commerce.core.services;

import com.coretex.items.cx_core.OrderItem;

import java.util.Date;
import java.util.Map;

public interface OrderService extends GenericItemService<OrderItem> {
	Map getStatisticForPeriod(Date from);
}
