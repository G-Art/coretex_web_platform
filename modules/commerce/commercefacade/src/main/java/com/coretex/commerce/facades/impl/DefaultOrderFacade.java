package com.coretex.commerce.facades.impl;

import com.coretex.commerce.core.services.OrderService;
import com.coretex.commerce.data.minimal.MinimalOrderData;
import com.coretex.commerce.data.OrderData;
import com.coretex.commerce.facades.OrderFacade;
import com.coretex.commerce.mapper.GenericDataMapper;
import com.coretex.commerce.mapper.minimal.MinimalOrderDataMapper;
import com.coretex.commerce.mapper.OrderDataMapper;
import com.coretex.commerce.core.services.PageableService;
import com.coretex.items.cx_core.OrderItem;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.UUID;

@Component
public class DefaultOrderFacade implements OrderFacade {

	@Resource
	private OrderService orderService;

	@Resource
	private MinimalOrderDataMapper minimalOrderDataMapper;

	@Resource
	private OrderDataMapper orderDataMapper;

	@Override
	public PageableService<OrderItem> getPageableService() {
		return orderService;
	}

	@Override
	public GenericDataMapper<OrderItem, MinimalOrderData> getDataMapper() {
		return minimalOrderDataMapper;
	}

	@Override
	public OrderData getOrderByUUID(UUID uuid) {
		return orderDataMapper.fromItem(orderService.getByUUID(uuid));
	}
}
