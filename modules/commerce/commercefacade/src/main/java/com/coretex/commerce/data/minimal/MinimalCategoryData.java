package com.coretex.commerce.data.minimal;

import com.coretex.commerce.data.GenericItemData;

public class MinimalCategoryData extends GenericItemData {

	private String code;

	private String name;

	private Boolean visible;

	private Integer productCount;

	private String store;

	private Integer subCategoriesCount;

	private MinimalCategoryData parent;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getVisible() {
		return visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

	public Integer getProductCount() {
		return productCount;
	}

	public void setProductCount(Integer productCount) {
		this.productCount = productCount;
	}

	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		this.store = store;
	}

	public MinimalCategoryData getParent() {
		return parent;
	}

	public Integer getSubCategoriesCount() {
		return subCategoriesCount;
	}

	public void setSubCategoriesCount(Integer subCategoriesCount) {
		this.subCategoriesCount = subCategoriesCount;
	}

	public void setParent(MinimalCategoryData parent) {
		this.parent = parent;
	}
}
