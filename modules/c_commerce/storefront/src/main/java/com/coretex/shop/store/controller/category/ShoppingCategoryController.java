package com.coretex.shop.store.controller.category;

import com.coretex.core.business.services.catalog.category.CategoryService;
import com.coretex.core.business.services.catalog.product.PricingService;
import com.coretex.core.business.services.catalog.product.ProductService;
import com.coretex.core.business.services.catalog.product.manufacturer.ManufacturerService;
import com.coretex.core.business.services.merchant.MerchantStoreService;
import com.coretex.core.business.services.reference.language.LanguageService;
import com.coretex.core.business.utils.CacheUtils;
import com.coretex.items.commerce_core_model.CategoryItem;
import com.coretex.items.commerce_core_model.ProductItem;
import com.coretex.core.model.catalog.product.ProductCriteria;
import com.coretex.items.commerce_core_model.ManufacturerItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.shop.constants.Constants;
import com.coretex.shop.model.catalog.ProductList;
import com.coretex.shop.model.catalog.category.ReadableCategory;
import com.coretex.shop.model.catalog.manufacturer.ReadableManufacturer;
import com.coretex.shop.model.catalog.product.ReadableProduct;
import com.coretex.shop.model.shop.Breadcrumb;
import com.coretex.shop.model.shop.PageInformation;
import com.coretex.shop.populator.catalog.ReadableCategoryPopulator;
import com.coretex.shop.populator.catalog.ReadableProductPopulator;
import com.coretex.shop.populator.manufacturer.ReadableManufacturerPopulator;
import com.coretex.shop.store.controller.ControllerConstants;
import com.coretex.shop.store.model.filter.QueryFilter;
import com.coretex.shop.store.model.filter.QueryFilterType;
import com.coretex.shop.utils.BreadcrumbsUtils;
import com.coretex.shop.utils.ImageFilePath;
import com.coretex.shop.utils.LabelUtils;
import com.coretex.shop.utils.PageBuilderUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;


/**
 * Renders a given category page based on friendly url
 * Can also filter by facets such as manufacturer
 *
 * @author Carl Samson
 */
@Controller
public class ShoppingCategoryController {


	@Resource
	private CategoryService categoryService;

	@Resource
	private LanguageService languageService;

	@Resource
	private MerchantStoreService merchantStoreService;

	@Resource
	private ProductService productService;

	@Resource
	private ManufacturerService manufacturerService;

	@Resource
	private LabelUtils messages;

	@Resource
	private BreadcrumbsUtils breadcrumbsUtils;

	@Resource
	private CacheUtils cache;

	@Resource
	private PricingService pricingService;

	@Resource
	@Qualifier("img")
	private ImageFilePath imageUtils;


	private static final Logger LOGGER = LoggerFactory.getLogger(ShoppingCategoryController.class);


	/**
	 * @param friendlyUrl
	 * @param ref
	 * @param model
	 * @param request
	 * @param response
	 * @param locale
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/shop/category/{friendlyUrl}.html/ref={ref}")
	public String displayCategoryWithReference(@PathVariable final String friendlyUrl, @PathVariable final String ref, Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {


		return this.displayCategory(friendlyUrl, ref, model, request, response, locale);
	}


	/**
	 * CategoryItem page entry point
	 *
	 * @param friendlyUrl
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/shop/category/{friendlyUrl}.html")
	public String displayCategoryNoReference(@PathVariable final String friendlyUrl, Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {

		return this.displayCategory(friendlyUrl, null, model, request, response, locale);
	}

	@SuppressWarnings("unchecked")
	private String displayCategory(final String friendlyUrl, final String ref, Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {

		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.MERCHANT_STORE);


		//get category
		CategoryItem category = categoryService.getBySeUrl(store, friendlyUrl);

		LanguageItem language = (LanguageItem) request.getAttribute("LANGUAGE");

		if (category == null) {
			LOGGER.error("No category found for friendlyUrl " + friendlyUrl);
			//redirect on page not found
			return PageBuilderUtils.build404(store);

		}

		if (!category.getVisible()) {
			return PageBuilderUtils.buildHomePage(store);
		}

		ReadableCategoryPopulator populator = new ReadableCategoryPopulator();
		ReadableCategory categoryProxy = populator.populate(category, new ReadableCategory(), store, language);

		Breadcrumb breadCrumb = breadcrumbsUtils.buildCategoryBreadcrumb(categoryProxy, store, language, request.getContextPath());
		request.getSession().setAttribute(Constants.BREADCRUMB, breadCrumb);
		request.setAttribute(Constants.BREADCRUMB, breadCrumb);


		//meta information
		PageInformation pageInformation = new PageInformation();
		pageInformation.setPageDescription(categoryProxy.getDescription().getMetaDescription());
		pageInformation.setPageKeywords(categoryProxy.getDescription().getKeyWords());
		pageInformation.setPageTitle(categoryProxy.getDescription().getTitle());
		pageInformation.setPageUrl(categoryProxy.getDescription().getFriendlyUrl());

		//** retrieves category id drill down**//
		String lineage = new StringBuilder().append(category.getLineage()).append(category.getUuid()).append(Constants.CATEGORY_LINEAGE_DELIMITER).toString();


