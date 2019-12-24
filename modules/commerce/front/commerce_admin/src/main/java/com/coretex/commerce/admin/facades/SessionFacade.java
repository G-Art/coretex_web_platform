package com.coretex.commerce.admin.facades;

import com.coretex.commerce.data.CurrencyData;
import com.coretex.commerce.data.LocaleData;

import java.util.Collection;

public interface SessionFacade {

	LocaleData getCurrentLanguage();

	CurrencyData getCurrentCurrency();

	Collection<LocaleData> getAllLanguages();

	Collection<CurrencyData> getAllCurrencies();

	LocaleData getDefaultLanguage();

	CurrencyData getDefaultCurrency();

}
