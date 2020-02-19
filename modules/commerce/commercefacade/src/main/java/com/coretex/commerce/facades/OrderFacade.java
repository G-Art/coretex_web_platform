package com.coretex.commerce.facades;

import com.coretex.commerce.data.OrderData;
import com.coretex.commerce.data.minimal.MinimalOrderData;
import com.coretex.items.cx_core.OrderItem;

import java.util.UUID;

public interface OrderFacade extends PageableDataTableFacade<OrderItem, MinimalOrderData> {

	OrderData getOrderByUUID(UUID uuid);
}
