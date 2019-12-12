package com.coretex.shop.populator.customer;

import com.coretex.core.business.exception.ConversionException;
import com.coretex.core.populators.AbstractDataPopulator;
import com.coretex.items.commerce_core_model.CustomerItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.core.LocaleItem;
import com.coretex.shop.model.customer.PersistableCustomer;
import com.coretex.shop.model.customer.address.Address;

public class PersistableCustomerPopulator extends
		AbstractDataPopulator<CustomerItem, PersistableCustomer> {

	@Override
	public PersistableCustomer populate(CustomerItem source,
										PersistableCustomer target, MerchantStoreItem store, LocaleItem language)
			throws ConversionException {


		try {


			if (source.getBilling() != null) {
				Address address = new Address();
				address.setCity(source.getBilling().getCity());
				address.setFirstName(source.getBilling().getFirstName());
				address.setLastName(source.getBilling().getLastName());
				address.setPostalCode(source.getBilling().getPostalCode());
				address.setPhone(source.getBilling().getPhone());
				if (source.getBilling().getPhone() == null) {
					address.setPhone(source.getBilling().getPhone());
				}
				address.setAddress(source.getBilling().getAddressLine1());
				if (source.getBilling().getCountry() != null) {
					address.setCountry(source.getBilling().getCountry().getIsoCode());
				}
				if (source.getBilling().getZone() != null) {
					address.setZone(source.getBilling().getZone().getCode());
				}

				target.setBilling(address);
			}

			target.setProvider(source.getProvider());

			if (source.getCustomerReviewAvg() != null) {
				target.setRating(source.getCustomerReviewAvg().doubleValue());
			}

			if (source.getCustomerReviewCount() != null) {
				target.setRatingCount(source.getCustomerReviewCount().intValue());
			}

			if (source.getDelivery() != null) {
				Address address = new Address();
				address.setAddress(source.getDelivery().getAddressLine1());
				address.setCity(source.getDelivery().getCity());
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

			target.setUuid(source.getUuid());
			target.setEmailAddress(source.getEmail());
			if (source.getGender() != null) {
				target.setGender(source.getGender().name());
			}
			if (source.getDefaultLanguage() != null) {
				target.setLanguage(source.getDefaultLanguage().getIso());
			}
			target.setUserName(source.getFirstName());
			target.setStoreCode(store.getCode());
			if (source.getDefaultLanguage() != null) {
				target.setLanguage(source.getDefaultLanguage().getIso());
			} else {
				target.setLanguage(store.getDefaultLanguage().getIso());
			}


		} catch (Exception e) {
			throw new ConversionException(e);
		}

		return target;

	}

	@Override
	protected PersistableCustomer createTarget() {
		// TODO Auto-generated method stub
		return null;
	}

}
