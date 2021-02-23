package com.coretex.commerce.core.services.impl;

import com.coretex.commerce.core.dao.CategoryDao;
import com.coretex.commerce.core.services.AbstractGenericItemService;
import com.coretex.commerce.core.services.CategoryService;
import com.coretex.items.cx_core.CategoryItem;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
public class DefaultCategoryService extends AbstractGenericItemService<CategoryItem> implements CategoryService {

	private CategoryDao categoryDao;

	public DefaultCategoryService(CategoryDao repository) {
		super(repository);
		this.categoryDao = repository;
	}

	@Override
	public Flux<CategoryItem> listByParent(CategoryItem category) {
		return listByParent(Objects.isNull(category) ? null : category.getUuid());
	}

	@Override
	public Flux<CategoryItem> listByRoot() {
		return categoryDao.findByParent(null);
	}

	@Override
	public Flux<CategoryItem> listByParent(UUID categoryUuid) {
		return categoryDao.findByParent(categoryUuid);
	}

	@Override
	public CategoryItem findByCode(String code) {
		return categoryDao.findSingle(Map.of(CategoryItem.CODE, code), true);
	}

}
