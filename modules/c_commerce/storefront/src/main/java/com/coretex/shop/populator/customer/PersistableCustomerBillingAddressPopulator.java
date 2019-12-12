package com.coretex.shop.populator.customer;

import com.coretex.items.commerce_core_model.CustomerItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.core.LocaleItem;
import org.apache.commons.lang3.StringUtils;

import com.coretex.core.business.exception.ConversionException;
import com.coretex.shop.model.customer.address.Address;
import com.coretex.core.populators.AbstractDataPopulator;

public class PersistableCustomerBillingAddressPopulator extends AbstractDataPopulator<Address, CustomerItem> {

	@Override
	public CustomerItem populate(Address source, CustomerItem target, MerchantStoreItem store, LocaleItem language)
			throws ConversionException {


		target.getBilling().setFirstName(source.getFirstName());
		target.getBilling().setLastName(source.getLastName());

		// lets fill optional data now

		if (StringUtils.isNotBlank(source.getAddress())) {
			target.getBilling().setAddressLine1(source.getAddress());
		}

		if (StringUtils.isNotBlank(source.getCity())) {
			target.getBilling().setCity(source.getCity());
		}

		if (StringUtils.isNotBlank(source.getPhone())) {
			target.getBilling().setPhone(source.getPhone());
		}

		if (StringUtils.isNotBlank(source.getPostalCode())) {
			target.getBilling().setPostalCode(source.getPostalCode());
		}

		if (StringUtils.isNotBlank(source.getStateProvince())) {
			target.getBilling().setState(source.getStateProvince());
		}

		return target;

	}

	@Override
	protected CustomerItem createTarget() {
		return null;
	}


}
