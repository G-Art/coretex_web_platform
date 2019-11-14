package com.coretex.shop.model.catalog.manufacturer;

import java.io.Serializable;

public class ReadableManufacturer extends ManufacturerEntity implements
		Serializable {


	private static final long serialVersionUID = 1L;
	private String description;
	private String name;

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
