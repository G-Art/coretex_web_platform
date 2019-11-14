package com.coretex.newpost.api.address;

import com.coretex.items.newpost.NewPostDeliveryServiceItem;
import com.coretex.newpost.api.NewPostApiRequestService;
import com.coretex.newpost.api.NewPostDataApiService;
import com.coretex.newpost.api.address.data.properties.CitiesProperties;
import com.coretex.newpost.api.data.ResponseApiData;
import com.coretex.newpost.api.address.data.properties.SettlementStreetsSearchProperties;
import com.coretex.newpost.api.address.data.properties.SettlementsProperties;
import com.coretex.newpost.api.address.data.properties.SettlementsSearchProperties;
import com.coretex.newpost.api.address.data.properties.StreetProperties;
import com.coretex.newpost.api.address.data.properties.WarehousesProperties;
import com.coretex.newpost.api.address.data.values.AreaValue;
import com.coretex.newpost.api.address.data.values.CityValue;
import com.coretex.newpost.api.address.data.values.SettlementValue;
import com.coretex.newpost.api.address.data.values.SettlementStreetsSearchValue;
import com.coretex.newpost.api.address.data.values.SettlementsSearchValue;
import com.coretex.newpost.api.address.data.values.StreetValue;
import com.coretex.newpost.api.address.data.values.WarehouseValue;

import java.util.Map;

public abstract class NewPostAddressApiService extends NewPostApiRequestService implements NewPostDataApiService {
	public abstract ResponseApiData<SettlementsSearchValue> searchSettlements(NewPostDeliveryServiceItem deliveryServiceItem, SettlementsSearchProperties settlementsSearchProperties);

	public abstract ResponseApiData<SettlementStreetsSearchValue> searchSettlementStreets(NewPostDeliveryServiceItem deliveryServiceItem, SettlementStreetsSearchProperties settlementStreetsSearchProperties);

	public abstract ResponseApiData<CityValue>  getCities(NewPostDeliveryServiceItem deliveryServiceItem, CitiesProperties citiesProperties);

	public abstract ResponseApiData<StreetValue> getStreet(NewPostDeliveryServiceItem deliveryServiceItem, StreetProperties streetProperties);

	public abstract ResponseApiData<AreaValue>  getAreas(NewPostDeliveryServiceItem deliveryServiceItem);

	public abstract ResponseApiData<WarehouseValue> getWarehouses(NewPostDeliveryServiceItem deliveryServiceItem, WarehousesProperties warehousesProperties);

	public abstract ResponseApiData<Map> getWarehouseTypes(NewPostDeliveryServiceItem deliveryServiceItem, WarehousesProperties warehousesProperties);

	public abstract ResponseApiData<SettlementValue> getSettlements(NewPostDeliveryServiceItem deliveryServiceItem, SettlementsProperties settlementsProperties);

}
