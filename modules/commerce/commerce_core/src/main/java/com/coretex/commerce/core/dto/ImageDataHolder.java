package com.coretex.commerce.core.dto;

import java.io.InputStream;

public class ImageDataHolder<D> {

	private D descriptor;
	private InputStream imageData;

	public ImageDataHolder(D descriptor, InputStream imageData) {
		this.descriptor = descriptor;
		this.imageData = imageData;
	}

	public D getDescriptor() {
		return descriptor;
	}

	public void setDescriptor(D descriptor) {
		this.descriptor = descriptor;
	}

	public InputStream getImageData() {
		return imageData;
	}

	public void setImageData(InputStream imageData) {
		this.imageData = imageData;
	}
}
