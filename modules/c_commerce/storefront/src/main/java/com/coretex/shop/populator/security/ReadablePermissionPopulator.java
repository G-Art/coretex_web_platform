package com.coretex.shop.populator.security;

import com.coretex.core.business.exception.ConversionException;
import com.coretex.core.populators.AbstractDataPopulator;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.core.LocaleItem;
import com.coretex.items.commerce_core_model.PermissionItem;
import com.coretex.shop.model.security.ReadablePermission;

public class ReadablePermissionPopulator extends AbstractDataPopulator<PermissionItem, ReadablePermission> {

	@Override
	public ReadablePermission populate(PermissionItem source, ReadablePermission target,
									   MerchantStoreItem store, LocaleItem language) throws ConversionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ReadablePermission createTarget() {
		// TODO Auto-generated method stub
		return null;
	}

}
