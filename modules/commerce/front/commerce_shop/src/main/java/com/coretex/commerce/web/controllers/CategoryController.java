package com.coretex.commerce.web.controllers;

import com.coretex.commerce.data.minimal.MinimalCategoryHierarchyData;
import com.coretex.commerce.facades.CategoryFacade;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;

@RestController
@RequestMapping("/v1/categories")
public class CategoryController {

	@Resource
	private CategoryFacade categoryFacade;

	@GetMapping(path = "/menu")
	private Flux<MinimalCategoryHierarchyData> getMenuCategories()
	{
		return Flux.fromStream(categoryFacade::rootCategories);
	}
}
