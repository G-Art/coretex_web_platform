package com.coretex.commerce.core.services.impl;

import com.coretex.commerce.core.dao.OrderDao;
import com.coretex.commerce.core.services.AbstractGenericItemService;
import com.coretex.commerce.core.services.OrderService;
import com.coretex.items.cx_core.OrderItem;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

@Service
public class DefaultOrderService extends AbstractGenericItemService<OrderItem> implements OrderService {

	private OrderDao repository;

	public DefaultOrderService(OrderDao repository) {
		super(repository);
		this.repository = repository;
	}

	@Override
	public Map getStatisticForPeriod(Date from) {
		if (Objects.isNull(from)) {
			return repository.getOrderStatistic();
		}
		return repository.getOrderStatistic(from);
	}
}
