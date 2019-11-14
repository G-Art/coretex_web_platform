package com.coretex.newpost.api.address.data.properties;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SettlementsSearchProperties {

	@JsonProperty(value = "CityName")
	private String cityName;

	@JsonProperty(value = "Limit", defaultValue = "10")
	private Integer limit = 10;

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
}
