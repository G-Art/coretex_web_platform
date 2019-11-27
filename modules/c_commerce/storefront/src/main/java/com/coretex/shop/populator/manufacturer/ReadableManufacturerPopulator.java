package com.coretex.shop.populator.manufacturer;

import com.coretex.core.business.exception.ConversionException;
import com.coretex.core.populators.AbstractDataPopulator;
import com.coretex.items.core.LocaleItem;
import com.coretex.items.commerce_core_model.ManufacturerItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.shop.model.catalog.manufacturer.ReadableManufacturer;

public class ReadableManufacturerPopulator extends AbstractDataPopulator<ManufacturerItem, ReadableManufacturer> {


	@Override
	public ReadableManufacturer populate(
			ManufacturerItem source,
			ReadableManufacturer target, MerchantStoreItem store, LocaleItem language) throws ConversionException {
		target.setUuid(source.getUuid());

		target.setOrder(source.getOrder()!=null? source.getOrder():0);
		target.setUuid(source.getUuid());
		target.setCode(source.getCode());
		target.setName(source.getName());
		target.setDescription(source.getDescription());

		return target;
	}

	@Override
	protected ReadableManufacturer createTarget() {
		return null;
	}
}
