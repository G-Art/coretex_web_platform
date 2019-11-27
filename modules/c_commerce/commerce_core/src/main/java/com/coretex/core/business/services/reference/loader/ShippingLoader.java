package com.coretex.core.business.services.reference.loader;

import com.coretex.core.business.constants.Constants;
import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.business.services.merchant.MerchantStoreService;
import com.coretex.core.business.services.reference.country.CountryService;
import com.coretex.core.business.services.reference.country.CountryServiceImpl;
import com.coretex.core.model.system.ModuleConfig;
import com.coretex.enums.newpost.DataFormatEnum;
import com.coretex.items.commerce_core_model.DeliveryServiceItem;
import com.coretex.items.newpost.NewPostDeliveryServiceItem;
import com.coretex.items.newpost.NewPostDeliveryTypeItem;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.LocaleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;

@Component
public class ShippingLoader {

	private static final Logger LOGGER = LoggerFactory.getLogger(ShippingLoader.class);

	@Resource
	private CountryService countryService;

	@Resource
	private MerchantStoreService merchantStoreService;


	public List<DeliveryServiceItem> loadShippingConfig(String jsonFilePath) throws ServiceException {


		List<DeliveryServiceItem> deliveryItems = new ArrayList<>();

		ObjectMapper mapper = new ObjectMapper();

		try {

			InputStream in =
					this.getClass().getClassLoader().getResourceAsStream(jsonFilePath);


			@SuppressWarnings("rawtypes")
			Map[] objects = mapper.readValue(in, Map[].class);

			for (int i = 0; i < objects.length; i++) {

				deliveryItems.add(this.loadModule(objects[i]));
			}

			return deliveryItems;

		} catch (Exception e) {
			throw new ServiceException(e);
		}


	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	private DeliveryServiceItem loadModule(Map object) throws Exception {

		ObjectMapper mapper = new ObjectMapper();
		NewPostDeliveryServiceItem serviceItem = new NewPostDeliveryServiceItem();

		serviceItem.setCode((String) object.get("code"));
		serviceItem.setApiKey((String) object.get("apiKey"));
		serviceItem.setEndpoint((String) object.get("endpoint"));
		serviceItem.setDataFormat(DataFormatEnum.fromString((String) object.get("dataFormat")));
		serviceItem.setSenderCity(UUID.fromString((String) object.get("senderCity")));
		serviceItem.setImage((String) object.get("image"));
		serviceItem.setStore(merchantStoreService.getByCode(Constants.DEFAULT_STORE));

		setLocalizedField("name", object, (locale, o) -> serviceItem.setName(String.valueOf(o), locale));

		var countries = (List<String>) object.get("countries");
		countries.forEach(iso -> serviceItem.getCountries().add(countryService.getByCode(iso)));

		List<Map> deliveryTypes = (List<Map>) object.get("deliveryTypes");

		if (deliveryTypes != null) {
			for (Map dt : deliveryTypes) {
				NewPostDeliveryTypeItem postDeliveryTypeItem = new NewPostDeliveryTypeItem();
				postDeliveryTypeItem.setCode((String) dt.get("code"));
				postDeliveryTypeItem.setPayOnDelivery((Boolean) dt.get("payOnDelivery"));
				postDeliveryTypeItem.setSendToWarehouse((Boolean) dt.get("sendToWarehouse"));
				postDeliveryTypeItem.setActive((Boolean) dt.get("active"));

				setLocalizedField("name", dt, (locale, o) -> postDeliveryTypeItem.setName(String.valueOf(o), locale));
				serviceItem.getDeliveryTypes().add(postDeliveryTypeItem);
			}
		}

		return serviceItem;


	}

	private void setLocalizedField(String fieldName, Map object, BiConsumer<Locale, Object> biConsumer) {
		List<Map> locales = (List<Map>) object.get(fieldName);
		for (Map m : locales) {
			var lang = (String) m.get("lang");
			var value = (String) m.get("value");
			biConsumer.accept(LocaleUtils.toLocale(lang), value);
		}
	}

}
