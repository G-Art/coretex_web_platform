package com.coretex.commerce.delivery.api.dao;

import com.coretex.core.activeorm.dao.Dao;
import com.coretex.items.cx_commercedelivery_api.DeliveryServiceItem;

import java.util.UUID;

public interface DeliveryServiceDao extends Dao<DeliveryServiceItem> {
	<T extends DeliveryServiceItem> T getByUUIDAndType(UUID uuid, Class<T> type);
}
