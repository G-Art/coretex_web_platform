package com.coretex.commerce.data;

import java.math.BigDecimal;

public class OrderEntryData extends GenericItemData {

	VariantProductData product;
	Integer quantity;
	Boolean calculated;
	BigDecimal price;

	public VariantProductData getProduct() {
		return product;
	}

	public void setProduct(VariantProductData product) {
		this.product = product;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Boolean getCalculated() {
		return calculated;
	}

	public void setCalculated(Boolean calculated) {
		this.calculated = calculated;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	BigDecimal totalPrice;

}
