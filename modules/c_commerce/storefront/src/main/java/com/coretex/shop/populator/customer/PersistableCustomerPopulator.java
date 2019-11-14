package com.coretex.shop.populator.customer;

import com.coretex.core.business.exception.ConversionException;
import com.coretex.core.populators.AbstractDataPopulator;
import com.coretex.items.commerce_core_model.CustomerItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.shop.model.customer.PersistableCustomer;
import com.coretex.shop.model.customer.address.Address;

public class PersistableCustomerPopulator extends
		AbstractDataPopulator<CustomerItem, PersistableCustomer> {

	@Override
	public PersistableCustomer populate(CustomerItem source,
										PersistableCustomer target, MerchantStoreItem store, LanguageItem language)
			throws ConversionException {


		try {


			if (source.getBilling() != null) {
				Address address = new Address();
				address.setCity(source.getBilling().getCity());
				address.setCompany(source.getBilling().getCompany());
				address.setFirstName(source.getBilling().getFirstName());
				address.setLastName(source.getBilling().getLastName());
				address.setPostalCode(source.getBilling().getPostalCode());
				address.setPhone(source.getBilling().getTelephone());
				if (source.getBilling().getTelephone() == null) {
					address.setPhone(source.getBilling().getTelephone());
				}
				address.setAddress(source.getBilling().getAddress());
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
				address.setAddress(source.getDelivery().getAddress());
				address.setCity(source.getDelivery().getCity());
				address.setCompany(source.getDelivery().getCompany());
				address.setFirstName(source.getDelivery().getFirstName());
				address.setLastName(source.getDelivery().getLastName());
				address.setPostalCode(source.getDelivery().getPostalCode());
				address.setPhone(source.getDelivery().getTelephone());
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
				target.setLanguage(source.getDefaultLanguage().getCode());
			}
			target.setUserName(source.getFirstName());
			target.setStoreCode(store.getCode());
			if (source.getDefaultLanguage() != null) {
				target.setLanguage(source.getDefaultLanguage().getCode());
			} else {
				target.setLanguage(store.getDefaultLanguage().getCode());
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
