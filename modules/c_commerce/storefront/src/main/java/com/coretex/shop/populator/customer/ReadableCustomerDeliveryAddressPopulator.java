
package com.coretex.shop.populator.customer;

import com.coretex.core.business.exception.ConversionException;

import com.coretex.core.business.services.reference.country.CountryService;
import com.coretex.core.business.services.reference.zone.ZoneService;
import com.coretex.core.populators.AbstractDataPopulator;
import com.coretex.items.commerce_core_model.DeliveryItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.core.CountryItem;
import com.coretex.items.core.LocaleItem;
import com.coretex.items.commerce_core_model.ZoneItem;
import com.coretex.shop.model.customer.ReadableDelivery;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @author Carl Samson
 *
 */
public class ReadableCustomerDeliveryAddressPopulator extends AbstractDataPopulator<DeliveryItem, ReadableDelivery> {


	private CountryService countryService;
	private ZoneService zoneService;

	@Override
	public ReadableDelivery populate(DeliveryItem source, ReadableDelivery target, MerchantStoreItem store, LocaleItem language)
			throws ConversionException {


		if (countryService == null) {
			throw new ConversionException("countryService must be set");
		}

		if (zoneService == null) {
			throw new ConversionException("zoneService must be set");
		}


		target.setLatitude(source.getLatitude());
		target.setLongitude(source.getLongitude());


		if (StringUtils.isNotBlank(source.getCity())) {
			target.setCity(source.getCity());
		}

		if (StringUtils.isNotBlank(source.getCompany())) {
			target.setCompany(source.getCompany());
		}

		if (StringUtils.isNotBlank(source.getAddress())) {
			target.setAddress(source.getAddress());
		}

		if (StringUtils.isNotBlank(source.getFirstName())) {
			target.setFirstName(source.getFirstName());
		}

		if (StringUtils.isNotBlank(source.getLastName())) {
			target.setLastName(source.getLastName());
		}

		if (StringUtils.isNotBlank(source.getPostalCode())) {
			target.setPostalCode(source.getPostalCode());
		}

		if (StringUtils.isNotBlank(source.getTelephone())) {
			target.setPhone(source.getTelephone());
		}

		target.setStateProvince(source.getState());

		if (source.getTelephone() == null) {
			target.setPhone(source.getTelephone());
		}
		target.setAddress(source.getAddress());
		if (source.getCountry() != null) {
			target.setCountry(source.getCountry().getIsoCode());

			//resolve country name
			Map<String, CountryItem> countries = countryService.getCountriesMap(language);
			CountryItem c = countries.get(source.getCountry().getIsoCode());
			if (c != null) {
				target.setCountryName(c.getName());
			}
		}
		if (source.getZone() != null) {
			target.setZone(source.getZone().getCode());

			//resolve zone name
			Map<String, ZoneItem> zones = zoneService.getZones(language);
			ZoneItem z = zones.get(source.getZone().getCode());
			if (z != null) {
				target.setProvinceName(z.getName());
			}
		}


		return target;
	}

	@Override
	protected ReadableDelivery createTarget() {
		// TODO Auto-generated method stub
		return null;
	}

	public CountryService getCountryService() {
		return countryService;
	}

	public void setCountryService(CountryService countryService) {
		this.countryService = countryService;
	}

	public ZoneService getZoneService() {
		return zoneService;
	}

	public void setZoneService(ZoneService zoneService) {
		this.zoneService = zoneService;
	}


}
