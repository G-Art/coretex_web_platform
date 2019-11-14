package com.coretex.shop.admin.data;

import javax.validation.constraints.NotBlank;

public class ProductTypeDto extends GenericDto {

	@NotBlank
	private String code;

	private boolean allowAddToCart = true;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public boolean isAllowAddToCart() {
		return allowAddToCart;
	}

	public void setAllowAddToCart(boolean allowAddToCart) {
		this.allowAddToCart = allowAddToCart;
	}
}
