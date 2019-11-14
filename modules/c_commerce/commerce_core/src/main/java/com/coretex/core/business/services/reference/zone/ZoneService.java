package com.coretex.core.business.services.reference.zone;

import java.util.List;
import java.util.Map;

import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.business.services.common.generic.SalesManagerEntityService;
import com.coretex.items.core.CountryItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.items.commerce_core_model.ZoneItem;

public interface ZoneService extends SalesManagerEntityService<ZoneItem> {

	ZoneItem getByCode(String code);

	List<ZoneItem> getZones(CountryItem country, LanguageItem language)
			throws ServiceException;

	Map<String, ZoneItem> getZones(LanguageItem language) throws ServiceException;

	List<ZoneItem> getZones(String countryCode, LanguageItem language) throws ServiceException;


}
