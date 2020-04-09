package com.coretex.commerce.admin.init.data.loaders;

import com.coretex.commerce.core.init.loader.DataLoader;
import com.coretex.commerce.core.services.CountryService;
import com.coretex.commerce.core.services.ZoneService;
import com.coretex.items.core.CountryItem;
import com.coretex.items.cx_core.ZoneItem;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ZonesLoader implements DataLoader {

	private static final Logger LOGGER = LoggerFactory.getLogger(ZonesLoader.class);

	@Resource
	private ZoneService zoneService;

	@Resource
	private CountryService countryService;

	public Map<String, ZoneItem> loadZones(String jsonFilePath) {

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

			Map<String, ZoneItem> zonesMap = new HashMap<>();
			Map<String, String> zonesMark = new HashMap<>();

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
//						zone.setCountry(country);
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
			throw new RuntimeException(e);
		}


	}

	@Override
	public boolean load(String name) {
		LOGGER.info(String.format("%s : Populating Zones", name));
		try {

			Map<String, ZoneItem> zonesMap = loadZones("reference/zoneconfig.json");

			for (Map.Entry<String, ZoneItem> entry : zonesMap.entrySet()) {
				ZoneItem value = entry.getValue();

				zoneService.create(value);

			}

		} catch (Exception e) {

			throw new RuntimeException(e);
		}
		return true;
	}

	@Override
	public int priority() {
		return PRIORITY_70;
	}
}