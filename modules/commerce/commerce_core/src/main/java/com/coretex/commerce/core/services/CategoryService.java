package com.coretex.commerce.core.services;

import com.coretex.items.cx_core.CategoryItem;

import java.util.UUID;
import java.util.stream.Stream;

public interface CategoryService extends GenericItemService<CategoryItem> {
	Stream<CategoryItem> listByParent(CategoryItem category);

	Stream<CategoryItem> listByRoot();

	Stream<CategoryItem> listByParent(UUID categoryUuid);

	CategoryItem findByCode(String code);

}
