package com.coretex.newpost.api.actions.data.properties;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DocumentListProperties {

	@JsonProperty(value = "DateTimeFrom")
	private String dateTimeFrom; //example  "21.06.2016"
	@JsonProperty(value = "DateTimeTo")
	private String dateTimeTo; //example  "21.06.2016"
	@JsonProperty(value = "Page")
	private String page;

	@JsonProperty(value = "GetFullList")
	private String fullList; // Switching pageable mode for result (0 - pagination on; 1 - pagination off)

	@JsonProperty(value = "DateTime")
	private String dateTime;

	public String getDateTimeFrom() {
		return dateTimeFrom;
	}

	public void setDateTimeFrom(String dateTimeFrom) {
		this.dateTimeFrom = dateTimeFrom;
	}

	public String getDateTimeTo() {
		return dateTimeTo;
	}

	public void setDateTimeTo(String dateTimeTo) {
		this.dateTimeTo = dateTimeTo;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getFullList() {
		return fullList;
	}

	public void setFullList(String fullList) {
		this.fullList = fullList;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
}
