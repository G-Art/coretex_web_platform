package com.coretex.commerce.data;

public class ProductData extends GenericItemData {

	private String code;
	private String name;
	private String description;
	private Boolean available;
	private String store;
	private ImageData[] images;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getAvailable() {
		return available;
	}

	public void setAvailable(Boolean available) {
		this.available = available;
	}

	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		this.store = store;
	}

	public ImageData[] getImages() {
		return images;
	}

	public void setImages(ImageData[] images) {
		this.images = images;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
