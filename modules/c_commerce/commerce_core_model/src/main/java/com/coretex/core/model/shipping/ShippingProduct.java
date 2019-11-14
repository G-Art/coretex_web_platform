package com.coretex.core.model.shipping;

import com.coretex.items.commerce_core_model.ProductItem;
import com.coretex.core.model.catalog.product.price.FinalPrice;

public class ShippingProduct {

	public ShippingProduct(ProductItem product) {
		this.product = product;

	}

	private int quantity = 1;
	private ProductItem product;

	private FinalPrice finalPrice;


	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setProduct(ProductItem product) {
		this.product = product;
	}

	public ProductItem getProduct() {
		return product;
	}

	public FinalPrice getFinalPrice() {
		return finalPrice;
	}

	public void setFinalPrice(FinalPrice finalPrice) {
		this.finalPrice = finalPrice;
	}

}
