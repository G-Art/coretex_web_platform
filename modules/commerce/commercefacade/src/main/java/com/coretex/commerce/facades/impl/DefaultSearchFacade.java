package com.coretex.commerce.facades.impl;

import com.coretex.commerce.data.BreadcrumbData;
import com.coretex.commerce.data.SearchPageResult;
import com.coretex.commerce.data.ShortProductData;
import com.coretex.commerce.facades.SearchFacade;
import com.coretex.items.cx_core.VariantProductItem;
import com.coretex.searchengine.solr.client.search.SolrSearchRequest;
import com.coretex.searchengine.solr.client.search.SolrSearchService;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.IteratorUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service("searchFacade")
public class DefaultSearchFacade implements SearchFacade {

	@Resource
	private SolrSearchService solrSearchService;

	@Override
	public SearchPageResult getSearchPage(String text, int page, int size, Map<String, List<String>> filter, Map<String, List<String>> sort) {
		var solrSearchRequest = new SolrSearchRequest<>(VariantProductItem.class);
		solrSearchRequest.setSearchText(text);
		if (Objects.nonNull(filter)) {
			filter.forEach(solrSearchRequest::putAllFilters);
		}

		solrSearchRequest.setPage(page);
		if (!sort.isEmpty()) {
			var first = IteratorUtils.first(sort.entrySet().iterator());
			var order = IteratorUtils.first(first.getValue().iterator());
			solrSearchRequest.setSort(first.getKey(), order);
		}
		solrSearchRequest.setCount(size);
		var search = solrSearchService.<ShortProductData>search(solrSearchRequest);
		SearchPageResult searchPageResult = new SearchPageResult();
		searchPageResult.setPage(page);
		searchPageResult.setCount(size);
		searchPageResult.setTotalCount(search.getTotalCount().intValue());
		searchPageResult.setTotalPages(search.getTotalPages());

		List<BreadcrumbData> breadcrumbData = Lists.newLinkedList();
		breadcrumbData.add(new BreadcrumbData("/", "Home"));
		BreadcrumbData breadcrumb = new BreadcrumbData("/", "search: " +text);
		breadcrumb.setActive(true);
		breadcrumbData.add(breadcrumb);
		searchPageResult.setBreadcrumb(breadcrumbData.toArray(new BreadcrumbData[0]));
		searchPageResult.setProducts(search.getResult());
		searchPageResult.setFacets(search.getFacets());
		return searchPageResult;
	}
}
