package com.coretex.commerce.core.services.impl;

import com.coretex.commerce.core.dao.CountryDao;
import com.coretex.commerce.core.services.AbstractGenericItemService;
import com.coretex.commerce.core.services.CountryService;
import com.coretex.items.core.CountryItem;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DefaultCountryService extends AbstractGenericItemService<CountryItem> implements CountryService {
	public DefaultCountryService(CountryDao repository) {
		super(repository);
	}

	@Override
	public CountryItem getByCode(String code) {
		return getRepository().findSingle(Map.of(CountryItem.ISO_CODE, code), true);
	}
}
