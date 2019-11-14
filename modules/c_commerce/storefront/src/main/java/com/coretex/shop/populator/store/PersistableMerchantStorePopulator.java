package com.coretex.shop.populator.store;

import java.util.List;

import javax.annotation.Resource;

import com.coretex.items.commerce_core_model.CurrencyItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.items.commerce_core_model.ZoneItem;
import org.apache.commons.collections4.CollectionUtils;
import org.drools.core.util.StringUtils;
import org.jsoup.helper.Validate;
import org.springframework.stereotype.Component;

import com.coretex.core.business.constants.Constants;
import com.coretex.core.business.exception.ConversionException;
import com.coretex.core.business.services.reference.country.CountryService;
import com.coretex.core.business.services.reference.currency.CurrencyService;
import com.coretex.core.business.services.reference.language.LanguageService;
import com.coretex.core.business.services.reference.zone.ZoneService;
import com.coretex.core.populators.AbstractDataPopulator;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.core.CountryItem;
import com.coretex.shop.model.references.PersistableAddress;
import com.coretex.shop.model.shop.PersistableMerchantStore;

@Component
public class PersistableMerchantStorePopulator extends AbstractDataPopulator<PersistableMerchantStore, MerchantStoreItem> {

	@Resource
	private CountryService countryService;
	@Resource
	private ZoneService zoneService;
	@Resource
	private LanguageService languageService;
	@Resource
	private CurrencyService currencyService;


	@Override
	public MerchantStoreItem populate(PersistableMerchantStore source, MerchantStoreItem target, MerchantStoreItem store,
									  LanguageItem language) throws ConversionException {

		Validate.notNull(source, "PersistableMerchantStore mst not be null");

		if (target == null) {
			target = new MerchantStoreItem();
		}

		target.setCode(source.getCode());
		target.setUuid(source.getId());
		target.setDateBusinessSince(source.getInBusinessSince());
		if (source.getDimension() != null) {
			target.setSeizeUnitCode(source.getDimension().name());
		}
		if (source.getWeight() != null) {
			target.setWeightUnitCode(source.getWeight().name());
		}
		target.setCurrencyFormatNational(source.isCurrencyFormatNational());
		target.setStoreName(source.getName());
		target.setStorePhone(source.getPhone());
		target.setStoreEmailAddress(source.getEmail());
		target.setUseCache(source.isUseCache());

		try {

			if (!StringUtils.isEmpty(source.getDefaultLanguage())) {
				LanguageItem l = languageService.getByCode(source.getDefaultLanguage());
				target.setDefaultLanguage(l);
			}

			if (!StringUtils.isEmpty(source.getCurrency())) {
				CurrencyItem c = currencyService.getByCode(source.getCurrency());
				target.setCurrency(c);
			} else {
				target.setCurrency(currencyService.getByCode(Constants.DEFAULT_CURRENCY.getCurrencyCode()));
			}

			List<String> languages = source.getSupportedLanguages();
			if (!CollectionUtils.isEmpty(languages)) {
				for (String lang : languages) {
					LanguageItem ll = languageService.getByCode(lang);
					target.getLanguages().add(ll);
				}
			}

		} catch (Exception e) {
			throw new ConversionException(e);
		}

		//address population
		PersistableAddress address = source.getAddress();
		if (address != null) {
			CountryItem country;
			try {
				country = countryService.getByCode(address.getCountry());

				ZoneItem zone = zoneService.getByCode(address.getStateProvince());
				if (zone != null) {
					target.setZone(zone);
				} else {
					target.setStoreStateProvince(address.getStateProvince());
				}

				target.setStoreAddress(address.getAddress());
				target.setStoreCity(address.getCity());
				target.setCountry(country);
				target.setStorePostalCode(address.getPostalCode());

			} catch (Exception e) {
				throw new ConversionException(e);
			}
		}

		return target;
	}

	@Override
	protected MerchantStoreItem createTarget() {
		// TODO Auto-generated method stub
		return null;
	}

	public ZoneService getZoneService() {
		return zoneService;
	}

	public void setZoneService(ZoneService zoneService) {
		this.zoneService = zoneService;
	}

	public CountryService getCountryService() {
		return countryService;
	}

	public void setCountryService(CountryService countryService) {
		this.countryService = countryService;
	}

	public LanguageService getLanguageService() {
		return languageService;
	}

	public void setLanguageService(LanguageService languageService) {
		this.languageService = languageService;
	}

	public CurrencyService getCurrencyService() {
		return currencyService;
	}

	public void setCurrencyService(CurrencyService currencyService) {
		this.currencyService = currencyService;
	}


}
