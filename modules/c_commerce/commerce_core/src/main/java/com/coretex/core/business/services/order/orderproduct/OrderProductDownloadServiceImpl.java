package com.coretex.core.business.services.order.orderproduct;

import java.util.List;
import java.util.UUID;

import com.coretex.items.commerce_core_model.OrderProductDownloadItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.coretex.core.business.repositories.order.orderproduct.OrderProductDownloadDao;
import com.coretex.core.business.services.common.generic.SalesManagerEntityServiceImpl;


@Service("orderProductDownloadService")
public class OrderProductDownloadServiceImpl extends SalesManagerEntityServiceImpl<OrderProductDownloadItem> implements OrderProductDownloadService {

	private static final Logger LOGGER = LoggerFactory.getLogger(OrderProductDownloadServiceImpl.class);


	private final OrderProductDownloadDao orderProductDownloadDao;

	public OrderProductDownloadServiceImpl(OrderProductDownloadDao orderProductDownloadDao) {
		super(orderProductDownloadDao);
		this.orderProductDownloadDao = orderProductDownloadDao;
	}

	@Override
	public List<OrderProductDownloadItem> getByOrderId(UUID orderId) {
		return orderProductDownloadDao.findByOrderId(orderId);
	}


}
