package com.coretex.core.business.services.reference.init;

import com.coretex.core.activeorm.dao.LocaleDao;
import com.coretex.core.activeorm.services.ItemService;
import com.coretex.core.business.constants.Constants;
import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.business.services.catalog.product.manufacturer.ManufacturerService;
import com.coretex.core.business.services.catalog.product.type.ProductTypeService;
import com.coretex.core.business.services.merchant.MerchantStoreService;
import com.coretex.core.business.services.reference.country.CountryService;
import com.coretex.core.business.services.reference.currency.CurrencyService;
import com.coretex.core.business.services.reference.language.LanguageService;
import com.coretex.core.business.services.reference.loader.ShippingLoader;
import com.coretex.core.business.services.reference.loader.ZonesLoader;
import com.coretex.core.business.services.reference.zone.ZoneService;
import com.coretex.core.constants.SchemaConstant;
import com.coretex.items.commerce_core_model.CurrencyItem;
import com.coretex.items.commerce_core_model.DeliveryServiceItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.items.commerce_core_model.ManufacturerItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.ProductTypeItem;
import com.coretex.items.commerce_core_model.ZoneItem;
import com.coretex.items.core.CountryItem;
import com.coretex.items.core.LocaleItem;
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
	private LanguageService languageService;

	@Resource
	private CountryService countryService;

	@Resource
	private CurrencyService currencyService;

	@Resource
	protected MerchantStoreService merchantService;

	@Resource
	protected ProductTypeService productTypeService;

	@Resource
	private ZonesLoader zonesLoader;

	@Resource
	private ShippingLoader shippingLoader;

	@Resource
	private ManufacturerService manufacturerService;

	private String name;

	public boolean isEmpty() {
		return languageService.count() == 0;
	}

	@Transactional
	public void populate(String contextName) throws ServiceException {
		this.name = contextName;

		createLanguages();
		createCountries();
		createZones();
		createCurrencies();
		createSubReferences();
		createMerchant();
		createShipping();

	}

	private void createShipping() throws ServiceException {
		LOGGER.info(String.format("%s : Populating Shipping ", name));
		try {
			List<DeliveryServiceItem> serviceItems = shippingLoader.loadShippingConfig("reference/deliveryconfig.json");
			itemService.saveAll(serviceItems);
		} catch (Exception e) {
			throw new ServiceException(e);
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
		List<LanguageItem> languages = languageService.list();
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

	private void createZones() throws ServiceException {
		LOGGER.info(String.format("%s : Populating Zones ", name));
		try {

			Map<String, ZoneItem> zonesMap = zonesLoader.loadZones("reference/zoneconfig.json");

			for (Map.Entry<String, ZoneItem> entry : zonesMap.entrySet()) {
				ZoneItem value = entry.getValue();

				zoneService.create(value);

			}

		} catch (Exception e) {

			throw new ServiceException(e);
		}

	}

	private void createLanguages() {
		LOGGER.info(String.format("%s : Populating Languages ", name));
		for (String code : SchemaConstant.LANGUAGE_ISO_CODE) {
			var locale = LocaleUtils.toLocale(code);
			LanguageItem language = new LanguageItem();
			language.setCode(code);
			languageService.create(language);

			LocaleItem localeItem = new LocaleItem();
			localeItem.setIso(code);
			localeItem.setName(locale.getDisplayName());
			localeItem.setName(locale.getDisplayName(locale), locale);
			localeItem.setActive(true);
			localeDao.save(localeItem);
		}
	}

	private void createMerchant() throws ServiceException {
		LOGGER.info(String.format("%s : Creating merchant ", name));

		Date date = new Date(System.currentTimeMillis());

		LanguageItem en = languageService.getByCode("ru");
		CountryItem ca = countryService.getByCode("UA");
		CurrencyItem currency = currencyService.getByCode("UAH");
		ZoneItem qc = zoneService.getByCode("Kyiv");

		List<LanguageItem> supportedLanguages = new ArrayList<LanguageItem>();
		supportedLanguages.add(en);

		//create a merchant
		MerchantStoreItem store = new MerchantStoreItem();
		store.setCode("default");
		store.setCountry(ca);
		store.setCurrency(currency);
		store.setDefaultLanguage(en);
		store.setInBusinessSince(date);
		store.setZone(qc);
		store.setUseCache(true);
		store.setStoreName("G.O.O.D M.O.O.D");
		store.setStorePhone("888-888-8888");
		store.setCode(Constants.DEFAULT_STORE);
		store.setStoreCity("My city");
		store.setStoreAddress("1234 Street address");
		store.setStorePostalCode("H2H-2H2");
		store.setStoreEmailAddress("john@test.com");
		store.setDomainName("localhost:8008");
		store.setStoreTemplate("exoticamobilia");
		store.setLanguages(supportedLanguages);
		store.setCurrencyFormatNational(true);


		merchantService.create(store);


		//create default manufacturer
		ManufacturerItem defaultManufacturer = new ManufacturerItem();
		defaultManufacturer.setCode("DEFAULT");
		defaultManufacturer.setMerchantStore(store);
		defaultManufacturer.setName("DEFAULT");
		defaultManufacturer.setDescription("DEFAULT");

		manufacturerService.create(defaultManufacturer);


	}

	private void createSubReferences()  {

		LOGGER.info(String.format("%s : Loading catalog sub references ", name));

		ProductTypeItem productType = new ProductTypeItem();
		productType.setCode(Constants.GENERAL_TYPE);
		productTypeService.create(productType);

	}


}
