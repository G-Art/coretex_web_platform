package com.coretex.shop.model.shoppingcart;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import com.coretex.shop.model.catalog.product.attribute.ProductAttribute;

/**
 * Compatible with v1
 *
 * @author c.samson
 */
public class PersistableShoppingCartItem implements Serializable {


	private static final long serialVersionUID = 1L;
	private UUID product;//product id
	private int quantity;

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	private List<ProductAttribute> attributes;

	public UUID getProduct() {
		return product;
	}

	public void setProduct(UUID product) {
		this.product = product;
	}

	public List<ProductAttribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<ProductAttribute> attributes) {
		this.attributes = attributes;
	}

}
