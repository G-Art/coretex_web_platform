package com.coretex.core.business.services.shipping;

import com.coretex.core.business.constants.ShippingConstants;
import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.business.services.reference.country.CountryService;
import com.coretex.core.business.services.reference.loader.ConfigurationModulesLoader;
import com.coretex.core.business.services.system.MerchantConfigurationService;
import com.coretex.core.model.shipping.ShippingConfiguration;
import com.coretex.core.model.shipping.ShippingMetaData;
import com.coretex.core.model.shipping.ShippingProduct;
import com.coretex.core.model.shipping.ShippingType;
import com.coretex.core.model.system.IntegrationConfiguration;
import com.coretex.core.business.utils.Encryption;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.items.commerce_core_model.MerchantConfigurationItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.core.CountryItem;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


@Service("shippingService")
public class ShippingServiceImpl implements ShippingService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ShippingServiceImpl.class);


	private final static String SUPPORTED_COUNTRIES = "SUPPORTED_CNTR";
	private final static String SHIPPING_MODULES = "SHIPPING";
	private final static String SHIPPING_DISTANCE = "shippingDistanceModule";

	@Resource
	private MerchantConfigurationService merchantConfigurationService;


	@Resource
	private CountryService countryService;


	@Resource
	private Encryption encryption;


	@Override
	public ShippingConfiguration getShippingConfiguration(MerchantStoreItem store) throws ServiceException {

		MerchantConfigurationItem configuration = merchantConfigurationService.getMerchantConfiguration(ShippingConstants.SHIPPING_CONFIGURATION, store);

		ShippingConfiguration shippingConfiguration = null;

		if (configuration != null) {
			String value = configuration.getValue();

			ObjectMapper mapper = new ObjectMapper();
			try {
				shippingConfiguration = mapper.readValue(value, ShippingConfiguration.class);
			} catch (Exception e) {
				throw new ServiceException("Cannot parse json string " + value);
			}
		}
		return shippingConfiguration;

	}

	@Override
	public void saveShippingConfiguration(ShippingConfiguration shippingConfiguration, MerchantStoreItem store) throws ServiceException {

		MerchantConfigurationItem configuration = merchantConfigurationService.getMerchantConfiguration(ShippingConstants.SHIPPING_CONFIGURATION, store);

		if (configuration == null) {
			configuration = new MerchantConfigurationItem();
			configuration.setMerchantStore(store);
			configuration.setKey(ShippingConstants.SHIPPING_CONFIGURATION);
		}

		String value = shippingConfiguration.toJSONString();
		configuration.setValue(value);
		merchantConfigurationService.saveOrUpdate(configuration);

	}


	@Override
	public void saveShippingQuoteModuleConfiguration(IntegrationConfiguration configuration, MerchantStoreItem store) throws ServiceException {

		try {
			Map<String, IntegrationConfiguration> modules = new HashMap<String, IntegrationConfiguration>();
			MerchantConfigurationItem merchantConfiguration = merchantConfigurationService.getMerchantConfiguration(SHIPPING_MODULES, store);
			if (merchantConfiguration != null) {
				if (!StringUtils.isBlank(merchantConfiguration.getValue())) {

					String decrypted = encryption.decrypt(merchantConfiguration.getValue());
					modules = ConfigurationModulesLoader.loadIntegrationConfigurations(decrypted);
				}
			} else {
				merchantConfiguration = new MerchantConfigurationItem();
				merchantConfiguration.setMerchantStore(store);
				merchantConfiguration.setKey(SHIPPING_MODULES);
			}
			modules.put(configuration.getModuleCode(), configuration);

			String configs = ConfigurationModulesLoader.toJSONString(modules);

			String encrypted = encryption.encrypt(configs);
			merchantConfiguration.setValue(encrypted);
			merchantConfigurationService.saveOrUpdate(merchantConfiguration);

		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}


	@Override
	public void removeShippingQuoteModuleConfiguration(String moduleCode, MerchantStoreItem store) throws ServiceException {


		try {
			Map<String, IntegrationConfiguration> modules = new HashMap<String, IntegrationConfiguration>();
			MerchantConfigurationItem merchantConfiguration = merchantConfigurationService.getMerchantConfiguration(SHIPPING_MODULES, store);
			if (merchantConfiguration != null) {
				if (!StringUtils.isBlank(merchantConfiguration.getValue())) {
					String decrypted = encryption.decrypt(merchantConfiguration.getValue());
					modules = ConfigurationModulesLoader.loadIntegrationConfigurations(decrypted);
				}

				modules.remove(moduleCode);
				String configs = ConfigurationModulesLoader.toJSONString(modules);
				String encrypted = encryption.encrypt(configs);
				merchantConfiguration.setValue(encrypted);
				merchantConfigurationService.saveOrUpdate(merchantConfiguration);


			}

			MerchantConfigurationItem configuration = merchantConfigurationService.getMerchantConfiguration(moduleCode, store);

			if (configuration != null) {//custom module

				merchantConfigurationService.delete(configuration);
			}


		} catch (Exception e) {
			throw new ServiceException(e);
		}

	}

	@Override
	public List<String> getSupportedCountries(MerchantStoreItem store) throws ServiceException {

		List<String> supportedCountries = new ArrayList<String>();
		MerchantConfigurationItem configuration = merchantConfigurationService.getMerchantConfiguration(SUPPORTED_COUNTRIES, store);

		if (configuration != null) {

			String countries = configuration.getValue();
			if (!StringUtils.isBlank(countries)) {

				Object objRegions = JSONValue.parse(countries);
				JSONArray arrayRegions = (JSONArray) objRegions;
				@SuppressWarnings("rawtypes")
				Iterator i = arrayRegions.iterator();
				while (i.hasNext()) {
					supportedCountries.add((String) i.next());
				}
			}

		}

		return supportedCountries;
	}

	@Override
	public List<CountryItem> getShipToCountryList(MerchantStoreItem store, LanguageItem language) throws ServiceException {


		ShippingConfiguration shippingConfiguration = getShippingConfiguration(store);
		ShippingType shippingType = ShippingType.INTERNATIONAL;
		List<String> supportedCountries = new ArrayList<String>();
		if (shippingConfiguration == null) {
			shippingConfiguration = new ShippingConfiguration();
		}

		if (shippingConfiguration.getShippingType() != null) {
			shippingType = shippingConfiguration.getShippingType();
		}


		if (shippingType.name().equals(ShippingType.NATIONAL.name())) {

			supportedCountries.add(store.getCountry().getIsoCode());

		} else {

			MerchantConfigurationItem configuration = merchantConfigurationService.getMerchantConfiguration(SUPPORTED_COUNTRIES, store);

			if (configuration != null) {

				String countries = configuration.getValue();
				if (!StringUtils.isBlank(countries)) {

					Object objRegions = JSONValue.parse(countries);
					JSONArray arrayRegions = (JSONArray) objRegions;
					@SuppressWarnings("rawtypes")
					Iterator i = arrayRegions.iterator();
					while (i.hasNext()) {
						supportedCountries.add((String) i.next());
					}
				}

			}

		}

		return countryService.getCountries(supportedCountries, language);

	}


	@Override
	public void setSupportedCountries(MerchantStoreItem store, List<String> countryCodes) throws ServiceException {


		//transform a list of string to json entry
		ObjectMapper mapper = new ObjectMapper();

		try {
			String value = mapper.writeValueAsString(countryCodes);

			MerchantConfigurationItem configuration = merchantConfigurationService.getMerchantConfiguration(SUPPORTED_COUNTRIES, store);

			if (configuration == null) {
				configuration = new MerchantConfigurationItem();
				configuration.
						setKey(SUPPORTED_COUNTRIES);
				configuration.setMerchantStore(store);
			}

			configuration.setValue(value);

			merchantConfigurationService.saveOrUpdate(configuration);

		} catch (Exception e) {
			throw new ServiceException(e);
		}

	}


	private BigDecimal calculateOrderTotal(List<ShippingProduct> products, MerchantStoreItem store) throws Exception {

		BigDecimal total = new BigDecimal(0);
		for (ShippingProduct shippingProduct : products) {
			BigDecimal currentPrice = shippingProduct.getFinalPrice().getFinalPrice();
			currentPrice = currentPrice.multiply(new BigDecimal(shippingProduct.getQuantity()));
			total = total.add(currentPrice);
		}


		return total;


	}

	@Override
	public ShippingMetaData getShippingMetaData(MerchantStoreItem store)
			throws ServiceException {

		ShippingMetaData metaData = new ShippingMetaData();

		// configured country
		List<CountryItem> countries = getShipToCountryList(store, store.getDefaultLanguage());
		metaData.setShipToCountry(countries);

		return metaData;
	}

	@Override
	public boolean hasTaxOnShipping(MerchantStoreItem store) throws ServiceException {
		ShippingConfiguration shippingConfiguration = getShippingConfiguration(store);
		return shippingConfiguration.isTaxOnShipping();
	}
}