		request.setAttribute(Constants.REQUEST_PAGE_INFORMATION, pageInformation);

		//TODO add to caching
		List<CategoryItem> subCategs = categoryService.getListByLineage(store, lineage);
		List<UUID> subIds = new ArrayList<>();
		if (subCategs != null && subCategs.size() > 0) {
			for (CategoryItem c : subCategs) {
				if (c.getVisible()) {
					subIds.add(c.getUuid());
				}
			}
		}
		subIds.add(category.getUuid());


		StringBuilder subCategoriesCacheKey = new StringBuilder();
		subCategoriesCacheKey
				.append(store.getUuid())
				.append("_")
				.append(category.getUuid())
				.append("_")
				.append(Constants.SUBCATEGORIES_CACHE_KEY)
				.append("-")
				.append(language.getCode());

		StringBuilder subCategoriesMissed = new StringBuilder();
		subCategoriesMissed
				.append(subCategoriesCacheKey.toString())
				.append(Constants.MISSED_CACHE_KEY);

		List<BigDecimal> prices = new ArrayList<>();
		List<ReadableCategory> subCategories = null;
		Map<UUID, Long> countProductsByCategories = null;

		if (store.getUseCache()) {

			//get from the cache
			subCategories = (List<ReadableCategory>) cache.getFromCache(subCategoriesCacheKey.toString());
			if (subCategories == null) {
				//get from missed cache
				//Boolean missedContent = (Boolean)cache.getFromCache(subCategoriesMissed.toString());

				//if(missedContent==null) {
				countProductsByCategories = getProductsByCategory(store, category, lineage, subIds);
				subCategories = getSubCategories(store, category, countProductsByCategories, language, locale);

				if (subCategories != null) {
					cache.putInCache(subCategories, subCategoriesCacheKey.toString());
				} else {
					//cache.putInCache(new Boolean(true), subCategoriesCacheKey.toString());
				}
				//}
			}
		} else {
			countProductsByCategories = getProductsByCategory(store, category, lineage, subIds);
			subCategories = getSubCategories(store, category, countProductsByCategories, language, locale);
		}

		//Parent category
		ReadableCategory parentProxy = null;

		if (category.getParent() != null) {
			CategoryItem parent = categoryService.getById(category.getParent().getUuid());
			parentProxy = populator.populate(parent, new ReadableCategory(), store, language);
		}


		//** List of manufacturers **//
		List<ReadableManufacturer> manufacturerList = getManufacturersByProductAndCategory(store, category, subIds, language);

		model.addAttribute("manufacturers", manufacturerList);
		model.addAttribute("parent", parentProxy);
		model.addAttribute("category", categoryProxy);
		model.addAttribute("subCategories", subCategories);

		if (parentProxy != null) {
			request.setAttribute(Constants.LINK_CODE, parentProxy.getDescription().getFriendlyUrl());
		}


