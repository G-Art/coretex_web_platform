package com.coretex.commerce.admin.facades.impl;

import com.coretex.commerce.admin.controllers.DataTableResults;
import com.coretex.commerce.admin.data.MinimalOrderData;
import com.coretex.commerce.admin.facades.OrderFacade;
import com.coretex.commerce.admin.mapper.GenericDataMapper;
import com.coretex.commerce.admin.mapper.MinimalOrderDataMapper;
import com.coretex.core.business.services.common.generic.PageableEntityService;
import com.coretex.core.business.services.order.OrderService;
import com.coretex.items.commerce_core_model.OrderItem;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.stream.Collectors;

@Component
public class DefaultOrderFacade implements OrderFacade {

	@Resource
	private OrderService orderService;

	@Resource
	private MinimalOrderDataMapper minimalOrderDataMapper;


	@Override
	public PageableEntityService<OrderItem> getPageableService() {
		return orderService;
	}

	@Override
	public GenericDataMapper<OrderItem, MinimalOrderData> getDataMapper() {
		return minimalOrderDataMapper;
	}
}
