package com.coretex.core.business.repositories.order.orderproduct;

import com.coretex.core.activeorm.dao.Dao;
import com.coretex.core.activeorm.dao.DefaultGenericDao;
import com.coretex.items.commerce_core_model.OrderProductDownloadItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class OrderProductDownloadDaoImpl extends DefaultGenericDao<OrderProductDownloadItem> implements OrderProductDownloadDao{


	public OrderProductDownloadDaoImpl() {
		super(OrderProductDownloadItem.ITEM_TYPE);
	}

	@Override
	public OrderProductDownloadItem findOne(UUID id) {
		return find(id);
	}

	@Override
	public List<OrderProductDownloadItem> findByOrderId(UUID id) {
		return null;
	}
}
