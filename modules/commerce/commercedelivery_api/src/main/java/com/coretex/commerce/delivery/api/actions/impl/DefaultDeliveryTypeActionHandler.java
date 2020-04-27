package com.coretex.commerce.delivery.api.actions.impl;

import com.coretex.commerce.delivery.api.actions.AddDeliveryInfoAction;
import com.coretex.commerce.delivery.api.actions.AdditionalInfoAction;
import com.coretex.commerce.delivery.api.actions.DeliveryTypeActionHandler;
import com.coretex.items.cx_commercedelivery_api.DeliveryTypeItem;
import com.coretex.items.cx_core.CartItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DefaultDeliveryTypeActionHandler implements DeliveryTypeActionHandler {
	private final Logger LOG = LoggerFactory.getLogger(this.getClass());

	private Map<String, AdditionalInfoAction<DeliveryTypeItem>> additionalInfoActionMap = new HashMap<>();
	private Map<String, AddDeliveryInfoAction> addDeliveryInfoActionMap = new HashMap<>();


	@Override
	public <T extends DeliveryTypeItem> Map<String, Object> getAdditionalInfo(T deliveryTypeItem) {
		AdditionalInfoAction<DeliveryTypeItem> additionalInfoAction = additionalInfoActionMap.get(deliveryTypeItem.getMetaType().getTypeCode());
		if (Objects.nonNull(additionalInfoAction)) {
			return additionalInfoAction.execute(deliveryTypeItem);
		}
		LOG.warn(String.format("AdditionalInfo action is not defined for [%s]", deliveryTypeItem.getMetaType().getTypeCode()));
		return Map.of();
	}

	@Override
	public void addDeliveryInfo(CartItem cartItem, Map<String, Object> info) {
		var deliveryType = cartItem.getDeliveryType();
		AddDeliveryInfoAction addDeliveryInfoAction = addDeliveryInfoActionMap.get(deliveryType.getMetaType().getTypeCode());

		if (Objects.nonNull(addDeliveryInfoAction)) {
			addDeliveryInfoAction.execute(cartItem, info);
		} else {
			LOG.warn(String.format("AddDeliveryInfoAction action is not defined for [%s]", deliveryType.getMetaType().getTypeCode()));
		}

	}

	public void setAdditionalInfoActionMap(Map<String, AdditionalInfoAction<DeliveryTypeItem>> additionalInfoActionMap) {
		this.additionalInfoActionMap = additionalInfoActionMap;
		LOG.info(String.format("Additional info actions for %s are loaded", additionalInfoActionMap.keySet()));
	}

	public void setAddDeliveryInfoActionMap(Map<String, AddDeliveryInfoAction> addDeliveryInfoActionMap) {
		this.addDeliveryInfoActionMap = addDeliveryInfoActionMap;
		LOG.info(String.format("AddDeliveryInfo actions for %s are loaded", addDeliveryInfoActionMap.keySet()));
	}
}
