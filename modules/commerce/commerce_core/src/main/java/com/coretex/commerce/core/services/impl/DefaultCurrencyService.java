package com.coretex.commerce.core.services.impl;

import com.coretex.commerce.core.dao.CurrencyDao;
import com.coretex.commerce.core.services.AbstractGenericItemService;
import com.coretex.commerce.core.services.CurrencyService;
import com.coretex.items.cx_core.CurrencyItem;
import org.springframework.stereotype.Service;

@Service
public class DefaultCurrencyService extends AbstractGenericItemService<CurrencyItem> implements CurrencyService {

	private CurrencyDao repository;

	public DefaultCurrencyService(CurrencyDao repository) {
		super(repository);
		this.repository = repository;
	}

	@Override
	public CurrencyItem getByCode(String code) {
		return repository.findByCode(code);
	}
}
