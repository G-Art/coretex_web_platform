package com.coretex.commerce.web.controllers;

import com.coretex.commerce.data.SearchPageResult;
import com.coretex.commerce.data.minimal.MinimalCategoryHierarchyData;
import com.coretex.commerce.facades.CategoryFacade;
import com.coretex.commerce.facades.ProductFacade;
import com.coretex.commerce.web.resolvers.GroupedParametersMap;
import com.coretex.commerce.web.resolvers.Group;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.Objects;

@RestController
@RequestMapping("/v1/categories")
public class CategoryController extends SearchController {

	@Resource
	private CategoryFacade categoryFacade;

	@Resource
	private ProductFacade productFacade;

	@GetMapping(path = "/menu")
	private Flux<MinimalCategoryHierarchyData> getMenuCategories() {
		return Flux.fromStream(categoryFacade::rootCategories);
	}


	@GetMapping(path = {"/{code}/page", "/{code}/page/{page}"})
	private Mono<SearchPageResult> getCategoryPage(@PathVariable(value = "code") String code,
												   @PathVariable(value = "page", required = false) Integer page,
												   @RequestParam(value = "size", required = false, defaultValue = "12") int size,
												   @GroupedParametersMap("f") Group f,
												   @GroupedParametersMap("s") Group s) {
		return Mono.just(productFacade.getCategoryPage(code, Objects.isNull(page) ? 0 : page, size, f.getGroupedParams(), s.getGroupedParams()));
	}
}
