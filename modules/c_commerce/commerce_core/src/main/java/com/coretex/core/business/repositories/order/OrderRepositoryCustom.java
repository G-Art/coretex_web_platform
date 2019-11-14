package com.coretex.core.business.repositories.order;

import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.core.model.order.OrderCriteria;
import com.coretex.core.model.order.OrderList;


public interface OrderRepositoryCustom {

	OrderList listByStore(MerchantStoreItem store, OrderCriteria criteria);

	OrderList getOrders(OrderCriteria criteria);
}
