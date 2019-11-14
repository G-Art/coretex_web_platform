package com.coretex.newpost.api.actions.data.properties;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RedeliveryCalculateProperties {

	@JsonProperty(value = "CargoType")
	private String cargoType;

	@JsonProperty(value = "Amount")
	private String amount;

	public String getCargoType() {
		return cargoType;
	}

	public void setCargoType(String cargoType) {
		this.cargoType = cargoType;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}
}
