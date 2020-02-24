package com.coretex.commerce.web.data;

import java.util.UUID;

public class AddToCartRequest {
	private UUID product;
	private Integer quantity;

	public UUID getProduct() {
		return product;
	}

	public void setProduct(UUID product) {
		this.product = product;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
}
