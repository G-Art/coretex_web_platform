package com.coretex.shop.filter;

import com.coretex.core.business.services.catalog.category.CategoryService;
import com.coretex.core.business.services.catalog.product.ProductService;
import com.coretex.core.business.services.merchant.MerchantStoreService;
import com.coretex.core.business.services.reference.language.LanguageService;
import com.coretex.core.business.services.system.MerchantConfigurationService;
import com.coretex.core.business.utils.CacheUtils;
import com.coretex.core.business.utils.CoreConfiguration;
import com.coretex.core.model.system.MerchantConfig;
import com.coretex.items.commerce_core_model.CategoryItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.ProductItem;
import com.coretex.items.core.LocaleItem;
import com.coretex.items.cx_core.CustomerItem;
import com.coretex.shop.constants.Constants;
import com.coretex.shop.model.catalog.category.ReadableCategory;
import com.coretex.shop.model.customer.AnonymousCustomer;
import com.coretex.shop.model.customer.address.Address;
import com.coretex.shop.model.shop.Breadcrumb;
import com.coretex.shop.model.shop.BreadcrumbItem;
import com.coretex.shop.model.shop.BreadcrumbItemType;
import com.coretex.shop.model.shop.PageInformation;
import com.coretex.shop.store.controller.category.facade.CategoryFacade;
import com.coretex.shop.utils.LabelUtils;
import com.coretex.shop.utils.LanguageUtils;
import com.coretex.shop.utils.WebApplicationCacheUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static com.coretex.core.business.constants.Constants.DEFAULT_STORE;

/**
 * Servlet Filter implementation class StoreFilter
 */

public class StoreFilter extends HandlerInterceptorAdapter {

	private static final Logger LOGGER = LoggerFactory.getLogger(StoreFilter.class);

	private final static String STORE_REQUEST_PARAMETER = "store";

	@Resource
	private CategoryService categoryService;

	@Resource
	private ProductService productService;

	@Resource
	private MerchantStoreService merchantService;

	@Resource
	private MerchantConfigurationService merchantConfigurationService;

	@Resource
	private LanguageService languageService;

	@Resource
	private LabelUtils messages;

	@Resource
	private LanguageUtils languageUtils;

	@Resource
	private CacheUtils cache;

	@Resource
	private WebApplicationCacheUtils webApplicationCache;

	@Resource
	private CategoryFacade categoryFacade;

	@Resource
	private CoreConfiguration coreConfiguration;

	private final static String SERVICES_URL_PATTERN = "/services";
	private final static String REFERENCE_URL_PATTERN = "/reference";


	/**
	 * Default constructor.
	 */
	public StoreFilter() {

	}

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		request.setCharacterEncoding("UTF-8");

		/**
		 * if url contains /services exit from here !
		 */
		if (request.getRequestURL().toString().toLowerCase().contains(SERVICES_URL_PATTERN)
				|| request.getRequestURL().toString().toLowerCase().contains(REFERENCE_URL_PATTERN)) {
			return true;
		}

		/*****
		 * where is my stuff
		 */
		// String currentPath = System.getProperty("user.dir");
		// System.out.println("*** user.dir ***" + currentPath);
		// LOGGER.debug("*** user.dir ***" + currentPath);

