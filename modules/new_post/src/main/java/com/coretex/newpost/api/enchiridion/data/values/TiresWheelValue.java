package com.coretex.newpost.api.enchiridion.data.values;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

//https://devcenter.novaposhta.ua/docs/services/55702570a0fe4f0cf4fc53ed/operations/5824774ba0fe4f0e60694eb0

@JsonIgnoreProperties(ignoreUnknown = true)
public class TiresWheelValue {

	@JsonProperty(value = "Ref")
	private String reference;

	@JsonProperty(value = "Description")
	private String description;

	@JsonProperty(value = "DescriptionRu")
	private String descriptionRu;

	@JsonProperty(value = "Weight")
	private String weight;

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescriptionRu() {
		return descriptionRu;
	}

	public void setDescriptionRu(String descriptionRu) {
		this.descriptionRu = descriptionRu;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}
}
