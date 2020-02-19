package com.coretex.commerce.core.dao.impl;

import com.coretex.commerce.core.dao.OrderDao;
import com.coretex.core.activeorm.dao.DefaultGenericDao;
import com.coretex.items.cx_core.OrderItem;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Component
public class DefaultOrderDao extends DefaultGenericDao<OrderItem> implements OrderDao {
	public DefaultOrderDao() {
		super(OrderItem.ITEM_TYPE);
	}

	@Override
	public Map getOrderStatistic() {
		String query = "SELECT count(o.uuid), sum(o.total) FROM \"" + OrderItem.ITEM_TYPE + "\" AS o ";
		var result = getSearchService().<Map>search(query);
		return result.getResult().iterator().next();
	}

	@Override
	public Map getOrderStatistic(Date from) {
		String query = "SELECT count(o.uuid), sum(o.total) FROM \"" + OrderItem.ITEM_TYPE + "\" AS o " +
				"WHERE o." + OrderItem.CREATE_DATE + ">= :from";
		var result = getSearchService().<Map>search(query, Map.of("from", from));
		return result.getResult().iterator().next();
	}
}
