package com.coretex.commerce.facades.impl;

import com.coretex.commerce.core.services.CategoryService;
import com.coretex.commerce.core.services.PageableService;
import com.coretex.commerce.core.services.ProductService;
import com.coretex.commerce.data.BreadcrumbData;
import com.coretex.commerce.data.DataTableResults;
import com.coretex.commerce.data.ProductData;
import com.coretex.commerce.data.SearchPageResult;
import com.coretex.commerce.data.ShortProductData;
import com.coretex.commerce.data.forms.ProductForm;
import com.coretex.commerce.data.minimal.MinimalProductData;
import com.coretex.commerce.facades.ProductFacade;
import com.coretex.commerce.mapper.GenericDataMapper;
import com.coretex.commerce.mapper.ProductDataMapper;
import com.coretex.commerce.mapper.VariantProductDataMapper;
import com.coretex.commerce.mapper.forms.ProductFormMapper;
import com.coretex.commerce.mapper.forms.VariantProductMapper;
import com.coretex.commerce.mapper.minimal.MinimalProductDataMapper;
import com.coretex.items.cx_core.CategoryItem;
import com.coretex.items.cx_core.ProductItem;
import com.coretex.items.cx_core.VariantProductItem;
import com.coretex.searchengine.solr.client.search.SolrSearchRequest;
import com.coretex.searchengine.solr.client.search.SolrSearchService;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.IteratorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service("productFacade")
public class DefaultProductFacade implements ProductFacade {

	@Resource
	private ProductService productService;

	@Resource
	private CategoryService categoryService;

	@Resource
	private MinimalProductDataMapper minimalProductDataMapper;

	@Resource
	private ProductDataMapper productDataMapper;

	@Resource
	private VariantProductDataMapper variantProductDataMapper;

	@Resource
	private ProductFormMapper productFormMapper;

	@Resource
	private SolrSearchService solrSearchService;

	@Resource
	private Map<String, VariantProductMapper<?>> variantMappers;

	private static final Logger LOG = LoggerFactory.getLogger(DefaultProductFacade.class);

	@Override
	public SearchPageResult getCategoryPage(String code, int page, int size, Map<String, List<String>> filter, Map<String, List<String>> sort) {
		var solrSearchRequest = new SolrSearchRequest<>(VariantProductItem.class);
		solrSearchRequest.putFilter("category", code);

		if(Objects.nonNull(filter)){
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

		var category = categoryService.findByCode(code);

		var categories = buildBreadcrumbData(category);
		categories.get(categories.size() - 1).setActive(true);

		searchPageResult.setBreadcrumb(categories.toArray(new BreadcrumbData[0]));
		searchPageResult.setProducts(search.getResult());
		searchPageResult.setFacets(search.getFacets());
		return searchPageResult;
	}

	List<BreadcrumbData> buildBreadcrumbData(CategoryItem categoryItem) {
		var categories = Lists.<BreadcrumbData>newLinkedList();
		if (Objects.nonNull(categoryItem.getParent())) {
			categories.addAll(buildBreadcrumbData(categoryItem.getParent()));
		} else {
			categories.add(new BreadcrumbData("/", "Home"));
		}
		categories.add(new BreadcrumbData("/category/" + categoryItem.getCode(), categoryItem.getName()));

		return categories;
	}

	@Override
	public ProductData getByCode(String code) {
		var productItem = productService.getByCode(code);
		if (productItem instanceof VariantProductItem) {
			return variantProductDataMapper.fromItem((VariantProductItem) productItem);
		}
		return productDataMapper.fromItem(productService.getByCode(code));
	}

	@Override
	public ProductData getByUUID(UUID uuid) {
		var productItem = productService.getByUUID(uuid);
		if (productItem instanceof VariantProductItem) {
			return variantProductDataMapper.fromItem((VariantProductItem) productItem);
		}
		return productDataMapper.fromItem(productService.getByUUID(uuid));
	}

	@Override
	public DataTableResults<MinimalProductData> getVariantsForProduct(UUID uuid, String draw, long page, Long length) {
		var variants = productService.variants(uuid, length, page);
		var dataTableResults = new DataTableResults<MinimalProductData>();
		dataTableResults.setDraw(draw);
		dataTableResults.setRecordsTotal(String.valueOf(variants.getTotalCount()));
		dataTableResults.setRecordsFiltered(String.valueOf(variants.getTotalCount()));
		dataTableResults.setListOfDataObjects(variants.getResult()
				.stream()
				.map(getDataMapper()::fromItem)
				.collect(Collectors.toList()));
		return dataTableResults;
	}

	@Override
	public Long count() {
		return productService.count();
	}

	@Override
	public Stream<ProductData> getAll() {
		return productService.listReactive()
				.map(productDataMapper::fromItem);
	}

	@Override
	public ProductItem save(ProductForm productForm, UUID uuid) {
		ProductItem productItem;
		if (Objects.nonNull(productForm.getVariantType())) {
			productItem = variantMappers.get(productForm.getVariantType()).toItem(productForm);
			var baseProduct = productService.getByUUID(uuid);
			((VariantProductItem) productItem).setBaseProduct(baseProduct);

		} else {
			productItem = productFormMapper.toItem(productForm);
		}

		productService.save(productItem);
		return productItem;
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
