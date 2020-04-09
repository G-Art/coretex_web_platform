package com.coretex.commerce.facades.impl;

import com.coretex.commerce.core.services.CartService;
import com.coretex.commerce.core.services.PageableService;
import com.coretex.commerce.data.DeliveryServiceData;
import com.coretex.commerce.data.DeliveryTypeData;
import com.coretex.commerce.data.minimal.MinimalDeliveryServiceData;
import com.coretex.commerce.delivery.api.service.DeliveryServiceService;
import com.coretex.commerce.facades.DeliveryServiceFacade;
import com.coretex.commerce.mapper.DeliveryServiceDataMapper;
import com.coretex.commerce.mapper.DeliveryTypeDataMapper;
import com.coretex.commerce.mapper.GenericDataMapper;
import com.coretex.commerce.mapper.minimal.MinimalDeliveryServiceDataMapper;
import com.coretex.items.cx_commercedelivery_api.DeliveryServiceItem;
import com.coretex.items.cx_commercedelivery_api.DeliveryTypeItem;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.stream.Stream;

@Component
public class DefaultDeliveryServiceFacade implements DeliveryServiceFacade {

	@Resource
	private DeliveryServiceService deliveryServiceService;

	@Resource
	private CartService cartService;

	@Resource
	private MinimalDeliveryServiceDataMapper minimalDeliveryServiceDataMapper;

	@Resource
	private DeliveryServiceDataMapper deliveryServiceDataMapper;

	@Resource
	private DeliveryTypeDataMapper deliveryTypeDataMapper;

	@Override
	public PageableService<DeliveryServiceItem> getPageableService() {
		return deliveryServiceService;
	}

	@Override
	public GenericDataMapper<DeliveryServiceItem, MinimalDeliveryServiceData> getDataMapper() {
		return minimalDeliveryServiceDataMapper;
	}

	@Override
	public DeliveryServiceData getByUUID(UUID uuid) {
		return deliveryServiceDataMapper.fromItem(deliveryServiceService.getByUUID(uuid));
	}

	@Override
	public Stream<DeliveryServiceData> getForCartUUID(UUID uuid) {
		var store = cartService.getByUUID(uuid).getStore();
		return store.getDeliveryServices().stream().map(deliveryServiceDataMapper::fromItem);
	}

	@Override
	public Stream<DeliveryTypeData> getDeliveryTypesForService(UUID uuid) {
		var types = deliveryServiceService.getByUUID(uuid).getDeliveryTypes();

		return types.stream().filter(DeliveryTypeItem::getActive).map(deliveryTypeDataMapper::fromItem);
	}
}
