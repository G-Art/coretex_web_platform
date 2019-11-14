package com.coretex.newpost.api.enchiridion.data.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
//https://devcenter.novaposhta.ua/docs/services/55702570a0fe4f0cf4fc53ed/operations/55702571a0fe4f0b6483890f
public class TimeIntervalsProperties {

	@JsonProperty(value = "RecipientCityRef")
	private String recipientCityRef;

	@JsonProperty(value = "DateTime")
	private String dateTime;

	public String getRecipientCityRef() {
		return recipientCityRef;
	}

	public void setRecipientCityRef(String recipientCityRef) {
		this.recipientCityRef = recipientCityRef;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
}
