package com.coretex.commerce.facades.impl;

import com.coretex.commerce.core.services.PageableService;
import com.coretex.commerce.core.services.ProductService;
import com.coretex.commerce.data.BreadcrumbData;
import com.coretex.commerce.data.ProductData;
import com.coretex.commerce.data.SearchPageResult;
import com.coretex.commerce.data.minimal.MinimalProductData;
import com.coretex.commerce.facades.ProductFacade;
import com.coretex.commerce.mapper.GenericDataMapper;
import com.coretex.commerce.mapper.ShortProductDataMapper;
import com.coretex.commerce.mapper.minimal.MinimalProductDataMapper;
import com.coretex.core.activeorm.services.PageableSearchResult;
import com.coretex.items.cx_core.ProductItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service("productFacade")
public class DefaultProductFacade implements ProductFacade {

	@Resource
	private ProductService productService;

	@Resource
	private MinimalProductDataMapper minimalProductDataMapper;

	@Resource
	private ShortProductDataMapper shortProductDataMapper;

	private static final Logger LOG = LoggerFactory.getLogger(DefaultProductFacade.class);

	@Override
	public SearchPageResult getCategoryPage(String code, int page, int size) {
		PageableSearchResult<ProductItem> searchResult = productService.categoryPage(code, size, page);
		SearchPageResult searchPageResult = new SearchPageResult();
		searchPageResult.setPage(page);
		searchPageResult.setCount(size);
		searchPageResult.setTotalCount(searchResult.getTotalCount().intValue());
		searchPageResult.setTotalPages(searchResult.getTotalPages());

		var bc = new BreadcrumbData[2];
		bc[0] = new BreadcrumbData("/", "Home");
		bc[1] = new BreadcrumbData(true, "Technical");

		searchPageResult.setBreadcrumb(bc);

		searchPageResult.setProducts(searchResult.getResultStream()
				.map(shortProductDataMapper::fromItem)
				.collect(Collectors.toList()));

		return searchPageResult;
	}

	@Override
	public ProductData getByCode(String code) {
		return null;
	}

	@Override
	public Long count() {
		return productService.count();
	}

	@Override
	public List<ProductData> getAll() {
		return null;
	}


	@Override
	public PageableService<ProductItem> getPageableService() {
		return productService;
	}

	@Override
	public GenericDataMapper<ProductItem, MinimalProductData> getDataMapper() {
		return minimalProductDataMapper;
	}

}
