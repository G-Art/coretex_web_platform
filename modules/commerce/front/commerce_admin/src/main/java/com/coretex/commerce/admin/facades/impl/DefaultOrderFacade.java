package com.coretex.commerce.admin.facades.impl;

import com.coretex.commerce.admin.controllers.DataTableResults;
import com.coretex.commerce.admin.data.MinimalOrderData;
import com.coretex.commerce.admin.facades.OrderFacade;
import com.coretex.commerce.admin.mapper.MinimalOrderDataMapper;
import com.coretex.core.business.services.order.OrderService;
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
	public DataTableResults<MinimalOrderData> tableResult(String draw, long page, Long length) {
		var pageableList = orderService.pageableList(length, page);
		DataTableResults dataTableResults = new DataTableResults();
		dataTableResults.setDraw(draw);
		dataTableResults.setRecordsTotal(String.valueOf(pageableList.getTotalCount()));
		dataTableResults.setRecordsFiltered(String.valueOf(pageableList.getCount()));
		dataTableResults.setListOfDataObjects(pageableList.getResult()
				.stream()
				.map(minimalOrderDataMapper::fromItem)
				.collect(Collectors.toList()));
		return dataTableResults;
	}
}
