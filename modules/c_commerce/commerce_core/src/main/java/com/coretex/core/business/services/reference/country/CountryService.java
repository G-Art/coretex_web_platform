package com.coretex.core.business.services.reference.country;

import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.business.services.common.generic.SalesManagerEntityService;
import com.coretex.items.core.CountryItem;
import com.coretex.items.commerce_core_model.LanguageItem;

import java.util.List;
import java.util.Map;

public interface CountryService extends SalesManagerEntityService<CountryItem> {

	CountryItem getByCode(String code);

	List<CountryItem> getCountries(LanguageItem language) throws ServiceException;

	Map<String, CountryItem> getCountriesMap(LanguageItem language)
			throws ServiceException;

	List<CountryItem> getCountries(List<String> isoCodes, LanguageItem language)
			throws ServiceException;


	/**
	 * List country - zone objects by language
	 *
	 * @param language
	 * @return
	 * @throws ServiceException
	 */
	List<CountryItem> listCountryZones(LanguageItem language) throws ServiceException;
}
