package com.coretex.commerce.delivery.api.actions;

import com.coretex.items.cx_commercedelivery_api.DeliveryTypeItem;

import java.util.Map;

public interface AdditionalInfoAction<T extends DeliveryTypeItem> extends TypeAction  {
	Map<String, Object> execute(T object);
}
