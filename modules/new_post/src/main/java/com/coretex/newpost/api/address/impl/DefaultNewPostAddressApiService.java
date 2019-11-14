package com.coretex.newpost.api.address.impl;

import com.coretex.items.newpost.NewPostDeliveryServiceItem;
import com.coretex.newpost.api.address.NewPostAddressApiService;
import com.coretex.newpost.api.address.data.properties.CitiesProperties;
import com.coretex.newpost.api.address.data.properties.StreetProperties;
import com.coretex.newpost.api.data.ResponseApiData;
import com.coretex.newpost.api.address.data.properties.SettlementStreetsSearchProperties;
import com.coretex.newpost.api.address.data.properties.SettlementsProperties;
import com.coretex.newpost.api.address.data.properties.SettlementsSearchProperties;
import com.coretex.newpost.api.address.data.properties.WarehousesProperties;
import com.coretex.newpost.api.address.data.values.AreaValue;
import com.coretex.newpost.api.address.data.values.CityValue;
import com.coretex.newpost.api.address.data.values.SettlementValue;
import com.coretex.newpost.api.address.data.values.SettlementStreetsSearchValue;
import com.coretex.newpost.api.address.data.values.SettlementsSearchValue;
import com.coretex.newpost.api.address.data.values.StreetValue;
import com.coretex.newpost.api.address.data.values.WarehouseValue;
import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DefaultNewPostAddressApiService extends NewPostAddressApiService {

	private Logger LOG = LoggerFactory.getLogger(DefaultNewPostAddressApiService.class);



	private final String ADDRESS_MODEL_NAME = "Address";
	private final String ADDRESS_GENERAL_MODEL_NAME = "AddressGeneral";


	@Override
	public ResponseApiData<SettlementsSearchValue> searchSettlements(NewPostDeliveryServiceItem deliveryServiceItem, SettlementsSearchProperties settlementsSearchProperties) {
		var requestApiData = createRequestApiData(deliveryServiceItem.getApiKey(),
				ADDRESS_MODEL_NAME, "searchSettlements", settlementsSearchProperties);
		return request(deliveryServiceItem,
				requestApiData,
				new TypeReference<ResponseApiData<SettlementsSearchValue>>() {
				});
	}

	@Override
	public ResponseApiData<SettlementStreetsSearchValue> searchSettlementStreets(NewPostDeliveryServiceItem deliveryServiceItem, SettlementStreetsSearchProperties settlementStreetsSearchProperties) {
		var requestApiData = createRequestApiData(deliveryServiceItem.getApiKey(),
				ADDRESS_MODEL_NAME, "searchSettlementStreets", settlementStreetsSearchProperties);
		return request(deliveryServiceItem,
				requestApiData,
				new TypeReference<ResponseApiData<SettlementStreetsSearchValue>>() {
				});
	}

	@Override
	public ResponseApiData<CityValue> getCities(NewPostDeliveryServiceItem deliveryServiceItem, CitiesProperties citiesProperties) {
		var requestApiData = createRequestApiData(deliveryServiceItem.getApiKey(),
				ADDRESS_MODEL_NAME, "getCities", citiesProperties);
		return request(deliveryServiceItem,
				requestApiData,
				new TypeReference<ResponseApiData<CityValue>>() {
		});
	}

	@Override
	public ResponseApiData<StreetValue> getStreet(NewPostDeliveryServiceItem deliveryServiceItem, StreetProperties streetProperties) {
		var requestApiData = createRequestApiData(deliveryServiceItem.getApiKey(),
				ADDRESS_MODEL_NAME, "getStreet", streetProperties);
		return request(deliveryServiceItem,
				requestApiData,
				new TypeReference<ResponseApiData<StreetValue>>() {
		});
	}

	@Override
	public ResponseApiData<AreaValue> getAreas(NewPostDeliveryServiceItem deliveryServiceItem) {
		var requestApiData = createRequestApiData(deliveryServiceItem.getApiKey(),
				ADDRESS_MODEL_NAME, "getAreas", null);
		return request(deliveryServiceItem,
				requestApiData,
				new TypeReference<ResponseApiData<AreaValue>>() {
		});
	}

	@Override
	public ResponseApiData<WarehouseValue> getWarehouses(NewPostDeliveryServiceItem deliveryServiceItem, WarehousesProperties warehousesProperties) {
		var requestApiData = createRequestApiData(deliveryServiceItem.getApiKey(),
				ADDRESS_GENERAL_MODEL_NAME, "getWarehouses", warehousesProperties);
		return request(deliveryServiceItem,
				requestApiData,
				new TypeReference<ResponseApiData<WarehouseValue>>() {
		});
	}

	@Override
	public ResponseApiData<Map> getWarehouseTypes(NewPostDeliveryServiceItem deliveryServiceItem, WarehousesProperties warehousesProperties) {
		var requestApiData = createRequestApiData(deliveryServiceItem.getApiKey(),
				ADDRESS_GENERAL_MODEL_NAME, "getWarehouseTypes", warehousesProperties);
		return request(deliveryServiceItem,
				requestApiData,
				new TypeReference<ResponseApiData<Map>>() {
		});
	}

	@Override
	public ResponseApiData<SettlementValue> getSettlements(NewPostDeliveryServiceItem deliveryServiceItem, SettlementsProperties settlementsProperties) {
		var requestApiData = createRequestApiData(deliveryServiceItem.getApiKey(),
				ADDRESS_GENERAL_MODEL_NAME, "getSettlements", settlementsProperties);
		return request(deliveryServiceItem,
				requestApiData,
				new TypeReference<ResponseApiData<SettlementValue>>() {
		});
	}
}
