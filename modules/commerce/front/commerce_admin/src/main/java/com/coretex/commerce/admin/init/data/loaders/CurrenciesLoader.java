package com.coretex.commerce.admin.init.data.loaders;

import com.coretex.commerce.core.init.SchemaConstant;
import com.coretex.commerce.core.init.loader.DataLoader;
import com.coretex.commerce.core.services.CurrencyService;
import com.coretex.items.cx_core.CurrencyItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class CurrenciesLoader implements DataLoader {

	private static final Logger LOGGER = LoggerFactory.getLogger(CurrenciesLoader.class);

	@Resource
	private CurrencyService currencyService;

	@Override
	public int priority() {
		return PRIORITY_MAX;
	}

	@Override
	public boolean load(String name) {
		LOGGER.info(String.format("%s : Populating Currencies ", name));

		for (String code : SchemaConstant.CURRENCY_MAP.keySet()) {

			try {
				java.util.Currency c = java.util.Currency.getInstance(code);

				if (c == null) {
					LOGGER.info(String.format("%s : Populating Currencies : no currency for code : %s", name, code));
				}else{

					CurrencyItem currency = new CurrencyItem();
					currency.setCode(c.getCurrencyCode());
					currency.setName(c.getDisplayName());
					currency.setSymbol(c.getSymbol());
					currencyService.create(currency);
				}

				//System.out.println(l.getCountry() + "   " + c.getSymbol() + "  " + c.getSymbol(l));
			} catch (IllegalArgumentException e) {
				LOGGER.info(String.format("%s : Populating Currencies : no currency for code : %s", name, code));
			}
		}
		return true;
	}
}
