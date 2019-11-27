
package com.coretex.shop.populator.customer;

import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.core.LocaleItem;
import org.apache.commons.lang3.StringUtils;


import com.coretex.core.business.exception.ConversionException;
import com.coretex.core.populators.AbstractDataPopulator;
import com.coretex.items.commerce_core_model.CustomerItem;
import com.coretex.shop.model.customer.CustomerEntity;
import com.coretex.shop.model.customer.address.Address;


/**
 * <p>
 * CustomerEntityPopulator will help to populate {@link CustomerEntity} from {@link CustomerItem} CustomerEntity will be
 * used to show data on the UI side.
 * </p>
 *
 * @author Umesh Awasthi
 * @version 1.2
 */
public class CustomerEntityPopulator
		extends AbstractDataPopulator<CustomerItem, CustomerEntity> {

	@Override
	public CustomerEntity populate(final CustomerItem source, final CustomerEntity target,
								   final MerchantStoreItem merchantStore, final LocaleItem language)
			throws ConversionException {
		try {


			target.setUuid(source.getUuid());
			if (StringUtils.isNotBlank(source.getEmail())) {
				target.setEmailAddress(source.getEmail());
			}


			if (source.getBilling() != null) {
				Address address = new Address();
				address.setCity(source.getBilling().getCity());
				address.setAddress(source.getBilling().getAddress());
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

				address.setStateProvince(source.getBilling().getState());

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

				address.setStateProvince(source.getDelivery().getState());

				target.setDelivery(address);
			}

		} catch (Exception e) {
			throw new ConversionException(e);
		}

		return target;
	}

	@Override
	protected CustomerEntity createTarget() {
		return new CustomerEntity();
	}

}
