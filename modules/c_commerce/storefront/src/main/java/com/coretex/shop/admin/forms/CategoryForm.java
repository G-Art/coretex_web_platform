package com.coretex.shop.admin.forms;

import com.coretex.shop.admin.data.CategoryDto;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

public class CategoryForm implements Serializable {

	private UUID uuid;
	private String code;

	private Map<String, String> name;
	private Map<String, String> title;
	private Map<String, String> description;
	private Map<String, String> categoryHighlight;
	private Map<String, String> metaTitle;
	private Map<String, String> metaKeywords;
	private Map<String, String> metaDescription;

	private Boolean featured = false;
	private Boolean visible = true;
	private Boolean categoryStatus;
	private String seUrl;

	private Integer sortOrder;
	private Integer depth;

	private CategoryDto parent;

	private String lineage;

	public CategoryDto getParent() {
		return parent;
	}

	public void setParent(CategoryDto parent) {
		this.parent = parent;
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Map<String, String> getName() {
		return name;
	}

	public void setName(Map<String, String> name) {
		this.name = name;
	}

	public Map<String, String> getTitle() {
		return title;
	}

	public void setTitle(Map<String, String> title) {
		this.title = title;
	}

	public Map<String, String> getDescription() {
		return description;
	}

	public void setDescription(Map<String, String> description) {
		this.description = description;
	}

	public Map<String, String> getCategoryHighlight() {
		return categoryHighlight;
	}

	public void setCategoryHighlight(Map<String, String> categoryHighlight) {
		this.categoryHighlight = categoryHighlight;
	}

	public Map<String, String> getMetaTitle() {
		return metaTitle;
	}

	public void setMetaTitle(Map<String, String> metaTitle) {
		this.metaTitle = metaTitle;
	}

	public Map<String, String> getMetaKeywords() {
		return metaKeywords;
	}

	public void setMetaKeywords(Map<String, String> metaKeywords) {
		this.metaKeywords = metaKeywords;
	}

	public Map<String, String> getMetaDescription() {
		return metaDescription;
	}

	public void setMetaDescription(Map<String, String> metaDescription) {
		this.metaDescription = metaDescription;
	}

	public Boolean getFeatured() {
		return featured;
	}

	public void setFeatured(Boolean featured) {
		this.featured = featured;
	}

	public Boolean getVisible() {
		return visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

	public Boolean getCategoryStatus() {
		return categoryStatus;
	}

	public void setCategoryStatus(Boolean categoryStatus) {
		this.categoryStatus = categoryStatus;
	}

	public String getSeUrl() {
		return seUrl;
	}

	public void setSeUrl(String seUrl) {
		this.seUrl = seUrl;
	}

	public Integer getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

	public Integer getDepth() {
		return depth;
	}

	public void setDepth(Integer depth) {
		this.depth = depth;
	}

	public String getLineage() {
		return lineage;
	}

	public void setLineage(String lineage) {
		this.lineage = lineage;
	}
}
