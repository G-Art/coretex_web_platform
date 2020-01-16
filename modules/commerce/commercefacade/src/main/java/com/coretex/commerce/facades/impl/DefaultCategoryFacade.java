package com.coretex.commerce.facades.impl;

import com.coretex.commerce.core.services.CategoryService;
import com.coretex.commerce.core.services.PageableService;
import com.coretex.commerce.data.CategoryHierarchyData;
import com.coretex.commerce.data.SearchPageResult;
import com.coretex.commerce.data.minimal.MinimalCategoryData;
import com.coretex.commerce.data.minimal.MinimalCategoryHierarchyData;
import com.coretex.commerce.facades.CategoryFacade;
import com.coretex.commerce.mapper.GenericDataMapper;
import com.coretex.commerce.mapper.minimal.MinimalCategoryDataMapper;
import com.coretex.commerce.mapper.minimal.MinimalCategoryHierarchyDataMapper;
import com.coretex.core.activeorm.services.PageableSearchResult;
import com.coretex.items.cx_core.CategoryItem;
import com.coretex.items.cx_core.ProductItem;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class DefaultCategoryFacade implements CategoryFacade {

	@Resource
	private CategoryService categoryService;

	@Resource
	private MinimalCategoryDataMapper minimalCategoryDataMapper;

	@Resource
	private MinimalCategoryHierarchyDataMapper minimalCategoryHierarchyDataMapper;

	@Override
	public List<CategoryHierarchyData> categoryHierarchyLeverByNodeUUID(UUID uuid) {
		return categoryService.listByParent(uuid)
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
	public Stream<MinimalCategoryHierarchyData> rootCategories() {
		return categoryService
				.listByRoot()
				.map(minimalCategoryHierarchyDataMapper::fromItem);
	}

	@Override
	public SearchPageResult getCategoryPage(String code, int page, int size) {
		PageableSearchResult<ProductItem> searchResult = categoryService.categoryPage(code, size, page);
		SearchPageResult searchPageResult = new SearchPageResult();
		searchPageResult.setPage(page);
		searchPageResult.setCount(size);
		searchPageResult.setTotalCount(searchResult.getTotalCount().intValue());
		searchPageResult.setTotalPages(searchResult.getTotalPages());

		return searchPageResult;
	}

	@Override
	public PageableService<CategoryItem> getPageableService() {
		return categoryService;
	}

	@Override
	public GenericDataMapper<CategoryItem, MinimalCategoryData> getDataMapper() {
		return minimalCategoryDataMapper;
	}
}
