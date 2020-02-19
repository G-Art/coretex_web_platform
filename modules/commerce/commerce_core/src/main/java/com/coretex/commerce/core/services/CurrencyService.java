package com.coretex.commerce.core.services;

import com.coretex.items.cx_core.CurrencyItem;

public interface CurrencyService extends GenericItemService<CurrencyItem> {
	CurrencyItem getByCode(String code);
}
