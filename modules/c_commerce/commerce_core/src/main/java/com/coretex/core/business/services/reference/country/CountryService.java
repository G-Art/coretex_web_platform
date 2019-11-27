package com.coretex.core.business.services.reference.country;

import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.business.services.common.generic.SalesManagerEntityService;
import com.coretex.items.core.CountryItem;
import com.coretex.items.core.LocaleItem;

import java.util.List;
import java.util.Map;

public interface CountryService extends SalesManagerEntityService<CountryItem> {

	CountryItem getByCode(String code);

	List<CountryItem> getCountries(LocaleItem language) throws ServiceException;

	Map<String, CountryItem> getCountriesMap(LocaleItem language)
			throws ServiceException;

	List<CountryItem> getCountries(List<String> isoCodes, LocaleItem language)
			throws ServiceException;


	/**
	 * List country - zone objects by language
	 *
	 * @param language
	 * @return
	 * @throws ServiceException
	 */
	List<CountryItem> listCountryZones(LocaleItem language) throws ServiceException;
}
