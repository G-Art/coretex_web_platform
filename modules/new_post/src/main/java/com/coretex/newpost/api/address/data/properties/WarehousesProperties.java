package com.coretex.newpost.api.address.data.properties;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WarehousesProperties {

	@JsonProperty(value = "CityName")
	private String cityName;

	@JsonProperty(value = "CityRef")
	private String cityRef;

	@JsonProperty(value = "SettlementRef")
	private String settlementRef;

	@JsonProperty(value = "Page")
	private Integer page = 0;

	@JsonProperty(value = "Limit")
	private Integer limit = 500;

	@JsonProperty(value = "Language")
	private String language;

	public String getCityRef() {
		return cityRef;
	}

	public void setCityRef(String cityRef) {
		this.cityRef = cityRef;
	}

	public String getSettlementRef() {
		return settlementRef;
	}

	public void setSettlementRef(String settlementRef) {
		this.settlementRef = settlementRef;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}
}
