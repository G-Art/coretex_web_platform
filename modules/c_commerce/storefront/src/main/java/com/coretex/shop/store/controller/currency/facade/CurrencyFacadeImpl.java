package com.coretex.shop.store.controller.currency.facade;

import com.coretex.core.business.services.reference.currency.CurrencyService;
import com.coretex.items.commerce_core_model.CurrencyItem;
import com.coretex.shop.store.api.exception.ResourceNotFoundException;

import java.util.List;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

@Service
public class CurrencyFacadeImpl implements CurrencyFacade {

	@Resource
	private CurrencyService currencyService;

	@Override
	public List<CurrencyItem> getList() {
		List<CurrencyItem> currencyList = currencyService.list();
		if (currencyList.isEmpty()) {
			throw new ResourceNotFoundException("No languages found");
		}
		return currencyList;
	}
}
