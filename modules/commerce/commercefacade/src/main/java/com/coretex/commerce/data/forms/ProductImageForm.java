package com.coretex.commerce.data.forms;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public class ProductImageForm {

	private MultipartFile image;

	private Map<String, String> alt;

	public ProductImageForm() {

	}

	public MultipartFile getImage() {
		return image;
	}

	public void setImage(MultipartFile image) {
		this.image = image;
	}

	public Map<String, String> getAlt() {
		return alt;
	}

	public void setAlt(Map<String, String> alt) {
		this.alt = alt;
	}
}
