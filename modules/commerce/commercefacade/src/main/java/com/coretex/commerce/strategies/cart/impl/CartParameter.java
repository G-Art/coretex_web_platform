package com.coretex.commerce.strategies.cart.impl;

import com.coretex.items.cx_core.CartItem;
import com.coretex.items.cx_core.OrderEntryItem;
import com.coretex.items.cx_core.VariantProductItem;

public class CartParameter {

	private CartItem cart;
	private OrderEntryItem entryItem;
	private VariantProductItem product;
	private int quantity;

	public CartItem getCart() {
		return cart;
	}

	public void setCart(CartItem cart) {
		this.cart = cart;
	}

	public VariantProductItem getProduct() {
		return product;
	}

	public void setProduct(VariantProductItem product) {
		this.product = product;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public OrderEntryItem getEntryItem() {
		return entryItem;
	}

	public void setEntryItem(OrderEntryItem entryItem) {
		this.entryItem = entryItem;
	}
}
