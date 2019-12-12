package com.coretex.shop.populator.customer;

import com.coretex.core.business.exception.ConversionException;
import com.coretex.core.populators.AbstractDataPopulator;
import com.coretex.items.commerce_core_model.CustomerItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.core.LocaleItem;
import com.coretex.shop.model.customer.ReadableCustomer;
import com.coretex.shop.model.customer.address.Address;
public class ReadableCustomerPopulator extends
		AbstractDataPopulator<CustomerItem, ReadableCustomer> {


	@Override
	public ReadableCustomer populate(CustomerItem source, ReadableCustomer target,
									 MerchantStoreItem store, LocaleItem language) throws ConversionException {

		try {

			if (target == null) {
				target = new ReadableCustomer();
			}

			if (source.getUuid() != null) {
				target.setUuid(source.getUuid());
			}
			target.setEmailAddress(source.getEmail());
			if (source.getBilling() != null) {
				Address address = new Address();
				address.setAddress(source.getBilling().getAddressLine1());
				address.setCity(source.getBilling().getCity());
				address.setFirstName(source.getBilling().getFirstName());
				address.setLastName(source.getBilling().getLastName());
				address.setPostalCode(source.getBilling().getPostalCode());
				address.setPhone(source.getBilling().getPhone());
				if (source.getBilling().getCountry() != null) {
					address.setCountry(source.getBilling().getCountry().getIsoCode());
				}
				if (source.getBilling().getZone() != null) {
					address.setZone(source.getBilling().getZone().getCode());
				}

				target.setFirstName(address.getFirstName());
				target.setLastName(address.getLastName());

				target.setBilling(address);
			}

			if (source.getCustomerReviewAvg() != null) {
				target.setRating(source.getCustomerReviewAvg().doubleValue());
			}

			if (source.getCustomerReviewCount() != null) {
				target.setRatingCount(source.getCustomerReviewCount().intValue());
			}

			if (source.getDelivery() != null) {
				Address address = new Address();
				address.setCity(source.getDelivery().getCity());
				address.setAddress(source.getDelivery().getAddressLine1());
				address.setFirstName(source.getDelivery().getFirstName());
				address.setLastName(source.getDelivery().getLastName());
				address.setPostalCode(source.getDelivery().getPostalCode());
				address.setPhone(source.getDelivery().getPhone());
				if (source.getDelivery().getCountry() != null) {
					address.setCountry(source.getDelivery().getCountry().getIsoCode());
				}
				if (source.getDelivery().getZone() != null) {
					address.setZone(source.getDelivery().getZone().getCode());
				}

				target.setDelivery(address);
			}

		} catch (Exception e) {
			throw new ConversionException(e);
		}

		return target;
	}

	@Override
	protected ReadableCustomer createTarget() {
		return null;
	}

}
