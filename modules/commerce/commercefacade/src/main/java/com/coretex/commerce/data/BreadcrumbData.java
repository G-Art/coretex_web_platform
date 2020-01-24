package com.coretex.commerce.data;

public class BreadcrumbData {
	private String link;
	private boolean active = false;
	private String text;

	public BreadcrumbData(String link, String text) {
		this.link = link;
		this.text = text;
	}

	public BreadcrumbData(boolean active, String text) {
		this.active = active;
		this.text = text;
	}

	public BreadcrumbData(String link, boolean active, String text) {
		this.link = link;
		this.active = active;
		this.text = text;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
