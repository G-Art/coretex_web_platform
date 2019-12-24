package com.coretex.commerce.facades;

import com.coretex.commerce.data.minimal.MinimalOrderData;
import com.coretex.commerce.data.OrderData;
import com.coretex.items.commerce_core_model.OrderItem;

import java.util.UUID;

public interface OrderFacade extends PageableDataTableFacade<OrderItem, MinimalOrderData> {

	OrderData getOrderByUUID(UUID uuid);
}
