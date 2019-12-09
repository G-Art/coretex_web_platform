package com.coretex.commerce.admin.facades.impl;

import com.coretex.commerce.admin.data.CategoryHierarchyData;
import com.coretex.commerce.admin.data.minimal.MinimalCategoryData;
import com.coretex.commerce.admin.facades.CategoryFacade;
import com.coretex.commerce.admin.mapper.GenericDataMapper;
import com.coretex.commerce.admin.mapper.minimal.MinimalCategoryDataMapper;
import com.coretex.core.business.services.catalog.category.CategoryService;
import com.coretex.core.business.services.common.generic.PageableEntityService;
import com.coretex.items.commerce_core_model.CategoryItem;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class DefaultCategoryFacade implements CategoryFacade {

	@Resource
	private CategoryService categoryService;

	@Resource
	private MinimalCategoryDataMapper minimalCategoryDataMapper;

	@Override
	public List<CategoryHierarchyData> categoryHierarchyLeverByNodeUUID(UUID uuid) {
		return categoryService.listByParent(uuid)
				.stream()
				.map(CategoryHierarchyData::new)
				.collect(Collectors.toList());
	}

	@Override
	public Long count() {
		return categoryService.count();
	}

	@Override
	public void setParent(UUID category, UUID parent) {
		var cat = categoryService.getByUUID(category);
		if(Objects.isNull(parent)){
			cat.setParent(null);
		}else {
			var p = categoryService.getByUUID(parent);
			cat.setParent(p);
		}
		categoryService.save(cat);
	}

	@Override
	public PageableEntityService<CategoryItem> getPageableService() {
		return categoryService;
	}

	@Override
	public GenericDataMapper<CategoryItem, MinimalCategoryData> getDataMapper() {
		return minimalCategoryDataMapper;
	}
}
