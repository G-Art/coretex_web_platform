package com.coretex.shop.model.shop;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.coretex.items.core.LocaleItem;
import com.coretex.items.core.LocaleItem;


public class Breadcrumb implements Serializable {


	private static final long serialVersionUID = 1L;
	private BreadcrumbItemType itemType;
	transient private LocaleItem language;
	private String urlRefContent = null;
	private List<BreadcrumbItem> breadCrumbs = new ArrayList<BreadcrumbItem>();

	public LocaleItem getLanguage() {
		return language;
	}

	public void setLanguage(LocaleItem language) {
		this.language = language;
	}

	public List<BreadcrumbItem> getBreadCrumbs() {
		return breadCrumbs;
	}

	public void setBreadCrumbs(List<BreadcrumbItem> breadCrumbs) {
		this.breadCrumbs = breadCrumbs;
	}

	public void setItemType(BreadcrumbItemType itemType) {
		this.itemType = itemType;
	}

	public BreadcrumbItemType getItemType() {
		return itemType;
	}

	public String getUrlRefContent() {
		return urlRefContent;
	}

	public void setUrlRefContent(String urlRefContent) {
		this.urlRefContent = urlRefContent;
	}

}
