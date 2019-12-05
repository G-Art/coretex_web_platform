package com.coretex.commerce.admin.facades;

import com.coretex.commerce.admin.data.MinimalOrderData;
import com.coretex.commerce.admin.data.OrderData;
import com.coretex.items.commerce_core_model.OrderItem;

import java.util.UUID;

public interface OrderFacade extends PageableDataTableFacade<OrderItem, MinimalOrderData> {

	OrderData getOrderByUUID(UUID uuid);
}
