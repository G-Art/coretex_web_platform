package com.coretex.commerce.data;

import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

public class DeliveryServiceData extends GenericItemData {

	private String code;
	private Map<String, String> name;
	private List<StoreData> stores;
	private List<CountryData> countries;
	private String active;
	private String type;

	private Map<String, Object> additionalData = Maps.newHashMap();

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Map<String, String> getName() {
		return name;
	}

	public void setName(Map<String, String> name) {
		this.name = name;
	}

	public List<StoreData> getStores() {
		return stores;
	}

	public void setStores(List<StoreData> stores) {
		this.stores = stores;
	}

	public List<CountryData> getCountries() {
		return countries;
	}

	public void setCountries(List<CountryData> countries) {
		this.countries = countries;
	}

	public Map<String, Object> getAdditionalData() {
		return additionalData;
	}

	public void setAdditionalData(Map<String, Object> additionalData) {
		this.additionalData = additionalData;
	}
}
