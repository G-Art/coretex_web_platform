package com.coretex.commerce.facades;

import com.coretex.commerce.data.CategoryHierarchyData;
import com.coretex.commerce.data.minimal.MinimalCategoryData;
import com.coretex.items.commerce_core_model.CategoryItem;

import java.util.List;
import java.util.UUID;

public interface CategoryFacade extends PageableDataTableFacade<CategoryItem, MinimalCategoryData> {

	List<CategoryHierarchyData> categoryHierarchyLeverByNodeUUID(UUID uuid);

	Long count();

	void setParent(UUID category, UUID parent);
}
