package com.coretex.shop.populator.store;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.coretex.items.core.CountryItem;
import com.coretex.items.core.LocaleItem;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.CollectionUtils;

import com.coretex.core.business.exception.ConversionException;
import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.business.services.reference.country.CountryService;
import com.coretex.core.business.services.reference.zone.ZoneService;
import com.coretex.core.populators.AbstractDataPopulator;
import com.coretex.core.constants.MeasureUnit;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.ZoneItem;
import com.coretex.shop.model.content.ReadableImage;
import com.coretex.shop.model.references.ReadableAddress;
import com.coretex.shop.model.shop.ReadableMerchantStore;
import com.coretex.shop.utils.DateUtil;
import com.coretex.shop.utils.ImageFilePath;

/**
 * Populates MerchantStoreItem core entity model object
 *
 * @author carlsamson
 */
public class ReadableMerchantStorePopulator extends
		AbstractDataPopulator<MerchantStoreItem, ReadableMerchantStore> {

	protected final Log logger = LogFactory.getLog(getClass());

	private CountryService countryService;
	private ZoneService zoneService;
	private ImageFilePath filePath;


	@Override
	public ReadableMerchantStore populate(MerchantStoreItem source,
										  ReadableMerchantStore target, MerchantStoreItem store, LocaleItem language)
			throws ConversionException {
		Validate.notNull(countryService, "Must use setter for countryService");
		Validate.notNull(zoneService, "Must use setter for zoneService");

		target.setId(source.getUuid());
		target.setCode(source.getCode());
		if (source.getDefaultLanguage() != null) {
			target.setDefaultLanguage(source.getDefaultLanguage().getIso());
		}

		target.setCurrency(source.getCurrency().getCode());
		target.setPhone(source.getStorePhone());

		ReadableAddress address = new ReadableAddress();
		address.setAddress(source.getStoreAddress());
		address.setCity(source.getStoreCity());
		if (source.getCountry() != null) {
			try {
				address.setCountry(source.getCountry().getIsoCode());
				CountryItem c = countryService.getCountriesMap(language).get(source.getCountry().getIsoCode());
				if (c != null) {
					address.setCountry(c.getIsoCode());
				}
			} catch (ServiceException e) {
				logger.error("Cannot get CountryItem", e);
			}
		}

		target.setDimension(Objects.nonNull(source.getSeizeUnitCode()) ? MeasureUnit.valueOf(source.getSeizeUnitCode()) : MeasureUnit.CM );
		target.setWeight(Objects.nonNull(source.getWeightUnitCode()) ? MeasureUnit.valueOf(source.getWeightUnitCode()) : MeasureUnit.KG);

		if (source.getZone() != null) {
			address.setStateProvince(source.getZone().getCode());
			try {
				ZoneItem z = zoneService.getZones(language).get(source.getZone().getCode());
				address.setStateProvince(z.getCode());
			} catch (ServiceException e) {
				logger.error("Cannot get ZoneItem", e);
			}
		}


		if (!StringUtils.isBlank(source.getStoreStateProvince())) {
			address.setStateProvince(source.getStoreStateProvince());
		}

		if (!StringUtils.isBlank(source.getStoreLogo())) {
			ReadableImage image = new ReadableImage();
			image.setName(source.getStoreLogo());
			if (filePath != null) {
				image.setPath(filePath.buildStoreLogoFilePath(source));
			}
			target.setLogo(image);
		}

		address.setPostalCode(source.getStorePostalCode());

		target.setAddress(address);

		target.setCurrencyFormatNational(source.getCurrencyFormatNational());
		target.setEmail(source.getStoreEmailAddress());
		target.setName(source.getStoreName());
		target.setId(source.getUuid());
		target.setInBusinessSince(DateUtil.formatDate(source.getInBusinessSince()));
		target.setUseCache(source.getUseCache());


		List<LocaleItem> languages = source.getLanguages();
		if (!CollectionUtils.isEmpty(languages)) {

			List<String> langs = new ArrayList<String>();
			for (LocaleItem lang : languages) {
				langs.add(lang.getIso());
			}

			target.setSupportedLanguages(langs);
		}

		return target;
	}

	@Override
	protected ReadableMerchantStore createTarget() {
		// TODO Auto-generated method stub
		return null;
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

	public ImageFilePath getFilePath() {
		return filePath;
	}

	public void setFilePath(ImageFilePath filePath) {
		this.filePath = filePath;
	}


}
