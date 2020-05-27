package com.coretex.newpost.init.loader;

import com.coretex.commerce.core.constants.Constants;
import com.coretex.commerce.core.init.loader.DataLoader;
import com.coretex.commerce.core.services.CountryService;
import com.coretex.commerce.core.services.StoreService;
import com.coretex.commerce.payment.service.PaymentModeService;
import com.coretex.core.activeorm.services.ItemService;
import com.coretex.enums.newpost.DataFormatEnum;
import com.coretex.items.cx_commercedelivery_api.DeliveryServiceItem;
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
import java.util.stream.Collectors;

@Component
public class NewPostShippingLoader implements DataLoader {

	@Resource
	private ItemService itemService;

	@Resource
	private CountryService countryService;

	@Resource
	private StoreService storeService;

	@Resource
	private PaymentModeService paymentModeService;

	private static final Logger LOGGER = LoggerFactory.getLogger(NewPostShippingLoader.class);

	@Override
	public int priority() {
		return PRIORITY_MIN;
	}

	@Override
	public boolean parallel() {
		return true;
	}

	@Override
	public boolean load(String name) {
		LOGGER.info(String.format("%s : Populating New Post Shipping ", name));
		try {
		List<DeliveryServiceItem> deliveryItems = new ArrayList<>();
		ObjectMapper mapper = new ObjectMapper();
		try {
			InputStream in =
					this.getClass().getClassLoader().getResourceAsStream("config/deliveryconfig.json");

			@SuppressWarnings("rawtypes")
			Map[] objects = mapper.readValue(in, Map[].class);

			for (int i = 0; i < objects.length; i++) {
				deliveryItems.add(this.loadModule(objects[i]));
			}
			itemService.saveAll(deliveryItems);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return true;
	}


	@SuppressWarnings({"rawtypes", "unchecked"})
	private DeliveryServiceItem loadModule(Map object){

		NewPostDeliveryServiceItem serviceItem = new NewPostDeliveryServiceItem();

		serviceItem.setCode((String) object.get("code"));
		serviceItem.setApiKey((String) object.get("apiKey"));
		serviceItem.setEndpoint((String) object.get("endpoint"));
		serviceItem.setDataFormat(DataFormatEnum.fromString((String) object.get("dataFormat")));
		serviceItem.setSenderCity(UUID.fromString((String) object.get("senderCity")));
		serviceItem.setImage((String) object.get("image"));
		serviceItem.getStores().add(storeService.getByCode(Constants.DEFAULT_STORE));

		setLocalizedField("name", object, (locale, o) -> serviceItem.setName(String.valueOf(o), locale));

		var countries = (List<String>) object.get("countries");
		countries.forEach(iso -> serviceItem.getCountries().add(countryService.getByCode(iso)));

		List<Map> deliveryTypes = (List<Map>) object.get("deliveryTypes");

		if (deliveryTypes != null) {
			for (Map dt : deliveryTypes) {
				NewPostDeliveryTypeItem postDeliveryTypeItem = new NewPostDeliveryTypeItem();
				postDeliveryTypeItem.setCode((String) dt.get("code"));
				var payments = (List<String>) dt.get("payments");
				postDeliveryTypeItem.setPaymentModes(payments
						.stream()
						.map(paymentModeService::getByCode)
						.collect(Collectors.toSet()));
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
