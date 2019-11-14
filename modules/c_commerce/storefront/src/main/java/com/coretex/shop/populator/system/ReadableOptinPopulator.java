package com.coretex.shop.populator.system;

import com.coretex.items.commerce_core_model.OptinItem;
import org.apache.commons.lang3.Validate;

import com.coretex.core.business.exception.ConversionException;
import com.coretex.core.populators.AbstractDataPopulator;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.shop.model.system.ReadableOptin;

public class ReadableOptinPopulator extends AbstractDataPopulator<OptinItem, ReadableOptin> {

	@Override
	public ReadableOptin populate(OptinItem source, ReadableOptin target, MerchantStoreItem store, LanguageItem language)
			throws ConversionException {
		Validate.notNull(store, "MerchantStoreItem cannot be null");
		Validate.notNull(source, "OptinItem cannot be null");

		if (target == null) {
			target = new ReadableOptin();
		}

		target.setCode(source.getCode());
		target.setDescription(source.getDescription());
		target.setEndDate(source.getEndDate());
		target.setUuid(source.getUuid());
		target.setOptinType(source.getOptinType().name());
		target.setStartDate(source.getStartDate());
		target.setStore(store.getCode());

		return target;
	}

	@Override
	protected ReadableOptin createTarget() {
		// TODO Auto-generated method stub
		return null;
	}

}
