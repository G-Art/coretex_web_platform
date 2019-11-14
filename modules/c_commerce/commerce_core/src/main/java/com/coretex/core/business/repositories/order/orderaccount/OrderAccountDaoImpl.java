package com.coretex.core.business.repositories.order.orderaccount;

import com.coretex.core.activeorm.dao.DefaultGenericDao;
import com.coretex.items.commerce_core_model.OrderAccountItem;
import org.springframework.stereotype.Component;

@Component
public class OrderAccountDaoImpl extends DefaultGenericDao<OrderAccountItem> implements OrderAccountDao {
	public OrderAccountDaoImpl() {
		super(OrderAccountItem.ITEM_TYPE);
	}
}
