package com.coretex.shop.model.catalog.product.attribute;

import java.io.Serializable;
import java.util.UUID;

public class ReadableProductVariantValue implements Serializable {


	private static final long serialVersionUID = 1L;
	private UUID value;


	public UUID getValue() {
		return value;
	}


	public void setValue(UUID value) {
		this.value = value;
	}


}
