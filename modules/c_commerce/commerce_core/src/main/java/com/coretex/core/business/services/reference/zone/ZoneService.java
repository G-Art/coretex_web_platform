package com.coretex.core.business.services.reference.zone;

import java.util.List;
import java.util.Map;

import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.business.services.common.generic.SalesManagerEntityService;
import com.coretex.items.core.CountryItem;
import com.coretex.items.core.LocaleItem;
import com.coretex.items.commerce_core_model.ZoneItem;

public interface ZoneService extends SalesManagerEntityService<ZoneItem> {

	ZoneItem getByCode(String code);

	List<ZoneItem> getZones(CountryItem country, LocaleItem language)
			throws ServiceException;

	Map<String, ZoneItem> getZones(LocaleItem language) throws ServiceException;

	List<ZoneItem> getZones(String countryCode, LocaleItem language) throws ServiceException;


}
