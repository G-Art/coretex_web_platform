package com.coretex.newpost.api.actions.data.properties;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DocumentPriceProperties {

	@JsonProperty(value = "CitySender")
	private String citySender;

	@JsonProperty(value = "CityRecipient")
	private String cityRecipient;

	@JsonProperty(value = "Weight")
	private String weight;

	@JsonProperty(value = "ServiceType")
	private String serviceType;

	@JsonProperty(value = "Cost")
	private Integer cost;

	@JsonProperty(value = "CargoType")
	private String cargoType; // example: Cargo, Documents, TiresWheels, Pallet

	@JsonProperty(value = "SeatsAmount")
	private String seatsAmount;

	@JsonProperty(value = "PackCalculate")
	private PackCalculateProperties packCalculate;

	@JsonProperty(value = "RedeliveryCalculate")
	private RedeliveryCalculateProperties redeliveryCalculate;

	@JsonProperty(value = "CargoDetails")
	private CargoDetailsProperties cargoDetails;


	public String getCitySender() {
		return citySender;
	}

	public void setCitySender(String citySender) {
		this.citySender = citySender;
	}

	public String getCityRecipient() {
		return cityRecipient;
	}

	public void setCityRecipient(String cityRecipient) {
		this.cityRecipient = cityRecipient;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public Integer getCost() {
		return cost;
	}

	public void setCost(Integer cost) {
		this.cost = cost;
	}

	public String getCargoType() {
		return cargoType;
	}

	public void setCargoType(String cargoType) {
		this.cargoType = cargoType;
	}

	public String getSeatsAmount() {
		return seatsAmount;
	}

	public void setSeatsAmount(String seatsAmount) {
		this.seatsAmount = seatsAmount;
	}

	public PackCalculateProperties getPackCalculate() {
		return packCalculate;
	}

	public void setPackCalculate(PackCalculateProperties packCalculate) {
		this.packCalculate = packCalculate;
	}

	public RedeliveryCalculateProperties getRedeliveryCalculate() {
		return redeliveryCalculate;
	}

	public void setRedeliveryCalculate(RedeliveryCalculateProperties redeliveryCalculate) {
		this.redeliveryCalculate = redeliveryCalculate;
	}

	public CargoDetailsProperties getCargoDetails() {
		return cargoDetails;
	}

	public void setCargoDetails(CargoDetailsProperties cargoDetails) {
		this.cargoDetails = cargoDetails;
	}
}
