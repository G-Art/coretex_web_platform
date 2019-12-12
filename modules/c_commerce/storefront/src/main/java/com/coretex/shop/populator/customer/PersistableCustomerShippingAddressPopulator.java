package com.coretex.shop.populator.customer;

import com.coretex.items.commerce_core_model.CustomerItem;
import org.apache.commons.lang3.StringUtils;

import com.coretex.items.commerce_core_model.DeliveryItem;
import com.coretex.core.business.exception.ConversionException;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.core.LocaleItem;
import com.coretex.shop.model.customer.address.Address;
import com.coretex.core.populators.AbstractDataPopulator;

public class PersistableCustomerShippingAddressPopulator extends AbstractDataPopulator<Address, CustomerItem> {

	@Override
	public CustomerItem populate(Address source, CustomerItem target, MerchantStoreItem store, LocaleItem language)
			throws ConversionException {


		if (target.getDelivery() == null) {

			DeliveryItem delivery = new DeliveryItem();
			delivery.setFirstName(source.getFirstName());
			delivery.setLastName(source.getLastName());

			if (StringUtils.isNotBlank(source.getAddress())) {
				delivery.setAddressLine1(source.getAddress());
			}

			if (StringUtils.isNotBlank(source.getCity())) {
				delivery.setCity(source.getCity());
			}

			if (StringUtils.isNotBlank(source.getPhone())) {
				delivery.setPhone(source.getPhone());
			}

			if (StringUtils.isNotBlank(source.getPostalCode())) {
				delivery.setPostalCode(source.getPostalCode());
			}

			if (StringUtils.isNotBlank(source.getStateProvince())) {
				delivery.setPostalCode(source.getStateProvince());
			}

			target.setDelivery(delivery);
		} else {
			target.getDelivery().setFirstName(source.getFirstName());
			target.getDelivery().setLastName(source.getLastName());

			// lets fill optional data now

			if (StringUtils.isNotBlank(source.getAddress())) {
				target.getDelivery().setAddressLine1(source.getAddress());
			}

			if (StringUtils.isNotBlank(source.getCity())) {
				target.getDelivery().setCity(source.getCity());
			}

			if (StringUtils.isNotBlank(source.getPhone())) {
				target.getDelivery().setPhone(source.getPhone());
			}

			if (StringUtils.isNotBlank(source.getPostalCode())) {
				target.getDelivery().setPostalCode(source.getPostalCode());
			}

			if (StringUtils.isNotBlank(source.getStateProvince())) {
				target.getDelivery().setPostalCode(source.getStateProvince());
			}
		}

		return target;

	}

	@Override
	protected CustomerItem createTarget() {
		return null;
	}


}
