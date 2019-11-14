package com.coretex.core.model.order;

import java.util.List;

import com.coretex.core.model.common.EntityList;
import com.coretex.items.commerce_core_model.OrderItem;

public class OrderList extends EntityList {


	private static final long serialVersionUID = -6645927228659963628L;
	private List<OrderItem> orders;

	public void setOrders(List<OrderItem> orders) {
		this.orders = orders;
	}

	public List<OrderItem> getOrders() {
		return orders;
	}

}
