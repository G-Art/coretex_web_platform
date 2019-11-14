package com.coretex.shop.store.controller.search.facade;

import com.coretex.core.business.exception.ConversionException;
import com.coretex.core.business.exception.ServiceException;
import com.coretex.items.commerce_core_model.CategoryItem;
import com.coretex.items.commerce_core_model.ProductItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.shop.store.api.exception.ConversionRuntimeException;
import com.coretex.shop.store.api.exception.ServiceRuntimeException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.UUID;
import java.util.stream.Collectors;
import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.coretex.core.business.services.catalog.category.CategoryService;
import com.coretex.core.business.services.catalog.product.PricingService;
import com.coretex.core.business.services.catalog.product.ProductService;
import com.coretex.core.business.services.search.SearchService;
import com.coretex.core.business.utils.CoreConfiguration;
import com.coretex.core.model.catalog.product.ProductCriteria;
import com.coretex.core.model.catalog.product.ProductList;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.core.model.search.IndexProduct;
import com.coretex.core.model.search.SearchEntry;
import com.coretex.core.model.search.SearchFacet;
import com.coretex.core.model.search.SearchKeywords;
import com.coretex.core.model.search.SearchResponse;
import com.coretex.shop.model.ValueList;
import com.coretex.shop.model.catalog.SearchProductList;
import com.coretex.shop.model.catalog.SearchProductRequest;
import com.coretex.shop.model.catalog.category.ReadableCategory;
import com.coretex.shop.model.catalog.product.ReadableProduct;
import com.coretex.shop.populator.catalog.ReadableCategoryPopulator;
import com.coretex.shop.populator.catalog.ReadableProductPopulator;
import com.coretex.shop.store.model.search.AutoCompleteRequest;
import com.coretex.shop.utils.ImageFilePath;


@Service("searchFacade")
public class SearchFacadeImpl implements SearchFacade {

	@Resource
	private SearchService searchService;

	@Resource
	private ProductService productService;

	@Resource
	private CategoryService categoryService;

	@Resource
	private PricingService pricingService;

	@Resource
	@Qualifier("img")
	private ImageFilePath imageUtils;

	@Resource
	private CoreConfiguration coreConfiguration;


	private final static String CATEGORY_FACET_NAME = "categories";
	private final static String MANUFACTURER_FACET_NAME = "manufacturer";
	private final static int AUTOCOMPLETE_ENTRIES_COUNT = 15;

	/**
	 * Index all products from the catalogue
	 * Better stop the system, remove ES indexex manually
	 * restart ES and run this query
	 */
	@Override
	@Async
	public void indexAllData(MerchantStoreItem store) throws Exception {


		List<ProductItem> products = productService.listByStore(store);

		for (ProductItem product : products) {
			searchService.index(store, product);
		}

	}

	@Override
	public SearchProductList search(MerchantStoreItem store, LanguageItem language, SearchProductRequest searchRequest) {
		String query = String.format(coreConfiguration.getProperty("SEARCH_QUERY"), searchRequest.getQuery());
		SearchResponse response = search(store, language.getCode(), query, searchRequest.getCount(), searchRequest.getStart());
		return copySearchResponse(response, store, searchRequest.getStart(), searchRequest.getCount(), language);
	}

	private SearchResponse search(
			MerchantStoreItem store, String languageCode, String query, Integer count, Integer start) {
		try {
			return searchService.search(store, languageCode, query, count, start);
		} catch (ServiceException e) {
			throw new ServiceRuntimeException(e);
		}
	}

