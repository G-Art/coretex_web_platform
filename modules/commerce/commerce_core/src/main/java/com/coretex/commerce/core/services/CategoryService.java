package com.coretex.commerce.core.services;

import com.coretex.items.cx_core.CategoryItem;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface CategoryService extends GenericItemService<CategoryItem> {
	Flux<CategoryItem> listByParent(CategoryItem category);

	Flux<CategoryItem> listByRoot();

	Flux<CategoryItem> listByParent(UUID categoryUuid);

	CategoryItem findByCode(String code);

}
