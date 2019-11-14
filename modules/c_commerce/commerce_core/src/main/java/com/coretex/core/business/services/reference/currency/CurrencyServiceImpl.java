package com.coretex.core.business.services.reference.currency;

import com.coretex.items.commerce_core_model.CurrencyItem;
import org.springframework.stereotype.Service;

import com.coretex.core.business.repositories.reference.currency.CurrencyDao;
import com.coretex.core.business.services.common.generic.SalesManagerEntityServiceImpl;

@Service("currencyService")
public class CurrencyServiceImpl extends SalesManagerEntityServiceImpl<CurrencyItem>
		implements CurrencyService {

	private CurrencyDao currencyDao;

	public CurrencyServiceImpl(CurrencyDao currencyDao) {
		super(currencyDao);
		this.currencyDao = currencyDao;
	}

	@Override
	public CurrencyItem getByCode(String code) {
		return currencyDao.getByCode(code);
	}

}
