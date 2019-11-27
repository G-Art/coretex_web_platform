
package com.coretex.shop.populator.customer;

import com.coretex.items.core.LocaleItem;
import org.apache.commons.lang3.StringUtils;


import com.coretex.core.business.exception.ConversionException;
import com.coretex.core.populators.AbstractDataPopulator;
import com.coretex.items.commerce_core_model.CustomerItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.shop.model.customer.address.Address;


/**
 * @author Admin
 *
 */
public class CustomerDeliveryAddressPopulator extends AbstractDataPopulator<CustomerItem, Address> {

	@Override
	public Address populate(CustomerItem source, Address target, MerchantStoreItem store, LocaleItem language)
			throws ConversionException {

		if (source.getDelivery() != null) {
			if (StringUtils.isNotBlank(source.getDelivery().getCity())) {
				target.setCity(source.getDelivery().getCity());
			}

			if (StringUtils.isNotBlank(source.getDelivery().getCompany())) {
				target.setCompany(source.getDelivery().getCompany());
			}

			if (StringUtils.isNotBlank(source.getDelivery().getAddress())) {
				target.setAddress(source.getDelivery().getAddress());
			}

			if (StringUtils.isNotBlank(source.getDelivery().getFirstName())) {
				target.setFirstName(source.getDelivery().getFirstName());
			}

			if (StringUtils.isNotBlank(source.getDelivery().getLastName())) {
				target.setLastName(source.getDelivery().getLastName());
			}

			if (StringUtils.isNotBlank(source.getDelivery().getPostalCode())) {
				target.setPostalCode(source.getDelivery().getPostalCode());
			}

			if (StringUtils.isNotBlank(source.getDelivery().getTelephone())) {
				target.setPhone(source.getDelivery().getTelephone());
			}

			target.setStateProvince(source.getDelivery().getState());

			if (source.getDelivery().getTelephone() == null) {
				target.setPhone(source.getDelivery().getTelephone());
			}
			target.setAddress(source.getDelivery().getAddress());
			if (source.getDelivery().getCountry() != null) {
				target.setCountry(source.getDelivery().getCountry().getIsoCode());
			}
			if (source.getDelivery().getZone() != null) {
				target.setZone(source.getDelivery().getZone().getCode());
			}
		}
		return target;
	}

	@Override
	protected Address createTarget() {
		return new Address();
	}

}
