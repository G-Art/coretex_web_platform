package com.coretex.commerce.data;

public class ImageData {

	private String alt;
	private String path;

	public ImageData() {
	}

	public ImageData(String path) {
		this.path = path;
	}

	public ImageData(String alt, String path) {
		this.alt = alt;
		this.path = path;
	}

	public String getAlt() {
		return alt;
	}

	public void setAlt(String alt) {
		this.alt = alt;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
