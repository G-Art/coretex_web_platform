package com.coretex.commerce.admin.facades.impl;

import com.coretex.commerce.admin.controllers.DataTableResults;
import com.coretex.commerce.admin.data.CategoryHierarchyData;
import com.coretex.commerce.admin.data.MinimalCategoryData;
import com.coretex.commerce.admin.data.MinimalOrderData;
import com.coretex.commerce.admin.facades.CategoryFacade;
import com.coretex.commerce.admin.facades.OrderFacade;
import com.coretex.commerce.admin.mapper.MinimalCategoryDataMapper;
import com.coretex.commerce.admin.mapper.MinimalOrderDataMapper;
import com.coretex.core.business.services.catalog.category.CategoryService;
import com.coretex.core.business.services.order.OrderService;
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
	public DataTableResults<MinimalCategoryData> tableResult(String draw, long page, Long length) {
		var pageableList = categoryService.pageableList(length, page);
		DataTableResults dataTableResults = new DataTableResults();
		dataTableResults.setDraw(draw);
		dataTableResults.setRecordsTotal(String.valueOf(pageableList.getTotalCount()));
		dataTableResults.setRecordsFiltered(String.valueOf(pageableList.getCount()));
		dataTableResults.setListOfDataObjects(pageableList.getResult()
				.stream()
				.map(minimalCategoryDataMapper::fromItem)
				.collect(Collectors.toList()));
		return dataTableResults;
	}

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
}
