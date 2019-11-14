package com.coretex.newpost.api.address.data.values;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SettlementsSearchValue {

	@JsonProperty(value = "TotalCount")
	private Integer count;

	@JsonProperty(value = "Addresses")
	private SettlementsAddressValue[] settlementsAddressValues;

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public SettlementsAddressValue[] getSettlementsAddressValues() {
		return settlementsAddressValues;
	}

	public void setSettlementsAddressValues(SettlementsAddressValue[] settlementsAddressValues) {
		this.settlementsAddressValues = settlementsAddressValues;
	}
}
