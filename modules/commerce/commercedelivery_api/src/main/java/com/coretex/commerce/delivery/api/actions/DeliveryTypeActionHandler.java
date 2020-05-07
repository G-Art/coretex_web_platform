package com.coretex.commerce.delivery.api.actions;

import com.coretex.items.cx_commercedelivery_api.DeliveryTypeItem;
import com.coretex.items.cx_core.AddressItem;
import com.coretex.items.cx_core.CartItem;

import java.util.Map;

public interface DeliveryTypeActionHandler {

	<T extends DeliveryTypeItem> Map<String, Object>  getAdditionalInfo(T deliveryTypeItem);

	void addDeliveryInfo(CartItem cartItem, Map<String, Object> info);

	Map<String, Object> addressAdditionalInfo(AddressItem addressItem, DeliveryTypeItem deliveryType);
}
