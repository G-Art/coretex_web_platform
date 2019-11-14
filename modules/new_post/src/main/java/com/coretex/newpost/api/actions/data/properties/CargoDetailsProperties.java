package com.coretex.newpost.api.actions.data.properties;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CargoDetailsProperties {

	@JsonProperty(value = "CargoDescription")
	private String cargoDescription;

	@JsonProperty(value = "Amount")
	private String amount;

	public String getCargoDescription() {
		return cargoDescription;
	}

	public void setCargoDescription(String cargoDescription) {
		this.cargoDescription = cargoDescription;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}
}
