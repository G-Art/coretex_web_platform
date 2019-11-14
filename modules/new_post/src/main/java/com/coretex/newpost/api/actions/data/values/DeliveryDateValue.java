package com.coretex.newpost.api.actions.data.values;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DeliveryDateValue {

	@JsonProperty(value = "date")
	private String date; // example: "2016-11-11 00:00:00.000000"

	@JsonProperty(value = "timezone_type")
	private String timezoneType;

	@JsonProperty(value = "timezone")
	private String timezone;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTimezoneType() {
		return timezoneType;
	}

	public void setTimezoneType(String timezoneType) {
		this.timezoneType = timezoneType;
	}

	public String getTimezone() {
		return timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}
}
