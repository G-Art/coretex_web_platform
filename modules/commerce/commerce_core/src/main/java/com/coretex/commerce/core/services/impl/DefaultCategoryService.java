package com.coretex.commerce.core.services.impl;

import com.coretex.commerce.core.dao.CategoryDao;
import com.coretex.commerce.core.services.AbstractGenericItemService;
import com.coretex.commerce.core.services.CategoryService;
import com.coretex.items.cx_core.CategoryItem;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class DefaultCategoryService extends AbstractGenericItemService<CategoryItem> implements CategoryService {

	private CategoryDao categoryDao;

	public DefaultCategoryService(CategoryDao repository) {
		super(repository);
		this.categoryDao = repository;
	}

	@Override
	public Stream<CategoryItem> listByParent(CategoryItem category) {
		return listByParent(Objects.isNull(category) ? null : category.getUuid());
	}

	@Override
	public Stream<CategoryItem> listByRoot() {
		return categoryDao.findByParent(null);
	}

	@Override
	public Stream<CategoryItem> listByParent(UUID categoryUuid) {
		return categoryDao.findByParent(categoryUuid);
	}

	@Override
	public CategoryItem findByCode(String code) {
		return categoryDao.findSingle(Map.of(CategoryItem.CODE, code), true);
	}

}
