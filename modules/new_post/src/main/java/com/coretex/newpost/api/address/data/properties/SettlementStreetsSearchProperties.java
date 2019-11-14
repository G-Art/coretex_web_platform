package com.coretex.newpost.api.address.data.properties;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SettlementStreetsSearchProperties {

	@JsonProperty(value = "StreetName")
	private String streetName;

	@JsonProperty(value = "SettlementRef")
	private String settlement;

	@JsonProperty(value = "Limit", defaultValue = "10")
	private Integer limit = 10;

	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public String getSettlement() {
		return settlement;
	}

	public void setSettlement(String settlement) {
		this.settlement = settlement;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}
}
