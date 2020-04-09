package com.coretex.commerce.delivery.api.service.impl;

import com.coretex.commerce.core.services.AbstractGenericItemService;
import com.coretex.commerce.delivery.api.dao.DeliveryServiceDao;
import com.coretex.commerce.delivery.api.service.DeliveryServiceService;
import com.coretex.items.cx_commercedelivery_api.DeliveryServiceItem;
import org.springframework.stereotype.Component;

@Component
public class DefaultDeliveryServiceService extends AbstractGenericItemService<DeliveryServiceItem> implements DeliveryServiceService {

	private DeliveryServiceDao repository;

	public DefaultDeliveryServiceService(DeliveryServiceDao repository) {
		super(repository);
		this.repository = repository;
	}



}
