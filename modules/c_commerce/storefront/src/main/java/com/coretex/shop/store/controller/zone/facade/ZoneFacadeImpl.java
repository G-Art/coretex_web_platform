package com.coretex.shop.store.controller.zone.facade;

import com.coretex.core.business.exception.ConversionException;
import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.business.services.reference.zone.ZoneService;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.items.commerce_core_model.ZoneItem;
import com.coretex.shop.model.references.ReadableZone;
import com.coretex.shop.populator.references.ReadableZonePopulator;
import com.coretex.shop.store.api.exception.ConversionRuntimeException;
import com.coretex.shop.store.api.exception.ResourceNotFoundException;
import com.coretex.shop.store.api.exception.ServiceRuntimeException;

import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

@Service
public class ZoneFacadeImpl implements ZoneFacade {

	@Resource
	private ZoneService zoneService;

	@Override
	public List<ReadableZone> getZones(String countryCode, LanguageItem language, MerchantStoreItem merchantStore) {
		List<ZoneItem> listZones = getListZones(countryCode, language);
		if (listZones.isEmpty()) {
			throw new ResourceNotFoundException("No zones found");
		}
		return listZones.stream()
				.map(zone -> convertToReadableZone(zone, language, merchantStore))
				.collect(Collectors.toList());
	}

	private ReadableZone convertToReadableZone(ZoneItem zone, LanguageItem language, MerchantStoreItem merchantStore) {
		try {
			ReadableZonePopulator populator = new ReadableZonePopulator();
			return populator.populate(zone, new ReadableZone(), merchantStore, language);
		} catch (ConversionException e) {
			throw new ConversionRuntimeException(e);
		}
	}

	private List<ZoneItem> getListZones(String countryCode, LanguageItem language) {
		try {
			return zoneService.getZones(countryCode, language);
		} catch (ServiceException e) {
			throw new ServiceRuntimeException(e);
		}
	}
}
