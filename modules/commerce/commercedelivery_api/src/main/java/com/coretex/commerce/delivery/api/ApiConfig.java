package com.coretex.commerce.delivery.api;

import com.coretex.commerce.delivery.api.actions.AddDeliveryInfoAction;
import com.coretex.commerce.delivery.api.actions.AdditionalInfoAction;
import com.coretex.commerce.delivery.api.actions.AddressAdditionalInfoAction;
import com.coretex.commerce.delivery.api.actions.DeliveryTypeActionHandler;
import com.coretex.commerce.delivery.api.actions.impl.DefaultDeliveryTypeActionHandler;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
@SuppressWarnings("unchecked")
public class ApiConfig {
	private final Logger LOG = LoggerFactory.getLogger(this.getClass());

	@Autowired(required = false)
	private List<AdditionalInfoAction> additionalInfoActions;
	@Autowired(required = false)
	private List<AddDeliveryInfoAction> addDeliveryInfoActions;
	@Autowired(required = false)
	private List<AddressAdditionalInfoAction> addressAdditionalInfoActions;

	@Bean
	public DeliveryTypeActionHandler createDeliveryTypeActionHandler(){
		DefaultDeliveryTypeActionHandler deliveryTypeActionHandler = new DefaultDeliveryTypeActionHandler();
		LOG.info("Creating DeliveryTypeActionHandler");

		if(CollectionUtils.isNotEmpty(additionalInfoActions)){
			LOG.info("Loading [additionalInfoActions]");
			deliveryTypeActionHandler.setAdditionalInfoActionMap(additionalInfoActions.stream()
					.collect(Collectors.toMap(AdditionalInfoAction::deliveryType, Function.identity())));
		}
		if(CollectionUtils.isNotEmpty(additionalInfoActions)){
			LOG.info("Loading [additionalInfoActions]");
			deliveryTypeActionHandler.setAddDeliveryInfoActionMap(addDeliveryInfoActions.stream()
					.collect(Collectors.toMap(AddDeliveryInfoAction::deliveryType, Function.identity())));
		}

		if(CollectionUtils.isNotEmpty(addressAdditionalInfoActions)){
			LOG.info("Loading [addressAdditionalInfoActions]");
			deliveryTypeActionHandler.setAddressAdditionalInfoActionMap(addressAdditionalInfoActions.stream()
					.collect(Collectors.toMap(AddressAdditionalInfoAction::deliveryType, Function.identity())));
		}

		return deliveryTypeActionHandler;
	}


}
