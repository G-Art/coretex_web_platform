package com.coretex.commerce.data.minimal;

import com.coretex.commerce.data.GenericItemData;

import java.util.List;

public class MinimalCategoryHierarchyData extends GenericItemData {

	private String code;

	private String name;

	private Boolean visible;

	private Boolean root;

	private List<MinimalCategoryHierarchyData> children;

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

	public Boolean getRoot() {
		return root;
	}

	public void setRoot(Boolean root) {
		this.root = root;
	}

	public List<MinimalCategoryHierarchyData> getChildren() {
		return children;
	}

	public void setChildren(List<MinimalCategoryHierarchyData> children) {
		this.children = children;
	}
}
