package com.coretex.newpost.api.enchiridion.data.properties;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CargoDescriptionProperties {

	@JsonProperty(value = "FindByString")
	private String findByString;

	@JsonProperty(value = "Page")
	private Integer page;
}