	@Override
	public SearchProductList copySearchResponse(SearchResponse searchResponse, MerchantStoreItem merchantStore, int start, int count, LanguageItem language) {

		SearchProductList returnList = new SearchProductList();
		List<SearchEntry> entries = searchResponse.getEntries();

		if (!CollectionUtils.isEmpty(entries)) {
			List<UUID> ids = new ArrayList<>();
			for (SearchEntry entry : entries) {
				IndexProduct indexedProduct = entry.getIndexProduct();
				UUID id = UUID.fromString(indexedProduct.getId());

				//No highlights	
				ids.add(id);
			}

			ProductCriteria searchCriteria = new ProductCriteria();
			searchCriteria.setMaxCount(count);
			searchCriteria.setStartIndex(start);
			searchCriteria.setProductIds(ids);
			searchCriteria.setAvailable(true);

			ProductList productList = productService.listByStore(merchantStore, language, searchCriteria);

			List<ReadableProduct> readableProducts = productList.getProducts()
					.stream()
					.map(product -> convertProductToReadableProduct(product, merchantStore, language))
					.collect(Collectors.toList());

			returnList.getProducts().addAll(readableProducts);
			returnList.setProductCount(productList.getProducts().size());
		}

		//Facets
		Map<String, List<SearchFacet>> facets = searchResponse.getFacets();
		List<SearchFacet> categoriesFacets = null;
		List<SearchFacet> manufacturersFacets = null;
		if (facets != null) {
			for (String key : facets.keySet()) {
				//supports category and manufacturer
				if (CATEGORY_FACET_NAME.equals(key)) {
					categoriesFacets = facets.get(key);
				}

				if (MANUFACTURER_FACET_NAME.equals(key)) {
					manufacturersFacets = facets.get(key);
				}
			}


			if (!CollectionUtils.isEmpty(categoriesFacets)) {
				List<String> categoryCodes = new ArrayList<String>();
				Map<String, Long> productCategoryCount = new HashMap<String, Long>();
				for (SearchFacet facet : categoriesFacets) {
					categoryCodes.add(facet.getName());
					productCategoryCount.put(facet.getKey(), facet.getCount());
				}

				List<CategoryItem> categories = categoryService.listByCodes(merchantStore, categoryCodes, language);
				List<ReadableCategory> categoryProxies = categories
						.stream()
						.map(category -> convertCategoryToReadableCategory(merchantStore, language,
								productCategoryCount, category))
						.collect(Collectors.toList());
				returnList.setCategoryFacets(categoryProxies);
			}

			//todo manufacturer facets
			if (manufacturersFacets != null) {

			}


		}

		return returnList;
	}

	private ReadableCategory convertCategoryToReadableCategory(MerchantStoreItem merchantStore,
															   LanguageItem language, Map<String, Long> productCategoryCount, CategoryItem category) {
		ReadableCategoryPopulator populator = new ReadableCategoryPopulator();
		try {
			ReadableCategory categoryProxy = populator.populate(category, new ReadableCategory(), merchantStore, language);
			Long total = productCategoryCount.get(categoryProxy.getCode());
			if (total != null) {
				categoryProxy.setProductCount(total.intValue());
			}
			return categoryProxy;
		} catch (ConversionException e) {
			throw new ConversionRuntimeException(e);
		}
	}

	private ReadableProduct convertProductToReadableProduct(ProductItem product, MerchantStoreItem merchantStore,
															LanguageItem language) {

		ReadableProductPopulator populator = new ReadableProductPopulator();
		populator.setPricingService(pricingService);
		populator.setimageUtils(imageUtils);

		try {
			return populator.populate(product, new ReadableProduct(), merchantStore, language);
		} catch (ConversionException e) {
			throw new ConversionRuntimeException(e);
		}
	}

	@Override
	public ValueList autocompleteRequest(String query, MerchantStoreItem store, LanguageItem language) {
		AutoCompleteRequest req = new AutoCompleteRequest(store.getCode(), language.getCode());
		String formattedQuery = String.format(coreConfiguration.getProperty("AUTOCOMPLETE_QUERY"), query);

		/** formatted toJSONString because of te specific field names required in the UI **/
		SearchKeywords keywords = getSearchKeywords(req, formattedQuery);
		ValueList returnList = new ValueList();
		returnList.setValues(keywords.getKeywords());
		return returnList;
	}

	private SearchKeywords getSearchKeywords(AutoCompleteRequest req, String query) {
		return searchService.searchForKeywords(req.getCollectionName(), query, AUTOCOMPLETE_ENTRIES_COUNT);
	}


}
