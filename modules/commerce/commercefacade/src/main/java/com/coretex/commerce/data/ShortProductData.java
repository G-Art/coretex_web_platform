package com.coretex.commerce.data;

import java.util.Set;

public class ShortProductData extends GenericItemData {

	private String code;
	private String name;
	private String title;
	private String description;
	private String price;
	private String priceDiscount;
	private Boolean hotBadge;
	private String discountBadgeInfo;
	private String defaultVariantCode;

	private Set<ShortVariantProductData> variants;


	public String getDefaultVariantCode() {
		return defaultVariantCode;
	}

	public void setDefaultVariantCode(String defaultVariantCode) {
		this.defaultVariantCode = defaultVariantCode;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getPriceDiscount() {
		return priceDiscount;
	}

	public void setPriceDiscount(String priceDiscount) {
		this.priceDiscount = priceDiscount;
	}

	public Boolean getHotBadge() {
		return hotBadge;
	}

	public void setHotBadge(Boolean hotBadge) {
		this.hotBadge = hotBadge;
	}

	public String getDiscountBadgeInfo() {
		return discountBadgeInfo;
	}

	public void setDiscountBadgeInfo(String discountBadgeInfo) {
		this.discountBadgeInfo = discountBadgeInfo;
	}

	public Set<ShortVariantProductData> getVariants() {
		return variants;
	}

	public void setVariants(Set<ShortVariantProductData> variants) {
		this.variants = variants;
	}
}
