package com.coretex.commerce.data;

import com.coretex.items.cx_core.CategoryItem;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.collections4.CollectionUtils;

public class CategoryHierarchyData{

	private transient CategoryItem categoryItem;

	public CategoryHierarchyData(CategoryItem categoryItem) {
		this.categoryItem = categoryItem;
	}

	@JsonProperty("id")
	public String getId(){
		return categoryItem.getUuid().toString();
	}

	@JsonProperty("text")
	public String getText(){
		return categoryItem.getName();
	}

	@JsonProperty("children")
	private Boolean getChildren(){
		return CollectionUtils.isNotEmpty(categoryItem.getCategories());
	}

	@JsonProperty("type")
	private String getType(){
		if(categoryItem.getVisible()){
			return "active";
		}else{
			return "notActive";
		}
	}
}