		/** template **/
		StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Category.category).append(".").append(store.getStoreTemplate());

		return template.toString();
	}

	@SuppressWarnings("unchecked")
	private List<ReadableManufacturer> getManufacturersByProductAndCategory(MerchantStoreItem store, CategoryItem category, List<UUID> subCategoryIds, LanguageItem language) throws Exception {

		List<ReadableManufacturer> manufacturerList = null;
		/** List of manufacturers **/
		if (subCategoryIds != null && subCategoryIds.size() > 0) {

			StringBuilder manufacturersKey = new StringBuilder();
			manufacturersKey
					.append(store.getUuid())
					.append("_")
					.append(Constants.MANUFACTURERS_BY_PRODUCTS_CACHE_KEY)
					.append("-")
					.append(language.getCode());

			StringBuilder manufacturersKeyMissed = new StringBuilder();
			manufacturersKeyMissed
					.append(manufacturersKey.toString())
					.append(Constants.MISSED_CACHE_KEY);

			if (store.getUseCache()) {

				//get from the cache

				manufacturerList = (List<ReadableManufacturer>) cache.getFromCache(manufacturersKey.toString());


				if (manufacturerList == null) {
					//get from missed cache
					//Boolean missedContent = (Boolean)cache.getFromCache(manufacturersKeyMissed.toString());
					//if(missedContent==null) {
					manufacturerList = this.getManufacturers(store, subCategoryIds, language);
					if (manufacturerList.isEmpty()) {
						cache.putInCache(new Boolean(true), manufacturersKeyMissed.toString());
					} else {
						//cache.putInCache(manufacturerList, manufacturersKey.toString());
					}
					//}
				}
			} else {
				manufacturerList = this.getManufacturers(store, subCategoryIds, language);
			}
		}
		return manufacturerList;
	}

	private List<ReadableManufacturer> getManufacturers(MerchantStoreItem store, List<UUID> ids, LanguageItem language) throws Exception {
		List<ReadableManufacturer> manufacturerList = new ArrayList<>();
		List<ManufacturerItem> manufacturers = manufacturerService.listByProductsByCategoriesId(store, ids, language);
		if (!manufacturers.isEmpty()) {

			for (ManufacturerItem manufacturer : manufacturers) {
				ReadableManufacturer manuf = new ReadableManufacturerPopulator().populate(manufacturer, new ReadableManufacturer(), store, language);
				manufacturerList.add(manuf);

			}
		}
		return manufacturerList;
	}

	private Map<UUID, Long> getProductsByCategory(MerchantStoreItem store, CategoryItem category, String lineage, List<UUID> subIds) throws Exception {

		if (subIds.isEmpty()) {
			return null;
		}

		List<Map<String, Object>> countProductsByCategories = categoryService.countProductsByCategories(store, subIds);
		Map<UUID, Long> countByCategories = new HashMap<>();

		for (Map<String, Object> counts : countProductsByCategories) {
			Object[] objects = counts.values().toArray();
			CategoryItem c = categoryService.getById((UUID) objects[0]);
			if (c.getParent() != null) {
				if (c.getParent().getUuid() == category.getUuid()) {
					countByCategories.put(c.getUuid(), (Long) objects[1]);
				} else {
					//get lineage
					String lin = c.getLineage();
					String[] categoryPath = lin.split(Constants.CATEGORY_LINEAGE_DELIMITER);
					for (int i = 0; i < categoryPath.length; i++) {
						String sId = categoryPath[i];
						if (!StringUtils.isBlank(sId)) {
							Long count = countByCategories.get(UUID.fromString(sId));
							if (count != null) {
								count = count +  (Long) objects[1];
								countByCategories.put(UUID.fromString(sId), count);
							}
						}
					}
				}
			}
		}

		return countByCategories;

	}

	private List<ReadableCategory> getSubCategories(MerchantStoreItem store, CategoryItem category, Map<UUID, Long> productCount, LanguageItem language, Locale locale) throws Exception {


		//sub categories
		List<CategoryItem> subCategories = categoryService.listByParent(category, language);
		ReadableCategoryPopulator populator = new ReadableCategoryPopulator();
		List<ReadableCategory> subCategoryProxies = new ArrayList<ReadableCategory>();


		for (CategoryItem sub : subCategories) {
			ReadableCategory cProxy = populator.populate(sub, new ReadableCategory(), store, language);
			//com.coretex.web.entity.catalog.CategoryItem cProxy =  catalogUtils.buildProxyCategory(sub, store, locale);
			if (productCount != null) {
				Long total = productCount.get(cProxy.getUuid());
				if (total != null) {
					cProxy.setProductCount(total.intValue());
				}
			}
			subCategoryProxies.add(cProxy);
		}

		return subCategoryProxies;

	}


	/**
	 * Returns all categories for a given MerchantStoreItem
	 */
	@RequestMapping("/services/public/category/{store}/{language}")
	@ResponseBody
	public List<ReadableCategory> getCategories(@PathVariable final String language, @PathVariable final String store, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<String, LanguageItem> langs = languageService.getLanguagesMap();
		LanguageItem l = langs.get(language);
		if (l == null) {
			l = languageService.getByCode(Constants.DEFAULT_LANGUAGE);
		}

		MerchantStoreItem merchantStore = (MerchantStoreItem) request.getAttribute(Constants.MERCHANT_STORE);

		if (merchantStore != null) {
			if (!merchantStore.getCode().equals(store)) {
				merchantStore = null; //reset for the current request
			}
		}

		if (merchantStore == null) {
			merchantStore = merchantStoreService.getByCode(store);
		}

		if (merchantStore == null) {
			LOGGER.error("Merchant store is null for code " + store);
			response.sendError(503, "Merchant store is null for code " + store);//TODO localized message
			return null;
		}

		List<CategoryItem> categories = categoryService.listByStore(merchantStore, l);

		ReadableCategoryPopulator populator = new ReadableCategoryPopulator();

		List<ReadableCategory> returnCategories = new ArrayList<ReadableCategory>();
		for (CategoryItem category : categories) {
			ReadableCategory categoryProxy = populator.populate(category, new ReadableCategory(), merchantStore, l);
			returnCategories.add(categoryProxy);
		}

		return returnCategories;
	}

	/**
	 * Returns an array of products belonging to a given category
	 * in a given language for a given store
	 * url example :  http://<host>/sm-shop/shop/services/public/products/DEFAULT/BOOKS
	 *
	 * @param store
	 * @param language
	 * @param category
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 **/
	////TODO : services/public/DEFAULT/products/category/MYCATEGORY?lang=fr
	@RequestMapping("/services/public/products/{store}/{language}/{category}")
	@ResponseBody
	public ProductList getProducts(@PathVariable final String store, @PathVariable final String language, @PathVariable final String category, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		//http://localhost:8080/sm-shop/services/public/products/DEFAULT/en/book

		try {


			/**
			 * How to Spring MVC Rest web service - ajax / jquery
			 * http://codetutr.com/2013/04/09/spring-mvc-easy-rest-based-json-services-with-responsebody/
			 */

			MerchantStoreItem merchantStore = (MerchantStoreItem) request.getAttribute(Constants.MERCHANT_STORE);
			Map<String, LanguageItem> langs = languageService.getLanguagesMap();

			if (merchantStore != null) {
				if (!merchantStore.getCode().equals(store)) {
					merchantStore = null; //reset for the current request
				}
			}

			if (merchantStore == null) {
				merchantStore = merchantStoreService.getByCode(store);
			}

			if (merchantStore == null) {
				LOGGER.error("Merchant store is null for code " + store);
				response.sendError(503, "Merchant store is null for code " + store);//TODO localized message
				return null;
			}

			//get the category by code
			CategoryItem cat = categoryService.getBySeUrl(merchantStore, category);

			if (cat == null) {
				LOGGER.error("CategoryItem with friendly url " + category + " is null");
				response.sendError(503, "CategoryItem is null");//TODO localized message
			}

			String lineage = new StringBuilder().append(cat.getLineage()).append(cat.getUuid()).append("/").toString();

			List<CategoryItem> categories = categoryService.getListByLineage(store, lineage);

			List<UUID> ids = new ArrayList<>();
			if (categories != null && categories.size() > 0) {
				for (CategoryItem c : categories) {
					ids.add(c.getUuid());
				}
			}
			ids.add(cat.getUuid());

			LanguageItem lang = langs.get(language);
			if (lang == null) {
				lang = langs.get(Constants.DEFAULT_LANGUAGE);
			}

			List<ProductItem> products = productService.getProducts(ids, lang);

			ProductList productList = new ProductList();

			ReadableProductPopulator populator = new ReadableProductPopulator();
			populator.setPricingService(pricingService);
			populator.setimageUtils(imageUtils);

			for (ProductItem product : products) {
				//create new proxy product
				ReadableProduct p = populator.populate(product, new ReadableProduct(), merchantStore, lang);
				productList.getProducts().add(p);

			}

			productList.setProductCount(productList.getProducts().size());
			return productList;


		} catch (Exception e) {
			LOGGER.error("Error while getting category", e);
			response.sendError(503, "Error while getting category");
		}

		return null;
	}


	/**
	 * Will page products of a given category
	 *
	 * @param store
	 * @param language
	 * @param category
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/services/public/products/page/{start}/{max}/{store}/{language}/{category}")
	@ResponseBody
	public ProductList getProducts(@PathVariable int start, @PathVariable int max, @PathVariable String store, @PathVariable final String language, @PathVariable final String category, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {


		return this.getProducts(start, max, store, language, category, null, model, request, response);
	}


	/**
	 * An entry point for filtering by another entity such as ManufacturerItem
	 * filter=BRAND&filter-value=123
	 *
	 * @param start
	 * @param max
	 * @param store
	 * @param language
	 * @param category
	 * @param filterType
	 * @param filterValue
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/services/public/products/page/{start}/{max}/{store}/{language}/{category}/filter={filterType}/filter-value={filterValue}")
	@ResponseBody
	public ProductList getProductsFilteredByType(@PathVariable int start, @PathVariable int max, @PathVariable String store, @PathVariable final String language, @PathVariable final String category, @PathVariable final String filterType, @PathVariable final String filterValue, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		List<QueryFilter> queryFilters = null;
		try {
			if (filterType.equals(QueryFilterType.BRAND.name())) {//the only one implemented so far
				QueryFilter filter = new QueryFilter();
				filter.setFilterType(QueryFilterType.BRAND);
				filter.setFilterId(UUID.fromString(filterValue));
				if (queryFilters == null) {
					queryFilters = new ArrayList<>();
				}
				queryFilters.add(filter);
			}
		} catch (Exception e) {
			LOGGER.error("Invalid filter or filter-value " + filterType + " - " + filterValue, e);
		}

		return this.getProducts(start, max, store, language, category, queryFilters, model, request, response);
	}


	private ProductList getProducts(final int start, final int max, final String store, final String language, final String category, final List<QueryFilter> filters, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {


		try {


			MerchantStoreItem merchantStore = (MerchantStoreItem) request.getAttribute(Constants.MERCHANT_STORE);
			List<BigDecimal> prices = new ArrayList<BigDecimal>();

			Map<String, LanguageItem> langs = languageService.getLanguagesMap();

			if (merchantStore != null) {
				if (!merchantStore.getCode().equals(store)) {
					merchantStore = null; //reset for the current request
				}
			}

			if (merchantStore == null) {
				merchantStore = merchantStoreService.getByCode(store);
			}

			if (merchantStore == null) {
				LOGGER.error("Merchant store is null for code " + store);
				response.sendError(503, "Merchant store is null for code " + store);//TODO localized message
				return null;
			}

			//get the category by code
			CategoryItem cat = categoryService.getBySeUrl(merchantStore, category);

			if (cat == null) {
				LOGGER.error("CategoryItem " + category + " is null");
				response.sendError(503, "CategoryItem is null");//TODO localized message
				return null;
			}

			String lineage = new StringBuilder().append(cat.getLineage()).append(cat.getUuid()).append("/").toString();

			List<CategoryItem> categories = categoryService.getListByLineage(store, lineage);

			List<UUID> ids = new ArrayList<>();
			if (categories != null && categories.size() > 0) {
				for (CategoryItem c : categories) {
					if (c.getVisible()) {
						ids.add(c.getUuid());
					}
				}
			}
			ids.add(cat.getUuid());


			LanguageItem lang = langs.get(language);
			if (lang == null) {
				lang = langs.get(Constants.DEFAULT_LANGUAGE);
			}

			ProductCriteria productCriteria = new ProductCriteria();
			productCriteria.setMaxCount(max);
			productCriteria.setStartIndex(start);
			productCriteria.setCategoryIds(ids);
			productCriteria.setAvailable(true);

			if (filters != null) {
				for (QueryFilter filter : filters) {
					if (filter.getFilterType().name().equals(QueryFilterType.BRAND.name())) {//the only filter implemented
						productCriteria.setManufacturerId(filter.getFilterId());
					}
				}
			}

			com.coretex.core.model.catalog.product.ProductList products = productService.listByStore(merchantStore, lang, productCriteria);

			ReadableProductPopulator populator = new ReadableProductPopulator();
			populator.setPricingService(pricingService);
			populator.setimageUtils(imageUtils);

			ProductList productList = new ProductList();
			for (ProductItem product : products.getProducts()) {

				//create new proxy product
				ReadableProduct p = populator.populate(product, new ReadableProduct(), merchantStore, lang);
				productList.getProducts().add(p);
				prices.add(p.getPrice());

			}


			/** order products based on the specified order **/
			Collections.sort(productList.getProducts(), new Comparator<ReadableProduct>() {

				@Override
				public int compare(ReadableProduct o1, ReadableProduct o2) {
					int order1 = o1.getSortOrder();
					int order2 = o2.getSortOrder();
					return order1 - order2;
				}
			});


			productList.setProductCount(products.getTotalCount());

			if (CollectionUtils.isNotEmpty(prices)) {
				BigDecimal minPrice = Collections.min(prices);
				BigDecimal maxPrice = Collections.max(prices);

				if (minPrice != null && maxPrice != null) {
					productList.setMinPrice(minPrice);
					productList.setMaxPrice(maxPrice);
				}
			}


			return productList;


		} catch (Exception e) {
			LOGGER.error("Error while getting products", e);
			response.sendError(503, "An error occured while retrieving products " + e.getMessage());
		}

		return null;

	}


}
