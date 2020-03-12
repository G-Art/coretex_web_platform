package com.coretex.commerce.facades;

import com.coretex.commerce.data.CategoryHierarchyData;
import com.coretex.commerce.data.minimal.MinimalCategoryData;
import com.coretex.commerce.data.minimal.MinimalCategoryHierarchyData;
import com.coretex.items.cx_core.CategoryItem;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public interface CategoryFacade extends PageableDataTableFacade<CategoryItem, MinimalCategoryData> {

	List<CategoryHierarchyData> categoryHierarchyLeverByNodeUUID(UUID uuid);

	Long count();

	void setParent(UUID category, UUID parent);

	Stream<MinimalCategoryHierarchyData> rootCategories();

	List<MinimalCategoryData> categories();


}
