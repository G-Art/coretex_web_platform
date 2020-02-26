package com.coretex.commerce.data;

import java.util.List;

public class ProductData extends GenericItemData {

	private String code;
	private String name;
	private String description;
	private Boolean available;
	private String store;
	private ImageData[] images;
	private String priceDiscount;
	private String price;
	private List<VariantProductData> variants;

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

	public String getPriceDiscount() {
		return priceDiscount;
	}

	public void setPriceDiscount(String priceDiscount) {
		this.priceDiscount = priceDiscount;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public List<VariantProductData> getVariants() {
		return variants;
	}

	public void setVariants(List<VariantProductData> variants) {
		this.variants = variants;
	}
}
