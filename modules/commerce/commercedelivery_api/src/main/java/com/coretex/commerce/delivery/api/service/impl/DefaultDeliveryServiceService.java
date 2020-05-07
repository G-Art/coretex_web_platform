package com.coretex.commerce.delivery.api.service.impl;

import com.coretex.commerce.core.services.AbstractGenericItemService;
import com.coretex.commerce.delivery.api.actions.DeliveryTypeActionHandler;
import com.coretex.commerce.delivery.api.dao.DeliveryServiceDao;
import com.coretex.commerce.delivery.api.service.DeliveryServiceService;
import com.coretex.items.cx_commercedelivery_api.DeliveryServiceItem;
import com.coretex.items.cx_commercedelivery_api.DeliveryTypeItem;
import com.coretex.items.cx_core.CartItem;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.UUID;

@Component
public class DefaultDeliveryServiceService extends AbstractGenericItemService<DeliveryServiceItem> implements DeliveryServiceService {

	@Resource
	private DeliveryTypeActionHandler deliveryTypeActionHandler;

	private DeliveryServiceDao repository;

	public DefaultDeliveryServiceService(DeliveryServiceDao repository) {
		super(repository);
		this.repository = repository;
	}

	@Override
	public <T extends DeliveryServiceItem> T getByUUIDAndType(UUID uuid, Class<T> type) {
		return repository.getByUUIDAndType(uuid, type);
	}

	@Override
	public <T extends DeliveryTypeItem> T getDeliveryTypeByUUID(UUID uuid) {
		return repository.getDeliveryTypeByUUID(uuid);
	}

	@Override
	public void saveDeliveryInfo(CartItem cartItem, Map<String, Object> info) {
		deliveryTypeActionHandler.addDeliveryInfo(cartItem, info);
	}

	@Override
	public <T extends DeliveryServiceItem> T getByCode(String code) {
		return repository.getByCode(code);
	}
}
