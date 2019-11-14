package com.coretex.core.business.repositories.order.orderproduct;

import com.coretex.core.activeorm.dao.DefaultGenericDao;
import com.coretex.items.commerce_core_model.OrderProductItem;
import org.springframework.stereotype.Component;

@Component
public class OrderProductDaoImpl extends DefaultGenericDao<OrderProductItem> implements OrderProductDao {

	public OrderProductDaoImpl() {
		super(OrderProductItem.ITEM_TYPE);
	}
}
