package com.coretex.newpost.api.address.data.values;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SettlementStreetsSearchValue {

	@JsonProperty(value = "TotalCount")
	private Integer count;

	@JsonProperty(value = "Addresses")
	private SettlementStreetsAddressValue[] addresses;

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public SettlementStreetsAddressValue[] getAddresses() {
		return addresses;
	}

	public void setAddresses(SettlementStreetsAddressValue[] addresses) {
		this.addresses = addresses;
	}
}
