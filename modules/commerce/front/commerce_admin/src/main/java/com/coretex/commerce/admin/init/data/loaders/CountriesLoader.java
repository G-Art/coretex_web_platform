package com.coretex.commerce.admin.init.data.loaders;

import com.coretex.commerce.core.init.SchemaConstant;
import com.coretex.commerce.core.init.loader.DataLoader;
import com.coretex.commerce.core.services.CountryService;
import com.coretex.commerce.core.services.LocaleService;
import com.coretex.items.core.CountryItem;
import com.coretex.items.core.LocaleItem;
import org.apache.commons.lang3.LocaleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

@Component
public class CountriesLoader implements DataLoader {

	private static final Logger LOGGER = LoggerFactory.getLogger(CountriesLoader.class);

	@Resource
	private LocaleService localeService;

	@Resource
	private CountryService countryService;

	@Override
	public int priority() {
		return PRIORITY_80;
	}

	@Override
	public boolean load(String name) {
		LOGGER.info(String.format("%s : Populating Countries ", name));
		SchemaConstant.COUNTRY_CONFIG.forEach((k, v) -> {
			CountryItem country = new CountryItem();
			country.setIsoCode(k);
			country.setSupported(true);
			country.setActive(true);

			for (String langName : v.get("name").split(",")) {
				var split = langName.split(":");
				var loc = LocaleUtils.toLocale(split[0]);
				country.setName(split[1], loc);
			}


			for (String lang : v.get("languages").split(",")) {
				var loc = LocaleUtils.toLocale(lang);
				var localeItem = localeService.getByIso(lang);
				if(Objects.isNull(localeItem)){
					localeItem = new LocaleItem();
					localeItem.setIso(lang);
					localeItem.setName(loc.getDisplayName(), loc);
					localeItem.setActive(true);
				}

				if(Objects.isNull(country.getDefaultLocale())){
					country.setDefaultLocale(localeItem);
				}
				country.getLocales().add(localeItem);
			}
			countryService.create(country);
		});
		return true;
	}
}
