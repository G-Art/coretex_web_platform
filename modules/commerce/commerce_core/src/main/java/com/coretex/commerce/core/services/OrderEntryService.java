package com.coretex.commerce.core.services;

import com.coretex.items.cx_core.AbstractOrderItem;
import com.coretex.items.cx_core.CartItem;
import com.coretex.items.cx_core.OrderEntryItem;
import com.coretex.items.cx_core.ProductItem;

public interface OrderEntryService extends GenericItemService<OrderEntryItem> {
	OrderEntryItem newOrderEntry(AbstractOrderItem abstractOrder);

	OrderEntryItem newOrderEntry(AbstractOrderItem abstractOrder, ProductItem product);

	OrderEntryItem newOrderEntry(AbstractOrderItem abstractOrder, ProductItem product, int quantity);

	OrderEntryItem findOrCreateOrderEntry(CartItem cart, ProductItem product);

	void removeEntity(CartItem cart, ProductItem product);
}
