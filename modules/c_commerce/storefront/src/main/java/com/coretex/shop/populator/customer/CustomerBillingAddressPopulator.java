
package com.coretex.shop.populator.customer;


import com.coretex.core.business.exception.ConversionException;
import com.coretex.core.populators.AbstractDataPopulator;
import com.coretex.items.cx_core.CustomerItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.core.LocaleItem;
import com.coretex.shop.model.customer.address.Address;

/**
 * @author csamson
 *
 */
public class CustomerBillingAddressPopulator extends AbstractDataPopulator<CustomerItem, Address> {

	@Override
	public Address populate(CustomerItem source, Address target, MerchantStoreItem store, LocaleItem language)
			throws ConversionException {

		target.setCity(source.getBilling().getCity());
		target.setFirstName(source.getBilling().getFirstName());
		target.setLastName(source.getBilling().getLastName());
		target.setPostalCode(source.getBilling().getPostalCode());
		target.setPhone(source.getBilling().getPhone());
		if (source.getBilling().getPhone() == null) {
			target.setPhone(source.getBilling().getPhone());
		}
		target.setAddress(source.getBilling().getAddressLine1());
		if (source.getBilling().getCountry() != null) {
			target.setCountry(source.getBilling().getCountry().getIsoCode());
		}
		if (source.getBilling().getZone() != null) {
			target.setZone(source.getBilling().getZone().getCode());
		}
		target.setStateProvince(source.getBilling().getState());

		return target;
	}

	@Override
	protected Address createTarget() {
		return new Address();
	}

}
