package com.coretex.shop.admin.model.content;

import java.util.UUID;

public class ProductImages extends ContentFiles {


	private static final long serialVersionUID = 7732719188032287938L;
	private UUID productId;

	public UUID getProductId() {
		return productId;
	}

	public void setProductId(UUID productId) {
		this.productId = productId;
	}

}