		try {

			/** merchant store **/
			MerchantStoreItem store =
					(MerchantStoreItem) request.getSession().getAttribute(Constants.MERCHANT_STORE);

			String storeCode = request.getParameter(STORE_REQUEST_PARAMETER);

			// remove link set from controllers for declaring active - inactive links
			request.removeAttribute(Constants.LINK_CODE);

			if (!StringUtils.isBlank(storeCode)) {
				if (store != null) {
					if (!store.getCode().equals(storeCode)) {
						store = setMerchantStoreInSession(request, storeCode);
					}
				} else { // when url sm-shop/shop is being loaded for first time store is null
					store = setMerchantStoreInSession(request, storeCode);
				}
			}

			if (store == null) {
				store = setMerchantStoreInSession(request, DEFAULT_STORE);
			}

			request.setAttribute(Constants.MERCHANT_STORE, store);

			/** customer **/
			CustomerItem customer = (CustomerItem) request.getSession().getAttribute(Constants.CUSTOMER);
			if (customer != null) {
				if (!customer.getStore().getUuid().equals(store.getUuid())) {
					request.getSession().removeAttribute(Constants.CUSTOMER);
				}
				if (customer.getAnonymous() == null || !customer.getAnonymous()) {
					if (!request.isUserInRole("AUTH_CUSTOMER")) {
						request.removeAttribute(Constants.CUSTOMER);
					}
				}
				request.setAttribute(Constants.CUSTOMER, customer);
			}

			AnonymousCustomer anonymousCustomer =
					(AnonymousCustomer) request.getSession().getAttribute(Constants.ANONYMOUS_CUSTOMER);
			if (anonymousCustomer == null) {

				Address address = new Address();
				address.setCountry(store.getCountry().getIsoCode());
				if (store.getZone() != null) {
					address.setZone(store.getZone().getCode());
				} else {
					address.setStateProvince(store.getStoreStateProvince());
				}
				/** no postal code **/
				// address.setPostalCode(store.getStorepostalcode());

				anonymousCustomer = new AnonymousCustomer();
				anonymousCustomer.setBilling(address);
				request.getSession().setAttribute(Constants.ANONYMOUS_CUSTOMER, anonymousCustomer);
			} else {
				request.setAttribute(Constants.ANONYMOUS_CUSTOMER, anonymousCustomer);
			}


			/** language & locale **/
			LocaleItem language = languageUtils.getRequestLanguage(request, response);
			request.setAttribute(Constants.LANGUAGE, language);


			Locale locale = languageService.toLocale(language, store);
			request.setAttribute(Constants.LOCALE, locale);

			// Locale locale = LocaleContextHolder.getLocale();
			LocaleContextHolder.setLocale(locale);

			/** Breadcrumbs **/
			setBreadcrumb(request, locale);


			/**
			 * Get global objects Themes are built on a similar way displaying Header, Body and Footer
			 * Header and Footer are displayed on each page Some themes also contain side bars which may
			 * include similar emements
			 *
			 * Elements from Header : - CMS links - CustomerItem - Mini shopping cart - Store name / logo -
			 * Top categories - Search
			 *
			 * Elements from Footer : - CMS links - Store address - Global payment information - Global
			 * shipping information
			 */


			// get from the cache first
			/**
			 * The cache for each object contains 2 objects, a Cache and a Missed-Cache Get objects from
			 * the cache If not null use those objects If null, get entry from missed-cache If
			 * missed-cache not null then nothing exist If missed-cache null, add missed-cache entry and
			 * load from the database If objects from database not null store in cache
			 */

			/******* CMS Objects ********/
			this.getContentObjects(store, language, request);

			/******* CMS Page names **********/
			this.getContentPageNames(store, language, request);

			/******* Top Categories ********/
			// this.getTopCategories(store, language, request);
			this.setTopCategories(store, language, request);

			/******* Default metatags *******/

			/**
			 * Title Description Keywords
			 */

			PageInformation pageInformation = new PageInformation();
			pageInformation.setPageTitle(store.getStoreName());
			pageInformation.setPageDescription(store.getStoreName());
			pageInformation.setPageKeywords(store.getStoreName());

			request.setAttribute(Constants.REQUEST_PAGE_INFORMATION, pageInformation);


			/******* Configuration objects *******/

			/**
			 * SHOP configuration type Should contain - Different configuration flags - Google analytics -
			 * Facebook page - Twitter handle - Show customer login - ...
			 */

			this.getMerchantConfigurations(store, request);

			/******* Shopping Cart *********/

			String shoppingCarCode = (String) request.getSession().getAttribute(Constants.SHOPPING_CART);
			if (shoppingCarCode != null) {
				request.setAttribute(Constants.REQUEST_SHOPPING_CART, shoppingCarCode);
			}


		} catch (Exception e) {
			LOGGER.error("Error in StoreFilter", e);
		}

