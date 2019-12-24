package com.coretex.commerce.admin.facades.impl;

import com.coretex.commerce.admin.facades.SessionFacade;
import com.coretex.commerce.core.dao.CurrencyDao;
import com.coretex.commerce.data.CurrencyData;
import com.coretex.commerce.data.LocaleData;
import com.coretex.commerce.mapper.CurrencyDataMapper;
import com.coretex.commerce.mapper.LocaleDataMapper;
import com.coretex.core.activeorm.dao.LocaleDao;
import com.google.common.base.Suppliers;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Component
public class DefaultSessionFacade implements SessionFacade {

	@Resource
	private LocaleDataMapper localeDataMapper;
	@Resource
	private CurrencyDataMapper currencyDataMapper;

	@Resource
	private LocaleDao localeDao;

	@Resource
	private CurrencyDao currencyDao;

	private Supplier<Collection<LocaleData>> localesSupplier;
	private Supplier<Collection<CurrencyData>> currenciesSupplier;

	@PostConstruct
	private void init(){
		localesSupplier = Suppliers.memoizeWithExpiration(()-> localeDao.find()
				.stream()
				.map(localeDataMapper::fromItem)
				.collect(Collectors.toList()), 30, TimeUnit.MINUTES);
		currenciesSupplier = Suppliers.memoizeWithExpiration(()-> currencyDao.find()
				.stream()
				.map(currencyDataMapper::fromItem)
				.collect(Collectors.toList()), 30, TimeUnit.MINUTES);
	}

	@Override
	public LocaleData getCurrentLanguage() {
		return null;
	}

	@Override
	public CurrencyData getCurrentCurrency() {
		return null;
	}

	@Override
	public Collection<LocaleData> getAllLanguages() {
		return localesSupplier.get();
	}

	@Override
	public Collection<CurrencyData> getAllCurrencies() {
		return currenciesSupplier.get();
	}

	@Override
	public LocaleData getDefaultLanguage() {
		return null;
	}

	@Override
	public CurrencyData getDefaultCurrency() {
		return null;
	}

}
