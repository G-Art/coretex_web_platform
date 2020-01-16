package com.coretex.commerce.core.services;

import com.coretex.core.activeorm.services.PageableSearchResult;
import com.coretex.items.cx_core.CategoryItem;
import com.coretex.items.cx_core.ProductItem;

import java.util.UUID;
import java.util.stream.Stream;

public interface CategoryService extends GenericItemService<CategoryItem> {
	Stream<CategoryItem> listByParent(CategoryItem category);

	Stream<CategoryItem> listByRoot();

	Stream<CategoryItem> listByParent(UUID categoryUuid);

	PageableSearchResult<ProductItem> categoryPage(String code, long count, long page);
}
