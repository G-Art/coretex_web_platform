package com.coretex.commerce.strategies.cart.impl;

import com.coretex.items.cx_core.CartItem;
import com.coretex.items.cx_core.ProductItem;

public class CartParameter {

	private CartItem cart;
	private ProductItem product;
	private int quantity;

	public CartItem getCart() {
		return cart;
	}

	public void setCart(CartItem cart) {
		this.cart = cart;
	}

	public ProductItem getProduct() {
		return product;
	}

	public void setProduct(ProductItem product) {
		this.product = product;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}
