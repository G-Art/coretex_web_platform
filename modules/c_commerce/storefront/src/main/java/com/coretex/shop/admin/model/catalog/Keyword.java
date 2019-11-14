package com.coretex.shop.admin.model.catalog;

import javax.validation.constraints.NotEmpty;

import java.io.Serializable;
import java.util.UUID;

/**
 * Post keyword from the admin
 *
 * @author Carl Samson
 */
public class Keyword implements Serializable {


	private static final long serialVersionUID = 1L;
	private UUID productId;
	private String languageCode;
	@NotEmpty
	private String keyword;

	public void setProductId(UUID productId) {
		this.productId = productId;
	}

	public UUID getProductId() {
		return productId;
	}

	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}

	public String getLanguageCode() {
		return languageCode;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getKeyword() {
		return keyword;
	}

}
