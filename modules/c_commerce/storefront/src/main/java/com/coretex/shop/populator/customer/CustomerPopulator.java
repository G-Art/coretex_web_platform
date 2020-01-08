package com.coretex.shop.populator.customer;


import com.coretex.core.business.exception.ConversionException;
import com.coretex.core.business.services.reference.country.CountryService;
import com.coretex.core.business.services.reference.language.LanguageService;
import com.coretex.core.business.services.reference.zone.ZoneService;
import com.coretex.core.business.services.user.GroupService;
import com.coretex.core.populators.AbstractDataPopulator;
import com.coretex.items.commerce_core_model.BillingItem;
import com.coretex.items.commerce_core_model.DeliveryItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.core.CountryItem;
import com.coretex.items.core.LocaleItem;
import com.coretex.items.cx_core.CustomerItem;
import com.coretex.items.cx_core.ZoneItem;
import com.coretex.shop.model.customer.PersistableCustomer;
import com.coretex.shop.model.customer.address.Address;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class CustomerPopulator extends
		AbstractDataPopulator<PersistableCustomer, CustomerItem> {

	protected static final Logger LOG = LoggerFactory.getLogger(CustomerPopulator.class);
	private CountryService countryService;
	private ZoneService zoneService;
	private LanguageService languageService;

	private GroupService groupService;


	/**
	 * Creates a CustomerItem entity ready to be saved
	 */
	@Override
	public CustomerItem populate(PersistableCustomer source, CustomerItem target,
								 MerchantStoreItem store, LocaleItem language) throws ConversionException {

		Validate.notNull(zoneService, "Requires to set ZoneService");
		Validate.notNull(countryService, "Requires to set CountryService");
		Validate.notNull(languageService, "Requires to set LanguageService");
		Validate.notNull(groupService, "Requires to set GroupService");

		try {

			if (source.getUuid() != null) {
				target.setUuid(source.getUuid());
			}


			if (!StringUtils.isBlank(source.getEncodedPassword())) {
				target.setPassword(source.getEncodedPassword());
				target.setAnonymous(false);
			}

			target.setEmail(source.getEmailAddress());
			target.setFirstName(source.getUserName());

			Map<String, CountryItem> countries = countryService.getCountriesMap(language);
			Map<String, ZoneItem> zones = zoneService.getZones(language);

			Address sourceBilling = source.getBilling();
			if (sourceBilling != null) {
				BillingItem billing = new BillingItem();
				billing.setAddressLine1(sourceBilling.getAddress());
				billing.setCity(sourceBilling.getCity());
				//billing.setCountry(country);
				billing.setFirstName(sourceBilling.getFirstName());
				billing.setLastName(sourceBilling.getLastName());
				billing.setPhone(sourceBilling.getPhone());
				billing.setPostalCode(sourceBilling.getPostalCode());
				billing.setState(sourceBilling.getStateProvince());
				CountryItem billingCountry = null;
				if (!StringUtils.isBlank(sourceBilling.getCountry())) {
					billingCountry = countries.get(sourceBilling.getCountry());
					if (billingCountry == null) {
						throw new ConversionException("Unsuported country code " + sourceBilling.getCountry());
					}
					billing.setCountry(billingCountry);
				}

				if (billingCountry != null && !StringUtils.isBlank(sourceBilling.getZone())) {
					ZoneItem zone = zoneService.getByCode(sourceBilling.getZone());
					if (zone == null) {
						throw new ConversionException("Unsuported zone code " + sourceBilling.getZone());
					}
					ZoneItem zoneDescription = zones.get(zone.getCode());
					billing.setZone(zoneDescription);
				}
				target.setBilling(billing);

			}
			if (target.getBilling() == null && source.getBilling() != null) {
				LOG.info("Setting default values for billing");
				BillingItem billing = new BillingItem();
				CountryItem billingCountry = null;
				if (StringUtils.isNotBlank(source.getBilling().getCountry())) {
					billingCountry = countries.get(source.getBilling().getCountry());
					if (billingCountry == null) {
						throw new ConversionException("Unsuported country code " + sourceBilling.getCountry());
					}
					billing.setCountry(billingCountry);
					target.setBilling(billing);
				}
			}
			Address sourceShipping = source.getDelivery();
			if (sourceShipping != null) {
				DeliveryItem delivery = new DeliveryItem();
				delivery.setAddressLine1(sourceShipping.getAddress());
				delivery.setCity(sourceShipping.getCity());
				delivery.setFirstName(sourceShipping.getFirstName());
				delivery.setLastName(sourceShipping.getLastName());
				delivery.setPhone(sourceShipping.getPhone());
				delivery.setPostalCode(sourceShipping.getPostalCode());
				delivery.setState(sourceShipping.getStateProvince());
				CountryItem deliveryCountry = null;


				if (!StringUtils.isBlank(sourceShipping.getCountry())) {
					deliveryCountry = countries.get(sourceShipping.getCountry());
					if (deliveryCountry == null) {
						throw new ConversionException("Unsuported country code " + sourceShipping.getCountry());
					}
					delivery.setCountry(deliveryCountry);
				}

				if (deliveryCountry != null && !StringUtils.isBlank(sourceShipping.getZone())) {
					ZoneItem zone = zoneService.getByCode(sourceShipping.getZone());
					if (zone == null) {
						throw new ConversionException("Unsuported zone code " + sourceShipping.getZone());
					}
					ZoneItem zoneDescription = zones.get(zone.getCode());
					delivery.setZone(zoneDescription);
				}
				target.setDelivery(delivery);
			}


			if (target.getDelivery() == null && source.getDelivery() != null) {
				LOG.info("Setting default value for delivery");
				DeliveryItem delivery = new DeliveryItem();
				CountryItem deliveryCountry = null;
				if (StringUtils.isNotBlank(source.getDelivery().getCountry())) {
					deliveryCountry = countries.get(source.getDelivery().getCountry());
					if (deliveryCountry == null) {
						throw new ConversionException("Unsuported country code " + sourceShipping.getCountry());
					}
					delivery.setCountry(deliveryCountry);
					target.setDelivery(delivery);
				}
			}

			if (target.getDefaultLanguage() == null) {
				target.setDefaultLanguage(target.getStore().getDefaultLanguage());
			}


		} catch (Exception e) {
			throw new ConversionException(e);
		}


		return target;
	}

	@Override
	protected CustomerItem createTarget() {
		return new CustomerItem();
	}


	public CountryService getCountryService() {
		return countryService;
	}

	public void setCountryService(CountryService countryService) {
		this.countryService = countryService;
	}

	public ZoneService getZoneService() {
		return zoneService;
	}

	public void setZoneService(ZoneService zoneService) {
		this.zoneService = zoneService;
	}

	public LanguageService getLanguageService() {
		return languageService;
	}

	public void setLanguageService(LanguageService languageService) {
		this.languageService = languageService;
	}

	public GroupService getGroupService() {
		return groupService;
	}

	public void setGroupService(GroupService groupService) {
		this.groupService = groupService;
	}

}