		return true;

	}

	@SuppressWarnings("unchecked")
	private void getMerchantConfigurations(MerchantStoreItem store, HttpServletRequest request)
			throws Exception {


		StringBuilder configKey = new StringBuilder();
		configKey.append(store.getUuid()).append("_").append(Constants.CONFIG_CACHE_KEY);


		StringBuilder configKeyMissed = new StringBuilder();
		configKeyMissed.append(configKey.toString()).append(Constants.MISSED_CACHE_KEY);

		Map<String, Object> configs = null;

		if (store.getUseCache()) {

			// get from the cache
			configs = (Map<String, Object>) cache.getFromCache(configKey.toString());
			if (configs == null) {
				// get from missed cache
				// Boolean missedContent = (Boolean)cache.getFromCache(configKeyMissed.toString());

				// if( missedContent==null) {
				configs = this.getConfigurations(store);
				// put in cache

				if (configs != null) {
					cache.putInCache(configs, configKey.toString());
				} else {
					// put in missed cache
					// cache.putInCache(new Boolean(true), configKeyMissed.toString());
				}
				// }
			}

		} else {
			configs = this.getConfigurations(store);
		}


		if (configs != null && configs.size() > 0) {
			request.setAttribute(Constants.REQUEST_CONFIGS, configs);
		}


	}


	@SuppressWarnings("unchecked")
	private void getContentPageNames(MerchantStoreItem store, LocaleItem language,
									 HttpServletRequest request) throws Exception {


		/**
		 * CMS links Those links are implemented as pages (ContentItem) ContentDescription will provide
		 * attributes name for the label to be displayed and seUrl for the friendly url page
		 */

		// build the key
		/**
		 * The cache is kept as a Map<String,Object> The key is <MERCHANT_ID>_CONTENTPAGELOCALE The
		 * value is a List of ContentItem object
		 */

		StringBuilder contentKey = new StringBuilder();
		contentKey.append(store.getUuid()).append("_").append(Constants.CONTENT_PAGE_CACHE_KEY)
				.append("-").append(language.getIso());

		StringBuilder contentKeyMissed = new StringBuilder();
		contentKeyMissed.append(contentKey.toString()).append(Constants.MISSED_CACHE_KEY);

	}

	@SuppressWarnings({"unchecked"})
	private void getContentObjects(MerchantStoreItem store, LocaleItem language, HttpServletRequest request)
			throws Exception {


		/**
		 * CMS links Those links are implemented as pages (ContentItem) ContentDescription will provide
		 * attributes name for the label to be displayed and seUrl for the friendly url page
		 */

		// build the key
		/**
		 * The cache is kept as a Map<String,Object> The key is CONTENT_<MERCHANT_ID>_<LOCALE> The value
		 * is a List of ContentItem object
		 */

		StringBuilder contentKey = new StringBuilder();
		contentKey.append(store.getUuid()).append("_").append(Constants.CONTENT_CACHE_KEY).append("-")
				.append(language.getIso());

		StringBuilder contentKeyMissed = new StringBuilder();
		contentKeyMissed.append(contentKey.toString()).append(Constants.MISSED_CACHE_KEY);

	}

	@SuppressWarnings("unchecked")
	private void setTopCategories(MerchantStoreItem store, LocaleItem language, HttpServletRequest request)
			throws Exception {

		StringBuilder categoriesKey = new StringBuilder();
		categoriesKey.append(store.getUuid()).append("_").append(Constants.CATEGORIES_CACHE_KEY)
				.append("-").append(language.getIso());

		StringBuilder categoriesKeyMissed = new StringBuilder();
		categoriesKeyMissed.append(categoriesKey.toString()).append(Constants.MISSED_CACHE_KEY);


		// language code - List of category
		Map<String, List<ReadableCategory>> objects = null;
		List<ReadableCategory> loadedCategories = null;

		if (store.getUseCache()) {
			objects = (Map<String, List<ReadableCategory>>) webApplicationCache
					.getFromCache(categoriesKey.toString());

			if (objects == null) {
				// load categories
				loadedCategories = categoryFacade.getCategoryHierarchy(store, 0, language, null);// null
				// filter
				objects = new ConcurrentHashMap<>();
				objects.put(language.getIso(), loadedCategories);
				webApplicationCache.putInCache(categoriesKey.toString(), objects);

			} else {
				loadedCategories = objects.get(language.getIso());
			}

		} else {
			loadedCategories = categoryFacade.getCategoryHierarchy(store, 0, language, null);// null
			// filter
		}

		if (loadedCategories != null) {
			request.setAttribute(Constants.REQUEST_TOP_CATEGORIES, loadedCategories);
		}

	}


	@SuppressWarnings("unused")
	private Map<String, Object> getConfigurations(MerchantStoreItem store) {

		Map<String, Object> configs = new HashMap<String, Object>();
		try {

			// get MerchantConfig
			MerchantConfig merchantConfig = merchantConfigurationService.getMerchantConfig(store);
			if (merchantConfig != null) {
				if (configs == null) {
					configs = new HashMap<String, Object>();
				}

				ObjectMapper m = new ObjectMapper();
				@SuppressWarnings("unchecked")
				Map<String, Object> props = m.convertValue(merchantConfig, Map.class);

				for (String key : props.keySet()) {
					configs.put(key, props.get(key));
				}
			}
		} catch (Exception e) {
			LOGGER.error("Exception while getting configurations", e);
		}

		return configs;

	}

	private void setBreadcrumb(HttpServletRequest request, Locale locale) {


		try {

			// breadcrumb
			Breadcrumb breadCrumb = (Breadcrumb) request.getSession().getAttribute(Constants.BREADCRUMB);
			LocaleItem language = (LocaleItem) request.getAttribute(Constants.LANGUAGE);
			if (breadCrumb == null) {
				breadCrumb = new Breadcrumb();
				breadCrumb.setLanguage(language);
				BreadcrumbItem item = this.getDefaultBreadcrumbItem(language, locale);
				breadCrumb.getBreadCrumbs().add(item);
			} else {

				// check language
				if (Objects.nonNull(breadCrumb.getLanguage()) && language.getIso().equals(breadCrumb.getLanguage().getIso())) {

					// rebuild using the appropriate language
					List<BreadcrumbItem> items = new ArrayList<BreadcrumbItem>();
					for (BreadcrumbItem item : breadCrumb.getBreadCrumbs()) {

						if (item.getItemType().name().equals(BreadcrumbItemType.HOME)) {
							BreadcrumbItem homeItem = this.getDefaultBreadcrumbItem(language, locale);
							homeItem.setItemType(BreadcrumbItemType.HOME);
							homeItem.setLabel(messages.getMessage(Constants.HOME_MENU_KEY, locale));
							homeItem.setUrl(Constants.HOME_URL);
							items.add(homeItem);
						} else if (item.getItemType().name().equals(BreadcrumbItemType.PRODUCT)) {
							ProductItem product = productService.getProductForLocale(item.getId(), language, locale);
							if (product != null) {
								BreadcrumbItem productItem = new BreadcrumbItem();
								productItem.setId(product.getUuid());
								productItem.setItemType(BreadcrumbItemType.PRODUCT);
								productItem.setLabel(product.getName());
								productItem.setUrl(product.getSeUrl());
								items.add(productItem);
							}
						} else if (item.getItemType().name().equals(BreadcrumbItemType.CATEGORY)) {
							CategoryItem category = categoryService.getOneByLanguage(item.getId(), language);
							if (category != null) {
								BreadcrumbItem categoryItem = new BreadcrumbItem();
								categoryItem.setId(category.getUuid());
								categoryItem.setItemType(BreadcrumbItemType.CATEGORY);
								categoryItem.setLabel(category.getName());
								categoryItem.setUrl(category.getSeUrl());
								items.add(categoryItem);
							}
						}

					}

					breadCrumb = new Breadcrumb();
					breadCrumb.setLanguage(language);
					breadCrumb.setBreadCrumbs(items);

				}

			}

			request.getSession().setAttribute(Constants.BREADCRUMB, breadCrumb);
			request.setAttribute(Constants.BREADCRUMB, breadCrumb);

		} catch (Exception e) {
			LOGGER.error("Error while building breadcrumbs", e);
		}

	}

	private BreadcrumbItem getDefaultBreadcrumbItem(LocaleItem language, Locale locale) {

		// set home page item
		BreadcrumbItem item = new BreadcrumbItem();
		item.setItemType(BreadcrumbItemType.HOME);
		item.setLabel(messages.getMessage(Constants.HOME_MENU_KEY, locale));
		item.setUrl(Constants.HOME_URL);
		return item;

	}

	/**
	 * Sets a MerchantStoreItem with the given storeCode in the session.
	 *
	 * @param request
	 * @param storeCode The storeCode of the Merchant.
	 * @return the MerchantStoreItem inserted in the session.
	 * @throws Exception
	 */
	private MerchantStoreItem setMerchantStoreInSession(HttpServletRequest request, String storeCode)
			throws Exception {
		if (storeCode == null || request == null)
			return null;
		MerchantStoreItem store = merchantService.getByCode(storeCode);
		if (store != null) {
			request.getSession().setAttribute(Constants.MERCHANT_STORE, store);
		}
		return store;
	}

}
