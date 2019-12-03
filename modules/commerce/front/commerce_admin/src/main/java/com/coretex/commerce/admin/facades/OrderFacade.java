package com.coretex.commerce.admin.facades;

import com.coretex.commerce.admin.data.MinimalOrderData;
import com.coretex.items.commerce_core_model.OrderItem;

public interface OrderFacade extends PageableDataTableFacade<OrderItem, MinimalOrderData> {
}
