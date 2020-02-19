package com.coretex.commerce.core.services;

import com.coretex.items.cx_core.ZoneItem;

public interface ZoneService extends GenericItemService<ZoneItem> {
	ZoneItem getByCode(String code);
}
