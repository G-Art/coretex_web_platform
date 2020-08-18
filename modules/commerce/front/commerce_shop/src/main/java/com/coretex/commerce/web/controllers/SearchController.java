package com.coretex.commerce.web.controllers;

import com.coretex.commerce.data.SearchPageResult;
import com.coretex.commerce.facades.SearchFacade;
import com.coretex.commerce.web.resolvers.Group;
import com.coretex.commerce.web.resolvers.GroupedParametersMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.Objects;

@RestController
@RequestMapping("/v1/search")
public class SearchController {


	@Resource
	private SearchFacade searchFacade;

	@GetMapping(path = {"/page", "/page/{page}"})
	private Mono<SearchPageResult> getCategoryPage(@RequestParam(value = "q") String query,
												   @PathVariable(value = "page", required = false) Integer page,
												   @RequestParam(value = "size", required = false, defaultValue = "12") int size,
												   @GroupedParametersMap("f") Group f,
												   @GroupedParametersMap("s") Group s) {
		return Mono.just(searchFacade.getSearchPage(query, Objects.isNull(page) ? 0 : page, size, f.getGroupedParams(), s.getGroupedParams()));
	}
}
