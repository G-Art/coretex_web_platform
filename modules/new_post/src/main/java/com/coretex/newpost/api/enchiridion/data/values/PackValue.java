package com.coretex.newpost.api.enchiridion.data.values;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

//https://devcenter.novaposhta.ua/docs/services/55702570a0fe4f0cf4fc53ed/operations/5824774ba0fe4f0e60694eb0
@JsonIgnoreProperties(ignoreUnknown = true)
public class PackValue {
	@JsonProperty(value = "Ref")
	private String reference;

	@JsonProperty(value = "Description")
	private String description;

	@JsonProperty(value = "DescriptionRu")
	private String descriptionRu;

	@JsonProperty(value = "Length")
	private String length;

	@JsonProperty(value = "Width")
	private String width;

	@JsonProperty(value = "Height")
	private String height;

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

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}
}
