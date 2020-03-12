package com.coretex.commerce.data;

public class VariantProductData extends ProductData {

	private String colorCssCode;
	private String colorName;
	private String size;
	private String baseProductCode;

	public String getColorCssCode() {
		return colorCssCode;
	}

	public void setColorCssCode(String colorCssCode) {
		this.colorCssCode = colorCssCode;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getBaseProductCode() {
		return baseProductCode;
	}

	public void setBaseProductCode(String baseProductCode) {
		this.baseProductCode = baseProductCode;
	}

	public String getColorName() {
		return colorName;
	}

	public void setColorName(String colorName) {
		this.colorName = colorName;
	}
}
