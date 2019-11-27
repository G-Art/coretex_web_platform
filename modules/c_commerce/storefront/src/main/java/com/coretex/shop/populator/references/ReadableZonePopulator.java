package com.coretex.shop.populator.references;

import com.coretex.core.business.exception.ConversionException;
import com.coretex.core.populators.AbstractDataPopulator;
import com.coretex.items.core.LocaleItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.ZoneItem;
import com.coretex.shop.model.references.ReadableZone;

public class ReadableZonePopulator extends AbstractDataPopulator<ZoneItem, ReadableZone> {

	@Override
	public ReadableZone populate(ZoneItem source, ReadableZone target, MerchantStoreItem store, LocaleItem language)
			throws ConversionException {
		if (target == null) {
			target = new ReadableZone();
		}

		target.setUuid(source.getUuid());
		target.setCode(source.getCode());
		target.setCountryCode(source.getCountry().getIsoCode());

		target.setName(source.getName());

		return target;

	}

	@Override
	protected ReadableZone createTarget() {
		// TODO Auto-generated method stub
		return null;
	}

}
