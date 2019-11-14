package com.coretex.shop.model.catalog.category;

import java.io.Serializable;

public class CategoryEntity extends Category implements Serializable {


	private static final long serialVersionUID = 1L;


	private Integer sortOrder;
	private Boolean visible;
	private Boolean featured;
	private String lineage;
	private Integer depth;
	private Category parent;


	public Integer getSortOrder() {
		return sortOrder == null ? 0 : sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public String getLineage() {
		return lineage;
	}

	public void setLineage(String lineage) {
		this.lineage = lineage;
	}

	public Integer getDepth() {
		return depth;
	}

	public void setDepth(Integer depth) {
		this.depth = depth;
	}

	public Category getParent() {
		return parent;
	}

	public void setParent(Category parent) {
		this.parent = parent;
	}

	public Boolean isFeatured() {
		return featured;
	}

	public void setFeatured(Boolean featured) {
		this.featured = featured;
	}

}
