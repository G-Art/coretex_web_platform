package com.coretex.commerce.data;

import java.math.BigDecimal;

public class OrderProductData extends GenericItemData {

	private String sku;
	private String productName;
	private Integer productQuantity;
	private BigDecimal oneTimeCharge;
	private ProductData originalProduct;

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer getProductQuantity() {
		return productQuantity;
	}

	public void setProductQuantity(Integer productQuantity) {
		this.productQuantity = productQuantity;
	}

	public BigDecimal getOneTimeCharge() {
		return oneTimeCharge;
	}

	public void setOneTimeCharge(BigDecimal oneTimeCharge) {
		this.oneTimeCharge = oneTimeCharge;
	}

	public ProductData getOriginalProduct() {
		return originalProduct;
	}

	public void setOriginalProduct(ProductData originalProduct) {
		this.originalProduct = originalProduct;
	}
}
