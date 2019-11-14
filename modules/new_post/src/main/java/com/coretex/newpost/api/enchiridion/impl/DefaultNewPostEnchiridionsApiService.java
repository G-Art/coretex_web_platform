package com.coretex.newpost.api.enchiridion.impl;

import com.coretex.items.newpost.NewPostDeliveryServiceItem;
import com.coretex.newpost.api.data.ResponseApiData;
import com.coretex.newpost.api.enchiridion.NewPostEnchiridionsApiService;
import com.coretex.newpost.api.enchiridion.data.properties.CargoDescriptionProperties;
import com.coretex.newpost.api.enchiridion.data.properties.PackProperties;
import com.coretex.newpost.api.enchiridion.data.properties.TimeIntervalsProperties;
import com.coretex.newpost.api.enchiridion.data.values.BackwardDeliveryCargoTypeValue;
import com.coretex.newpost.api.enchiridion.data.values.CargoDescriptionValue;
import com.coretex.newpost.api.enchiridion.data.values.CargoTypeValue;
import com.coretex.newpost.api.enchiridion.data.values.MessageCodeTextValue;
import com.coretex.newpost.api.enchiridion.data.values.OwnershipFormValue;
import com.coretex.newpost.api.enchiridion.data.values.PackValue;
import com.coretex.newpost.api.enchiridion.data.values.PalletValue;
import com.coretex.newpost.api.enchiridion.data.values.PaymentFormValue;
import com.coretex.newpost.api.enchiridion.data.values.ServiceTypeValue;
import com.coretex.newpost.api.enchiridion.data.values.TimeIntervalValue;
import com.coretex.newpost.api.enchiridion.data.values.TiresWheelValue;
import com.coretex.newpost.api.enchiridion.data.values.TypesOfCounterpartyValue;
import com.coretex.newpost.api.enchiridion.data.values.TypesOfPayerValue;
import com.coretex.newpost.api.impl.NewPostApiJsonIOService;
import com.coretex.newpost.api.impl.NewPostApiXmlIOService;
import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class DefaultNewPostEnchiridionsApiService extends NewPostEnchiridionsApiService {

	private Logger LOG = LoggerFactory.getLogger(DefaultNewPostEnchiridionsApiService.class);

	private final String COMMON_MODEL_NAME = "Common";
	private final String COMMON_GENERAL_MODEL_NAME = "CommonGeneral";

	@Resource
	private NewPostApiJsonIOService newPostApiJsonIOService;

	@Resource
	private NewPostApiXmlIOService newPostApiXmlIOService;

	@Override
	public ResponseApiData<TimeIntervalValue> getTimeIntervals(NewPostDeliveryServiceItem deliveryServiceItem, TimeIntervalsProperties timeIntervalsProperties) {
		var requestApiData = createRequestApiData(deliveryServiceItem.getApiKey(),
				COMMON_MODEL_NAME, "getTimeIntervals", timeIntervalsProperties);
		return request(deliveryServiceItem,
				requestApiData,
				new TypeReference<ResponseApiData<TimeIntervalValue>>() {
				});
	}

	@Override
	public ResponseApiData<CargoTypeValue> getCargoTypes(NewPostDeliveryServiceItem deliveryServiceItem) {
		var requestApiData = createRequestApiData(deliveryServiceItem.getApiKey(),
				COMMON_MODEL_NAME, "getCargoTypes", null);
		return request(deliveryServiceItem,
				requestApiData,
				new TypeReference<ResponseApiData<CargoTypeValue>>() {
				});
	}

	@Override
	public ResponseApiData<BackwardDeliveryCargoTypeValue> getBackwardDeliveryCargoTypes(NewPostDeliveryServiceItem deliveryServiceItem) {
		var requestApiData = createRequestApiData(deliveryServiceItem.getApiKey(),
				COMMON_MODEL_NAME, "getBackwardDeliveryCargoTypes", null);
		return request(deliveryServiceItem,
				requestApiData,
				new TypeReference<ResponseApiData<BackwardDeliveryCargoTypeValue>>() {
				});
	}

	@Override
	public ResponseApiData<PalletValue> getPalletsList(NewPostDeliveryServiceItem deliveryServiceItem) {
		var requestApiData = createRequestApiData(deliveryServiceItem.getApiKey(),
				COMMON_MODEL_NAME, "getPalletsList", null);
		return request(deliveryServiceItem,
				requestApiData,
				new TypeReference<ResponseApiData<PalletValue>>() {
				});
	}

	@Override
	public ResponseApiData<TypesOfPayerValue> getTypesOfPayers(NewPostDeliveryServiceItem deliveryServiceItem) {
		var requestApiData = createRequestApiData(deliveryServiceItem.getApiKey(),
				COMMON_MODEL_NAME, "getTypesOfPayers", null);
		return request(deliveryServiceItem,
				requestApiData,
				new TypeReference<ResponseApiData<TypesOfPayerValue>>() {
				});
	}

	@Override
	public ResponseApiData<TypesOfPayerValue> getTypesOfPayersForRedelivery(NewPostDeliveryServiceItem deliveryServiceItem) {
		var requestApiData = createRequestApiData(deliveryServiceItem.getApiKey(),
				COMMON_MODEL_NAME, "getTypesOfPayersForRedelivery", null);
		return request(deliveryServiceItem,
				requestApiData,
				new TypeReference<ResponseApiData<TypesOfPayerValue>>() {
				});
	}

	@Override
	public ResponseApiData<PackValue> getPackList(NewPostDeliveryServiceItem deliveryServiceItem, PackProperties packProperties) {
		var requestApiData = createRequestApiData(deliveryServiceItem.getApiKey(),
				COMMON_MODEL_NAME, "getPackList", packProperties);
		return request(deliveryServiceItem,
				requestApiData,
				new TypeReference<ResponseApiData<PackValue>>() {
				});
	}

	@Override
	public ResponseApiData<TiresWheelValue> getTiresWheelsList(NewPostDeliveryServiceItem deliveryServiceItem) {
		var requestApiData = createRequestApiData(deliveryServiceItem.getApiKey(),
				COMMON_MODEL_NAME, "getTiresWheelsList", null);
		return request(deliveryServiceItem,
				requestApiData,
				new TypeReference<ResponseApiData<TiresWheelValue>>() {
				});
	}

	@Override
	public ResponseApiData<CargoDescriptionValue> getCargoDescriptionList(NewPostDeliveryServiceItem deliveryServiceItem, CargoDescriptionProperties cargoDescriptionProperties) {
		var requestApiData = createRequestApiData(deliveryServiceItem.getApiKey(),
				COMMON_MODEL_NAME, "getCargoDescriptionList", cargoDescriptionProperties);
		return request(deliveryServiceItem,
				requestApiData,
				new TypeReference<ResponseApiData<CargoDescriptionValue>>() {
				});
	}

	@Override
	public ResponseApiData<MessageCodeTextValue> getMessageCodeText(NewPostDeliveryServiceItem deliveryServiceItem) {
		var requestApiData = createRequestApiData(deliveryServiceItem.getApiKey(),
				COMMON_GENERAL_MODEL_NAME, "getMessageCodeText", null);
		return request(deliveryServiceItem,
				requestApiData,
				new TypeReference<ResponseApiData<MessageCodeTextValue>>() {
				});
	}

	@Override
	public ResponseApiData<ServiceTypeValue> getServiceTypes(NewPostDeliveryServiceItem deliveryServiceItem) {
		var requestApiData = createRequestApiData(deliveryServiceItem.getApiKey(),
				COMMON_MODEL_NAME, "getServiceTypes", null);
		return request(deliveryServiceItem,
				requestApiData,
				new TypeReference<ResponseApiData<ServiceTypeValue>>() {
				});
	}

	@Override
	public ResponseApiData<TypesOfCounterpartyValue> getTypesOfCounterparties(NewPostDeliveryServiceItem deliveryServiceItem) {
		var requestApiData = createRequestApiData(deliveryServiceItem.getApiKey(),
				COMMON_MODEL_NAME, "getTypesOfCounterparties", null);
		return request(deliveryServiceItem,
				requestApiData,
				new TypeReference<ResponseApiData<TypesOfCounterpartyValue>>() {
				});
	}

	@Override
	public ResponseApiData<PaymentFormValue> getPaymentForms(NewPostDeliveryServiceItem deliveryServiceItem) {
		var requestApiData = createRequestApiData(deliveryServiceItem.getApiKey(),
				COMMON_MODEL_NAME, "getPaymentForms", null);
		return request(deliveryServiceItem,
				requestApiData,
				new TypeReference<ResponseApiData<PaymentFormValue>>() {
				});
	}

	@Override
	public ResponseApiData<OwnershipFormValue> getOwnershipFormsList(NewPostDeliveryServiceItem deliveryServiceItem) {
		var requestApiData = createRequestApiData(deliveryServiceItem.getApiKey(),
				COMMON_MODEL_NAME, "getOwnershipFormsList", null);
		return request(deliveryServiceItem,
				requestApiData,
				new TypeReference<ResponseApiData<OwnershipFormValue>>() {
				});
	}

}
