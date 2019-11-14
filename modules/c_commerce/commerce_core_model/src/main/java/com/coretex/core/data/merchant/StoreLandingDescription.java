package com.coretex.core.data.merchant;

import com.coretex.items.commerce_core_model.LanguageItem;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

public class StoreLandingDescription implements Serializable {



	private static final long serialVersionUID = 1L;
	@NotEmpty
	private String title;
	private String description;
	private String keywords;
	private String homePageContent;


	private LanguageItem language;


	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setHomePageContent(String homePageContent) {
		this.homePageContent = homePageContent;
	}

	public String getHomePageContent() {
		return homePageContent;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setLanguage(LanguageItem language) {
		this.language = language;
	}

	public LanguageItem getLanguage() {
		return language;
	}

}
