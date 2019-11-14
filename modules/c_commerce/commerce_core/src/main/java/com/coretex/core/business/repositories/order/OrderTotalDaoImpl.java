package com.coretex.core.business.repositories.order;

import com.coretex.core.activeorm.dao.DefaultGenericDao;
import com.coretex.items.commerce_core_model.OrderTotalItem;
import org.springframework.stereotype.Component;

@Component
public class OrderTotalDaoImpl extends DefaultGenericDao<OrderTotalItem> implements OrderTotalDao{

	public OrderTotalDaoImpl() {
		super(OrderTotalItem.ITEM_TYPE);
	}
}
