package com.coretex.commerce.core.services;

import com.coretex.items.core.CountryItem;

public interface CountryService extends GenericItemService<CountryItem> {
	CountryItem getByCode(String code);
}
