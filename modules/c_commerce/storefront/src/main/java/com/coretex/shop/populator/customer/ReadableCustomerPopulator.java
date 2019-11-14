package com.coretex.shop.populator.customer;

import com.coretex.core.business.exception.ConversionException;
import com.coretex.core.populators.AbstractDataPopulator;
import com.coretex.items.commerce_core_model.CustomerItem;
import com.coretex.items.commerce_core_model.CustomerAttributeItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.shop.model.customer.ReadableCustomer;
import com.coretex.shop.model.customer.address.Address;
import com.coretex.shop.model.customer.attribute.CustomerOptionDescription;
import com.coretex.shop.model.customer.attribute.CustomerOptionValueDescription;
import com.coretex.shop.model.customer.attribute.ReadableCustomerAttribute;
import com.coretex.shop.model.customer.attribute.ReadableCustomerOption;
import com.coretex.shop.model.customer.attribute.ReadableCustomerOptionValue;

public class ReadableCustomerPopulator extends
		AbstractDataPopulator<CustomerItem, ReadableCustomer> {


	@Override
	public ReadableCustomer populate(CustomerItem source, ReadableCustomer target,
									 MerchantStoreItem store, LanguageItem language) throws ConversionException {

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
				address.setAddress(source.getBilling().getAddress());
				address.setCity(source.getBilling().getCity());
				address.setCompany(source.getBilling().getCompany());
				address.setFirstName(source.getBilling().getFirstName());
				address.setLastName(source.getBilling().getLastName());
				address.setPostalCode(source.getBilling().getPostalCode());
				address.setPhone(source.getBilling().getTelephone());
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
				address.setAddress(source.getDelivery().getAddress());
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

			if (source.getAttributes() != null) {
				for (CustomerAttributeItem attribute : source.getAttributes()) {
					ReadableCustomerAttribute readableAttribute = new ReadableCustomerAttribute();
					readableAttribute.setUuid(attribute.getUuid());
					ReadableCustomerOption option = new ReadableCustomerOption();
					option.setUuid(attribute.getCustomerOption().getUuid());
					option.setCode(attribute.getCustomerOption().getCode());

					CustomerOptionDescription d = new CustomerOptionDescription();
					d.setDescription(attribute.getCustomerOption().getDescription());
					d.setName(attribute.getCustomerOption().getName());
					option.setDescription(d);

					readableAttribute.setCustomerOption(option);

					ReadableCustomerOptionValue optionValue = new ReadableCustomerOptionValue();
					optionValue.setUuid(attribute.getCustomerOptionValue().getUuid());
					CustomerOptionValueDescription vd = new CustomerOptionValueDescription();
					vd.setDescription(attribute.getCustomerOptionValue().getDescription());
					vd.setName(attribute.getCustomerOptionValue().getName());
					optionValue.setCode(attribute.getCustomerOptionValue().getCode());
					optionValue.setDescription(vd);


					readableAttribute.setCustomerOptionValue(optionValue);
					target.getAttributes().add(readableAttribute);
				}
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
