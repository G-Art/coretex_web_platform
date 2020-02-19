package com.coretex.commerce.admin.init.data;

import com.coretex.commerce.admin.init.data.loaders.ZonesLoader;
import com.coretex.commerce.core.services.CountryService;
import com.coretex.commerce.core.services.CurrencyService;
import com.coretex.commerce.core.services.LocaleService;
import com.coretex.commerce.core.services.ManufacturerService;
import com.coretex.commerce.core.services.ZoneService;
import com.coretex.core.activeorm.dao.LocaleDao;
import com.coretex.core.activeorm.services.ItemService;
import com.coretex.commerce.core.constants.Constants;
import com.coretex.items.core.CountryItem;
import com.coretex.items.core.LocaleItem;
import com.coretex.items.cx_core.AddressItem;
import com.coretex.items.cx_core.CurrencyItem;
import com.coretex.items.cx_core.ManufacturerItem;
import com.coretex.items.cx_core.StoreItem;
import com.coretex.items.cx_core.ZoneItem;
import org.apache.commons.lang3.LocaleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service("initializationDatabase")
public class InitializationDatabaseImpl implements InitializationDatabase {

	private static final Logger LOGGER = LoggerFactory.getLogger(InitializationDatabaseImpl.class);

	@Resource
	private ItemService itemService;

	@Resource
	private ZoneService zoneService;

	@Resource
	private LocaleDao localeDao;

	@Resource
	private LocaleService localeService;

	@Resource
	private CountryService countryService;

	@Resource
	private CurrencyService currencyService;

	@Resource
	private ZonesLoader zonesLoader;

//	@Resource
//	private ShippingLoader shippingLoader;

	@Resource
	private ManufacturerService manufacturerService;

	private String name;

	public boolean isEmpty() {
		return localeService.count() == 0;
	}

	@Transactional
	public void populate(String contextName)  {
		this.name = contextName;

		createLanguages();
		createCountries();
		createZones();
		createCurrencies();
		createMerchant();
		createShipping();

	}

	private void createShipping()  {
		LOGGER.info(String.format("%s : Populating Shipping ", name));
		try {
//			List<DeliveryServiceItem> serviceItems = shippingLoader.loadShippingConfig("reference/deliveryconfig.json");
//			itemService.saveAll(serviceItems);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}


	private void createCurrencies() {
		LOGGER.info(String.format("%s : Populating Currencies ", name));

		for (String code : SchemaConstant.CURRENCY_MAP.keySet()) {

			try {
				java.util.Currency c = java.util.Currency.getInstance(code);

				if (c == null) {
					LOGGER.info(String.format("%s : Populating Currencies : no currency for code : %s", name, code));
				}else{

					CurrencyItem currency = new CurrencyItem();
					currency.setCode(c.getCurrencyCode());
					currency.setName(c.getDisplayName());
					currency.setSymbol(c.getSymbol());
					currencyService.create(currency);
				}



				//System.out.println(l.getCountry() + "   " + c.getSymbol() + "  " + c.getSymbol(l));
			} catch (IllegalArgumentException e) {
				LOGGER.info(String.format("%s : Populating Currencies : no currency for code : %s", name, code));
			}
		}
	}

	private void createCountries() {
		LOGGER.info(String.format("%s : Populating Countries ", name));
		List<LocaleItem> languages = localeService.list();
		SchemaConstant.COUNTRY_CONFIG.forEach((k,v) -> {
			CountryItem country = new CountryItem();
			country.setIsoCode(k);
			country.setSupported(true);
			country.setActive(true);

			for (String langName : v.get("name").split(",")) {
				var split = langName.split(":");
				var loc = LocaleUtils.toLocale(split[0]);
				country.setName(split[1], loc);
			}


			for (String lang : v.get("languages").split(",")) {
				var loc = LocaleUtils.toLocale(lang);
				var localeItem = localeDao.findSingle(Map.of(LocaleItem.ISO, lang), true);
				if(Objects.isNull(localeItem)){
					localeItem = new LocaleItem();
					localeItem.setIso(lang);
					localeItem.setName(loc.getDisplayName(), loc);
					localeItem.setActive(true);
				}

				if(Objects.isNull(country.getDefaultLocale())){
					country.setDefaultLocale(localeItem);
				}
				country.getLocales().add(localeItem);
			}
			countryService.create(country);
		});

	}

	private void createZones()  {
		LOGGER.info(String.format("%s : Populating Zones ", name));
		try {

			Map<String, ZoneItem> zonesMap = zonesLoader.loadZones("reference/zoneconfig.json");

			for (Map.Entry<String, ZoneItem> entry : zonesMap.entrySet()) {
				ZoneItem value = entry.getValue();

				zoneService.create(value);

			}

		} catch (Exception e) {

			throw new RuntimeException(e);
		}

	}

	private void createLanguages() {
		LOGGER.info(String.format("%s : Populating Languages ", name));
		for (String code : SchemaConstant.LANGUAGE_ISO_CODE) {
			var locale = LocaleUtils.toLocale(code);

			LocaleItem localeItem = new LocaleItem();
			localeItem.setIso(code);
			localeItem.setName(locale.getDisplayName());
			localeItem.setName(locale.getDisplayName(locale), locale);
			localeItem.setActive(true);
			localeDao.save(localeItem);
		}
	}

	private void createMerchant()  {
		LOGGER.info(String.format("%s : Creating merchant ", name));

		Date date = new Date(System.currentTimeMillis());

		LocaleItem en = localeService.getByIso("en");
		LocaleItem ru = localeService.getByIso("ru");
		LocaleItem ua = localeService.getByIso("ua");
		CountryItem ca = countryService.getByCode("UA");
		CurrencyItem currency = currencyService.getByCode("UAH");
		ZoneItem qc = zoneService.getByCode("Kyiv");

		List<LocaleItem> supportedLanguages = new ArrayList<>();
		supportedLanguages.add(en);
		supportedLanguages.add(ru);
		supportedLanguages.add(ua);


		StoreItem s = new StoreItem();
		s.setCountry(ca);
		s.setCurrency(currency);
		s.setDefaultLanguage(en);
		s.setZone(qc);
		s.setName("G.O.O.D M.O.O.D");
		s.setPhone("+38(066) 666-66-66");
		s.setCode(Constants.DEFAULT_STORE);
		s.setStoreEmail("support@goodmood.market");
		s.setDomainName("goodmood.market");
		s.setLanguages(supportedLanguages);

		AddressItem address = new AddressItem();
		address.setPhone("+38(066) 666-66-66");
		address.setAddressLine1("1234 Street address");
		address.setCity("Kiev");
		address.setPostalCode("08122");

		s.setAddress(address);

		itemService.save(s);


		//create default manufacturer
		ManufacturerItem defaultManufacturer = new ManufacturerItem();
		defaultManufacturer.setCode("DEFAULT");
		defaultManufacturer.setStore(s);
		defaultManufacturer.setName("DEFAULT");
		defaultManufacturer.setDescription("DEFAULT");

		manufacturerService.save(defaultManufacturer);


	}

}
