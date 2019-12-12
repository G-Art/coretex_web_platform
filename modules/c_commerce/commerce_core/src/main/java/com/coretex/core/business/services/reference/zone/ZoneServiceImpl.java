package com.coretex.core.business.services.reference.zone;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.coretex.items.core.LocaleItem;
import com.coretex.items.cx_core.ZoneItem;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.coretex.core.business.constants.Constants;

import com.coretex.core.business.repositories.reference.zone.ZoneDao;
import com.coretex.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.coretex.core.business.utils.CacheUtils;
import com.coretex.items.core.CountryItem;

@Service("zoneService")
public class ZoneServiceImpl extends SalesManagerEntityServiceImpl<ZoneItem> implements
		ZoneService {

	private final static String ZONE_CACHE_PREFIX = "ZONES_";

	private ZoneDao zoneDao;

	@Resource
	private CacheUtils cache;

	private static final Logger LOGGER = LoggerFactory.getLogger(ZoneServiceImpl.class);

	public ZoneServiceImpl(ZoneDao zoneDao) {
		super(zoneDao);
		this.zoneDao = zoneDao;
	}

	@Override
	public ZoneItem getByCode(String code) {
		return zoneDao.findByCode(code);
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<ZoneItem> getZones(CountryItem country, LocaleItem language)  {

		//Validate.notNull(country,"CountryItem cannot be null");
		Validate.notNull(language, "LocaleItem cannot be null");

		List<ZoneItem> zones = null;
		try {

			String countryCode = Constants.DEFAULT_COUNTRY;
			if (country != null) {
				countryCode = country.getIsoCode();
			}

			String cacheKey = ZONE_CACHE_PREFIX + countryCode + Constants.UNDERSCORE + language.getIso();

			zones = (List<ZoneItem>) cache.getFromCache(cacheKey);


			if (zones == null) {

				zones = zoneDao.listByLanguageAndCountry(countryCode, language.getUuid());

				cache.putInCache(zones, cacheKey);
			}

		} catch (Exception e) {
			LOGGER.error("getZones()", e);
		}
		return zones;


	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ZoneItem> getZones(String countryCode, LocaleItem language) {

		Validate.notNull(countryCode, "countryCode cannot be null");
		Validate.notNull(language, "LocaleItem cannot be null");

		List<ZoneItem> zones = null;
		try {


			String cacheKey = ZONE_CACHE_PREFIX + countryCode + Constants.UNDERSCORE + language.getIso();

			zones = (List<ZoneItem>) cache.getFromCache(cacheKey);


			if (zones == null) {

				zones = zoneDao.listByLanguageAndCountry(countryCode, language.getUuid());

				cache.putInCache(zones, cacheKey);
			}

		} catch (Exception e) {
			LOGGER.error("getZones()", e);
		}
		return zones;


	}

	@Override
	@SuppressWarnings("unchecked")
	public Map<String, ZoneItem> getZones(LocaleItem language) {

		Map<String, ZoneItem> zones = null;
		try {

			String cacheKey = ZONE_CACHE_PREFIX + language.getIso();

			zones = (Map<String, ZoneItem>) cache.getFromCache(cacheKey);


			if (zones == null) {
				zones = new HashMap<>();
				List<ZoneItem> zns = zoneDao.listByLanguage(language.getUuid());
				for (ZoneItem zoneItem : zns) {
					zones.put(zoneItem.getCode(), zoneItem);
				}
				//set names
				cache.putInCache(zones, cacheKey);
			}

		} catch (Exception e) {
			LOGGER.error("getZones()", e);
		}
		return zones;


	}

}
