package com.coretex.commerce.core.dao.impl;

import com.coretex.commerce.core.dao.OrderEntryDao;
import com.coretex.core.activeorm.dao.DefaultGenericDao;
import com.coretex.items.cx_core.OrderEntryItem;
import org.springframework.stereotype.Component;

@Component
public class DefaultOrderEntryDao extends DefaultGenericDao<OrderEntryItem> implements OrderEntryDao {
	public DefaultOrderEntryDao() {
		super(OrderEntryItem.ITEM_TYPE);
	}

}
