package com.coretex.commerce.delivery.api.service;

import com.coretex.commerce.core.services.GenericItemService;
import com.coretex.items.cx_commercedelivery_api.DeliveryServiceItem;
import com.coretex.items.cx_commercedelivery_api.DeliveryTypeItem;
import com.coretex.items.cx_core.CartItem;

import java.util.Map;
import java.util.UUID;

public interface DeliveryServiceService extends GenericItemService<DeliveryServiceItem> {


	<T extends DeliveryServiceItem> T getByUUIDAndType(UUID uuid, Class<T> type);

	<T extends DeliveryTypeItem> T getDeliveryTypeByUUID(UUID uuid);

	void saveDeliveryInfo(CartItem cartItem, Map<String, Object> info);

	<T extends DeliveryServiceItem> T getByCode(String code);
}
