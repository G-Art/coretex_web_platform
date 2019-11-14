package com.coretex.shop.store.controller.country.facade;

import com.coretex.core.business.exception.ConversionException;
import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.business.services.reference.country.CountryService;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.core.CountryItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.shop.model.references.ReadableCountry;
import com.coretex.shop.populator.references.ReadableCountryPopulator;
import com.coretex.shop.store.api.exception.ConversionRuntimeException;
import com.coretex.shop.store.api.exception.ServiceRuntimeException;

import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

@Service
public class CountryFacadeImpl implements CountryFacade {

	@Resource
	private CountryService countryService;

	@Override
	public List<ReadableCountry> getListCountryZones(LanguageItem language, MerchantStoreItem merchantStore) {
		return getListOfCountryZones(language)
				.stream()
				.map(country -> convertToReadableCountry(country, language, merchantStore))
				.collect(Collectors.toList());
	}

	private ReadableCountry convertToReadableCountry(CountryItem country, LanguageItem language, MerchantStoreItem merchantStore) {
		try {
			ReadableCountryPopulator populator = new ReadableCountryPopulator();
			return populator.populate(country, new ReadableCountry(), merchantStore, language);
		} catch (ConversionException e) {
			throw new ConversionRuntimeException(e);
		}
	}

	private List<CountryItem> getListOfCountryZones(LanguageItem language) {
		try {
			return countryService.listCountryZones(language);
		} catch (ServiceException e) {
			throw new ServiceRuntimeException(e);
		}
	}
}
