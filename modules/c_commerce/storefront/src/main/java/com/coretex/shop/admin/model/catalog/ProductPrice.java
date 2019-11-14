package com.coretex.shop.admin.model.catalog;

import com.coretex.items.commerce_core_model.ProductAvailabilityItem;
import com.coretex.items.commerce_core_model.ProductItem;
import com.coretex.items.commerce_core_model.ProductPriceItem;

import javax.validation.Valid;

public class ProductPrice {

	@Valid
	private ProductPriceItem price = null;
	private String priceText;
	private String specialPriceText;
	private ProductAvailabilityItem productAvailability;


	//cannot convert in this object to date ??? needs to use a string, parse, bla bla
	private String productPriceSpecialStartDate;
	private String productPriceSpecialEndDate;

	private ProductItem product;


	public ProductAvailabilityItem getProductAvailability() {
		return productAvailability;
	}

	public void setProductAvailability(ProductAvailabilityItem productAvailability) {
		this.productAvailability = productAvailability;
	}

	public String getPriceText() {
		return priceText;
	}

	public void setPriceText(String priceText) {
		this.priceText = priceText;
	}

	public ProductPriceItem getPrice() {
		return price;
	}

	public void setPrice(ProductPriceItem price) {
		this.price = price;
	}

	public String getSpecialPriceText() {
		return specialPriceText;
	}

	public void setSpecialPriceText(String specialPriceText) {
		this.specialPriceText = specialPriceText;
	}

	public ProductItem getProduct() {
		return product;
	}

	public void setProduct(ProductItem product) {
		this.product = product;
	}

	public String getProductPriceSpecialStartDate() {
		return productPriceSpecialStartDate;
	}

	public void setProductPriceSpecialStartDate(
			String productPriceSpecialStartDate) {
		this.productPriceSpecialStartDate = productPriceSpecialStartDate;
	}

	public String getProductPriceSpecialEndDate() {
		return productPriceSpecialEndDate;
	}

	public void setProductPriceSpecialEndDate(String productPriceSpecialEndDate) {
		this.productPriceSpecialEndDate = productPriceSpecialEndDate;
	}

}
