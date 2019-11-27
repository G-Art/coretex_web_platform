package com.coretex.core.business.services.reference.loader;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.coretex.items.core.CountryItem;
import com.coretex.items.core.LocaleItem;
import com.coretex.items.commerce_core_model.ZoneItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.business.services.reference.country.CountryService;
import com.coretex.core.business.services.reference.language.LanguageService;

@Component
public class ZonesLoader {

	private static final Logger LOGGER = LoggerFactory.getLogger(ZonesLoader.class);

	@Resource
	private LanguageService languageService;

	@Resource
	private CountryService countryService;

	public Map<String, ZoneItem> loadZones(String jsonFilePath) throws Exception {


		List<LocaleItem> languages = languageService.list();

		List<CountryItem> countries = countryService.list();
		Map<String, CountryItem> countriesMap = new HashMap<String, CountryItem>();
		for (CountryItem country : countries) {

			countriesMap.put(country.getIsoCode(), country);

		}

		ObjectMapper mapper = new ObjectMapper();

		try {

			InputStream in =
					this.getClass().getClassLoader().getResourceAsStream(jsonFilePath);

			@SuppressWarnings("unchecked")
			Map<String, Object> data = mapper.readValue(in, Map.class);

			Map<String, ZoneItem> zonesMap = new HashMap<String, ZoneItem>();
			Map<String, String> zonesMark = new HashMap<String, String>();

			@SuppressWarnings("rawtypes")
			List langList = (List) data.get("en");
			if (langList != null) {
				for (Object z : langList) {
					@SuppressWarnings("unchecked")
					Map<String, String> e = (Map<String, String>) z;
					String zoneCode = e.get("zoneCode");
					ZoneItem zone = null;
					if (!zonesMap.containsKey(zoneCode)) {
						zone = new ZoneItem();
						zone.setName(e.get("zoneName"));
						CountryItem country = countriesMap.get(e.get("countryCode"));
						if (country == null) {
							LOGGER.warn("CountryItem is null for " + zoneCode + " and country code " + e.get("countryCode"));
							continue;
						}
						zone.setCountry(country);
						zonesMap.put(zoneCode, zone);
						zone.setCode(zoneCode);

					}


					if (zonesMark.containsKey(zoneCode)) {
						LOGGER.warn("This zone seems to be a duplicate !  " + zoneCode);
						continue;
					}

					zonesMark.put(zoneCode, zoneCode);
				}
			}


			return zonesMap;


		} catch (Exception e) {
			throw new ServiceException(e);
		}


	}

}
