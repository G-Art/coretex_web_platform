package com.coretex.newpost.api.enchiridion.data.properties;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PackProperties {

	@JsonProperty(value = "Length")
	private String length;

	@JsonProperty(value = "Width")
	private String width;

	@JsonProperty(value = "Height")
	private String height;

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
