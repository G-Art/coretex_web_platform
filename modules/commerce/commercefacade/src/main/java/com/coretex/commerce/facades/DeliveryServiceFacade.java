package com.coretex.commerce.facades;

import com.coretex.commerce.data.DeliveryServiceData;
import com.coretex.commerce.data.DeliveryTypeData;
import com.coretex.commerce.data.minimal.MinimalDeliveryServiceData;
import com.coretex.items.cx_commercedelivery_api.DeliveryServiceItem;

import java.util.UUID;
import java.util.stream.Stream;

public interface DeliveryServiceFacade extends PageableDataTableFacade<DeliveryServiceItem, MinimalDeliveryServiceData> {

	DeliveryServiceData getByUUID(UUID uuid);

	Stream<DeliveryServiceData> getForCartUUID(UUID uuid);

	Stream<DeliveryTypeData> getDeliveryTypesForService(UUID uuid);
}
