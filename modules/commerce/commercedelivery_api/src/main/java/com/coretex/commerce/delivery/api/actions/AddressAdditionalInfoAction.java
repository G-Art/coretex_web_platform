package com.coretex.commerce.delivery.api.actions;

import com.coretex.items.cx_core.AddressItem;

import java.util.Map;

public interface AddressAdditionalInfoAction<T extends AddressItem> extends TypeAction  {
	Map<String, Object> execute(T object);
}
