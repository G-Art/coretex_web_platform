package com.coretex.commerce.core.services.impl;

import com.coretex.commerce.core.dao.ZoneDao;
import com.coretex.commerce.core.services.AbstractGenericItemService;
import com.coretex.commerce.core.services.ZoneService;
import com.coretex.items.cx_core.ZoneItem;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DefaultZoneService extends AbstractGenericItemService<ZoneItem> implements ZoneService {
	public DefaultZoneService(ZoneDao repository) {
		super(repository);
	}

	@Override
	public ZoneItem getByCode(String code) {
		return getRepository().findSingle(Map.of(ZoneItem.CODE, code), true);
	}
}
