package com.coretex.newpost.api.actions.data.properties;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DocumentDeliveryDateProperties {

	@JsonProperty(value = "DateTime")
	private String dateTime;

	@JsonProperty(value = "ServiceType")
	private String serviceType;

	@JsonProperty(value = "CitySender")
	private String citySender;

	@JsonProperty(value = "CityRecipient")
	private String cityRecipient;

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

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
}
