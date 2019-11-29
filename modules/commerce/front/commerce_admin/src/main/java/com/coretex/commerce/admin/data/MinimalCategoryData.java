package com.coretex.commerce.admin.data;

public class MinimalCategoryData extends GenericItemData {

	private String code;

	private String name;

	private Boolean visible;

	private Integer productCount;

	private String merchantStore;

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

	public String getMerchantStore() {
		return merchantStore;
	}

	public void setMerchantStore(String merchantStore) {
		this.merchantStore = merchantStore;
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
