package com.coretex.core.business.services.reference.country;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.coretex.items.core.LocaleItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.business.repositories.reference.country.CountryDao;
import com.coretex.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.coretex.core.business.utils.CacheUtils;
import com.coretex.items.core.CountryItem;

@Service("countryService")
public class CountryServiceImpl extends SalesManagerEntityServiceImpl<CountryItem>
		implements CountryService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CountryServiceImpl.class);

	private CountryDao countryDao;

	@Resource
	private CacheUtils cache;


	public CountryServiceImpl(CountryDao countryDao) {
		super(countryDao);
		this.countryDao = countryDao;
	}

	public CountryItem getByCode(String code) {
		return countryDao.findByIsoCode(code);
	}

	@Override
	public Map<String, CountryItem> getCountriesMap(LocaleItem language) throws ServiceException {

		List<CountryItem> countries = this.getCountries(language);

		Map<String, CountryItem> returnMap = new LinkedHashMap<String, CountryItem>();

		for (CountryItem country : countries) {
			returnMap.put(country.getIsoCode(), country);
		}

		return returnMap;
	}


	@Override
	public List<CountryItem> getCountries(final List<String> isoCodes, final LocaleItem language) throws ServiceException {
		List<CountryItem> countryList = getCountries(language);
		List<CountryItem> requestedCountryList = new ArrayList<CountryItem>();
		if (!CollectionUtils.isEmpty(countryList)) {
			for (CountryItem c : countryList) {
				if (isoCodes.contains(c.getIsoCode())) {
					requestedCountryList.add(c);
				}
			}
		}
		return requestedCountryList;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<CountryItem> getCountries(LocaleItem language) throws ServiceException {

		List<CountryItem> countries = null;
		try {

			countries = (List<CountryItem>) cache.getFromCache("COUNTRIES_" + language.getIso());


			if (countries == null) {

				countries = countryDao.listByLanguage(language);

				cache.putInCache(countries, "COUNTRIES_" + language.getIso());
			}

		} catch (Exception e) {
			LOGGER.error("getCountries()", e);
		}

		return countries;


	}

	@Override
	public List<CountryItem> listCountryZones(LocaleItem language) throws ServiceException {
		try {
			return countryDao.listCountryZonesByLanguage(language.getUuid());
		} catch (Exception e) {
			LOGGER.error("listCountryZones", e);
			throw new ServiceException(e);
		}

	}


}
