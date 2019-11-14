package com.coretex.shop.populator.references;

import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.core.CountryItem;
import org.apache.commons.collections4.CollectionUtils;

import com.coretex.core.business.exception.ConversionException;
import com.coretex.core.populators.AbstractDataPopulator;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.items.commerce_core_model.ZoneItem;
import com.coretex.shop.model.references.ReadableCountry;
import com.coretex.shop.model.references.ReadableZone;

public class ReadableCountryPopulator extends AbstractDataPopulator<CountryItem, ReadableCountry> {

	@Override
	public ReadableCountry populate(CountryItem source, ReadableCountry target, MerchantStoreItem store, LanguageItem language)
			throws ConversionException {

		if (target == null) {
			target = new ReadableCountry();
		}

		target.setUuid(source.getUuid());
		target.setCode(source.getIsoCode());
		target.setSupported(source.getSupported());
		target.setName(source.getName());

		if (!CollectionUtils.isEmpty(source.getZones())) {
			for (ZoneItem z : source.getZones()) {
				ReadableZone readableZone = new ReadableZone();
				readableZone.setCountryCode(target.getCode());
				readableZone.setUuid(z.getUuid());
				readableZone.setName(z.getName());
				target.getZones().add(readableZone);
			}
		}

		return target;
	}

	@Override
	protected ReadableCountry createTarget() {
		// TODO Auto-generated method stub
		return null;
	}

}
