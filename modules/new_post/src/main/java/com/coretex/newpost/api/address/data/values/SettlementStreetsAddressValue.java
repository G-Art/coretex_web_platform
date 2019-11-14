package com.coretex.newpost.api.address.data.values;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SettlementStreetsAddressValue {

	@JsonProperty(value = "SettlementRef")
	private String settlementRef;

	@JsonProperty(value = "SettlementStreetRef")
	private String settlementStreetRef;

	@JsonProperty(value = "SettlementStreetDescription")
	private String settlementStreetDescription;

	@JsonProperty(value = "Present")
	private String present;

	@JsonProperty(value = "StreetsType")
	private String streetsType;

	@JsonProperty(value = "StreetsTypeDescription")
	private String streetsTypeDescription;

	@JsonProperty(value = "Location")
	private String location;

	public String getSettlementRef() {
		return settlementRef;
	}

	public void setSettlementRef(String settlementRef) {
		this.settlementRef = settlementRef;
	}

	public String getSettlementStreetRef() {
		return settlementStreetRef;
	}

	public void setSettlementStreetRef(String settlementStreetRef) {
		this.settlementStreetRef = settlementStreetRef;
	}

	public String getSettlementStreetDescription() {
		return settlementStreetDescription;
	}

	public void setSettlementStreetDescription(String settlementStreetDescription) {
		this.settlementStreetDescription = settlementStreetDescription;
	}

	public String getPresent() {
		return present;
	}

	public void setPresent(String present) {
		this.present = present;
	}

	public String getStreetsType() {
		return streetsType;
	}

	public void setStreetsType(String streetsType) {
		this.streetsType = streetsType;
	}

	public String getStreetsTypeDescription() {
		return streetsTypeDescription;
	}

	public void setStreetsTypeDescription(String streetsTypeDescription) {
		this.streetsTypeDescription = streetsTypeDescription;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
}
