package com.coretex.commerce.admin.init.data.loaders;

import com.coretex.commerce.core.constants.Constants;
import com.coretex.commerce.core.init.loader.DataLoader;
import com.coretex.commerce.core.services.CountryService;
import com.coretex.commerce.core.services.CurrencyService;
import com.coretex.commerce.core.services.LocaleService;
import com.coretex.commerce.core.services.ManufacturerService;
import com.coretex.commerce.core.services.ZoneService;
import com.coretex.core.activeorm.services.ItemService;
import com.coretex.items.core.CountryItem;
import com.coretex.items.core.LocaleItem;
import com.coretex.items.cx_core.AddressItem;
import com.coretex.items.cx_core.CurrencyItem;
import com.coretex.items.cx_core.ManufacturerItem;
import com.coretex.items.cx_core.StoreItem;
import com.coretex.items.cx_core.ZoneItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component
public class MerchantsLoader implements DataLoader {

	private static final Logger LOGGER = LoggerFactory.getLogger(MerchantsLoader.class);

	@Resource
	private ItemService itemService;

	@Resource
	private LocaleService localeService;

	@Resource
	private CountryService countryService;

	@Resource
	private CurrencyService currencyService;

	@Resource
	private ZoneService zoneService;

	@Resource
	private ManufacturerService manufacturerService;


	@Override
	public int priority() {
		return PRIORITY_MIN;
	}

	@Override
	public boolean load(String name) {
		LOGGER.info(String.format("%s : Creating merchant ", name));
		creteGoodMoodStore();
		return false;
	}

	private void creteGoodMoodStore() {

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
