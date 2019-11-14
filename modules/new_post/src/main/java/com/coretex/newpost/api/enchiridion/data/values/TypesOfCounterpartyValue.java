package com.coretex.newpost.api.enchiridion.data.values;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TypesOfCounterpartyValue {

	@JsonProperty(value = "Ref")
	private String reference;

	@JsonProperty(value = "Description")
	private String description;
}
