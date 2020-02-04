package com.coretex.commerce.data;

public class ShortVariantProductData extends ShortProductData {

	private String colorCssCode;
	private String size;

	private ImageData[] images;

	public ImageData[] getImages() {
		return images;
	}

	public void setImages(ImageData[] images) {
		this.images = images;
	}

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
}
