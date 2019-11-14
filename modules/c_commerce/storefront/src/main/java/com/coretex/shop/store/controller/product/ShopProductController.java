package com.coretex.shop.store.controller.product;

import com.coretex.core.business.services.catalog.product.PricingService;
import com.coretex.core.business.services.catalog.product.ProductService;
import com.coretex.core.business.services.catalog.product.attribute.ProductAttributeService;
import com.coretex.core.business.services.catalog.product.relationship.ProductRelationshipService;
import com.coretex.core.business.services.catalog.product.review.ProductReviewService;
import com.coretex.core.business.utils.CacheUtils;
import com.coretex.core.model.catalog.product.price.FinalPrice;
import com.coretex.core.model.catalog.product.relationship.ProductRelationshipType;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.ProductAttributeItem;
import com.coretex.items.commerce_core_model.ProductItem;
import com.coretex.items.commerce_core_model.ProductOptionValueItem;
import com.coretex.items.commerce_core_model.ProductRelationshipItem;
import com.coretex.items.commerce_core_model.ProductReviewItem;
import com.coretex.shop.constants.Constants;
import com.coretex.shop.model.catalog.product.ReadableProduct;
import com.coretex.shop.model.catalog.product.ReadableProductPrice;
import com.coretex.shop.model.catalog.product.ReadableProductReview;
import com.coretex.shop.model.shop.Breadcrumb;
import com.coretex.shop.model.shop.PageInformation;
import com.coretex.shop.populator.catalog.ReadableFinalPricePopulator;
import com.coretex.shop.populator.catalog.ReadableProductPopulator;
import com.coretex.shop.populator.catalog.ReadableProductReviewPopulator;
import com.coretex.shop.store.controller.ControllerConstants;
import com.coretex.shop.store.model.catalog.Attribute;
import com.coretex.shop.store.model.catalog.AttributeValue;
import com.coretex.shop.utils.BreadcrumbsUtils;
import com.coretex.shop.utils.ImageFilePath;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;
import java.util.stream.Collectors;


/**
 * Populates the product details page
 *
 * @author Carl Samson
 */
@Controller
@RequestMapping("/shop/product")
public class ShopProductController {

	@Resource
	private ProductService productService;

	@Resource
	private ProductAttributeService productAttributeService;

	@Resource
	private ProductRelationshipService productRelationshipService;

	@Resource
	private PricingService pricingService;

	@Resource
	private ProductReviewService productReviewService;

	@Resource
	private CacheUtils cache;

	@Resource
	private BreadcrumbsUtils breadcrumbsUtils;

	@Resource
	@Qualifier("img")
	private ImageFilePath imageUtils;

	private static final Logger LOG = LoggerFactory.getLogger(ShopProductController.class);


	/**
	 * Display product details with reference to caller page
	 *
	 * @param friendlyUrl
	 * @param ref
	 * @param model
	 * @param request
	 * @param response
	 * @param locale
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/{friendlyUrl}.html/ref={ref}")
	public String displayProductWithReference(@PathVariable final String friendlyUrl, @PathVariable final String ref, Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		return display(ref, friendlyUrl, model, request, locale);
	}


	/**
	 * Display product details no reference
	 *
	 * @param friendlyUrl
	 * @param model
	 * @param request
	 * @param response
	 * @param locale
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/{friendlyUrl}.html")
	public String displayProduct(@PathVariable final String friendlyUrl, Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		return display(null, friendlyUrl, model, request, locale);
	}


	@SuppressWarnings("unchecked")
	public String display(final String reference, final String friendlyUrl, Model model, HttpServletRequest request, Locale locale) throws Exception {


		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.MERCHANT_STORE);
		LanguageItem language = (LanguageItem) request.getAttribute("LANGUAGE");

		ProductItem product = productService.getBySeUrl(store, friendlyUrl, locale);

		if (product == null) {
			return PageBuilderUtils.build404(store);
		}

		ReadableProductPopulator populator = new ReadableProductPopulator();
		populator.setPricingService(pricingService);
		populator.setimageUtils(imageUtils);

		ReadableProduct productProxy = populator.populate(product, new ReadableProduct(), store, language);

		//meta information
		PageInformation pageInformation = new PageInformation();
		pageInformation.setPageDescription(productProxy.getDescription().getMetaDescription());
		pageInformation.setPageKeywords(productProxy.getDescription().getKeyWords());
		pageInformation.setPageTitle(productProxy.getDescription().getTitle());
		pageInformation.setPageUrl(productProxy.getDescription().getFriendlyUrl());

		request.setAttribute(Constants.REQUEST_PAGE_INFORMATION, pageInformation);

		Breadcrumb breadCrumb = breadcrumbsUtils.buildProductBreadcrumb(reference, productProxy, store, language, request.getContextPath());
		request.getSession().setAttribute(Constants.BREADCRUMB, breadCrumb);
		request.setAttribute(Constants.BREADCRUMB, breadCrumb);


		StringBuilder relatedItemsCacheKey = new StringBuilder();
		relatedItemsCacheKey
				.append(store.getUuid())
				.append("_")
				.append(Constants.RELATEDITEMS_CACHE_KEY)
				.append("-")
				.append(language.getCode());

		StringBuilder relatedItemsMissed = new StringBuilder();
		relatedItemsMissed
				.append(relatedItemsCacheKey.toString())
				.append(Constants.MISSED_CACHE_KEY);

		Map<UUID, List<ReadableProduct>> relatedItemsMap = null;
		List<ReadableProduct> relatedItems = null;

		if (store.getUseCache()) {

			//get from the cache
			relatedItemsMap = (Map<UUID, List<ReadableProduct>>) cache.getFromCache(relatedItemsCacheKey.toString());
			if (relatedItemsMap == null) {
				//get from missed cache
				//Boolean missedContent = (Boolean)cache.getFromCache(relatedItemsMissed.toString());

				//if(missedContent==null) {
				relatedItems = relatedItems(store, product, language);
				if (relatedItems != null) {
					relatedItemsMap = new HashMap<>();
					relatedItemsMap.put(product.getUuid(), relatedItems);
					cache.putInCache(relatedItemsMap, relatedItemsCacheKey.toString());
				} else {
					//cache.putInCache(new Boolean(true), relatedItemsMissed.toString());
				}
				//}
			} else {
				relatedItems = relatedItemsMap.get(product.getUuid());
			}
		} else {
			relatedItems = relatedItems(store, product, language);
		}

		model.addAttribute("relatedProducts", relatedItems);
		Set<ProductAttributeItem> attributes = product.getAttributes();


		//split read only and options
		Map<UUID, Attribute> readOnlyAttributes = null;
		Map<UUID, Attribute> selectableOptions = null;

		if (!CollectionUtils.isEmpty(attributes)) {

			for (ProductAttributeItem attribute : attributes) {
				Attribute attr = null;
				AttributeValue attrValue = new AttributeValue();
				ProductOptionValueItem optionValue = attribute.getProductOptionValue();

				if (attribute.getAttributeDisplayOnly() == true) {//read only attribute
					if (readOnlyAttributes == null) {
						readOnlyAttributes = new TreeMap<>();
					}
					attr = readOnlyAttributes.get(attribute.getProductOption().getUuid());
					if (attr == null) {
						attr = createAttribute(attribute, language);
					}
					if (attr != null) {
						readOnlyAttributes.put(attribute.getProductOption().getUuid(), attr);
						attr.setReadOnlyValue(attrValue);
					}
				} else {//selectable option
					if (selectableOptions == null) {
						selectableOptions = new TreeMap<>();
					}
					attr = selectableOptions.get(attribute.getProductOption().getUuid());
					if (attr == null) {
						attr = createAttribute(attribute, language);
					}
					if (attr != null) {
						selectableOptions.put(attribute.getProductOption().getUuid(), attr);
					}
				}


				attrValue.setDefaultAttribute(attribute.getAttributeDefault());
				attrValue.setUuid(attribute.getUuid());//id of the attribute
				attrValue.setLanguage(language.getCode());
				if (attribute.getProductAttributePrice() != null && attribute.getProductAttributePrice().doubleValue() > 0) {
					String formatedPrice = pricingService.getDisplayAmount(attribute.getProductAttributePrice(), store);
					attrValue.setPrice(formatedPrice);
				}

				if (!StringUtils.isBlank(attribute.getProductOptionValue().getProductOptionValueImage())) {
					attrValue.setImage(imageUtils.buildProductPropertyImageUtils(store, attribute.getProductOptionValue().getProductOptionValueImage()));
				}
				attrValue.setSortOrder(0);
				if (attribute.getProductOptionSortOrder() != null) {
					attrValue.setSortOrder(attribute.getProductOptionSortOrder().intValue());
				}

				attrValue.setName(optionValue.getName());
				attrValue.setDescription(optionValue.getDescription());
				List<AttributeValue> attrs = attr.getValues();
				if (attrs == null) {
					attrs = new ArrayList<>();
					attr.setValues(attrs);
				}
				attrs.add(attrValue);
			}

		}


		List<ProductReviewItem> reviews = productReviewService.getByProduct(product, language);
		if (!CollectionUtils.isEmpty(reviews)) {
			List<ReadableProductReview> revs = new ArrayList<ReadableProductReview>();
			ReadableProductReviewPopulator reviewPopulator = new ReadableProductReviewPopulator();
			for (ProductReviewItem review : reviews) {
				ReadableProductReview rev = new ReadableProductReview();
				reviewPopulator.populate(review, rev, store, language);
				revs.add(rev);
			}
			model.addAttribute("reviews", revs);
		}

		List<Attribute> attributesList = null;
		if (readOnlyAttributes != null) {
			attributesList = new ArrayList<>(readOnlyAttributes.values());
		}

		List<Attribute> optionsList = null;
		if (selectableOptions != null) {
			optionsList = new ArrayList<>(selectableOptions.values());
			//order attributes by sort order
			for (Attribute attr : optionsList) {
				attr.getValues().sort((o1, o2) -> {
					if (o1.getSortOrder() == o2.getSortOrder())
						return 0;
					return o1.getSortOrder() < o2.getSortOrder() ? -1 : 1;

				});
			}
		}

		model.addAttribute("attributes", attributesList);
		model.addAttribute("options", optionsList);

		model.addAttribute("product", productProxy);


		/** template **/
		StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Product.product).append(".").append(store.getStoreTemplate());

		return template.toString();
	}

	@RequestMapping(value = {"/{productId}/calculatePrice.json"}, method = RequestMethod.POST)
	public @ResponseBody
	ReadableProductPrice calculatePrice(@RequestParam(value = "attributeIds[]") String[] attributeIds, @PathVariable final String productId, final HttpServletRequest request, final HttpServletResponse response, final Locale locale) throws Exception {


		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.MERCHANT_STORE);
		LanguageItem language = (LanguageItem) request.getAttribute("LANGUAGE");


		ProductItem product = productService.getById(UUID.fromString(productId));

		@SuppressWarnings("unchecked")
		List<UUID> ids = Arrays.stream(attributeIds).map(UUID::fromString).collect(Collectors.toList());
		List<ProductAttributeItem> attributes = productAttributeService.getByAttributeIds(store, product, ids);

		for (ProductAttributeItem attribute : attributes) {
			if (!attribute.getProduct().getUuid().equals(UUID.fromString(productId))) {
				return null;
			}
		}

		FinalPrice price = pricingService.calculateProductPrice(product, attributes);
		ReadableProductPrice readablePrice = new ReadableProductPrice();
		ReadableFinalPricePopulator populator = new ReadableFinalPricePopulator();
		populator.setPricingService(pricingService);
		populator.populate(price, readablePrice, store, language);
		return readablePrice;

	}

	private Attribute createAttribute(ProductAttributeItem productAttribute, LanguageItem language) {

		Attribute attribute = new Attribute();
		attribute.setUuid(productAttribute.getProductOption().getUuid());//attribute of the option
		attribute.setType(productAttribute.getProductOption().getProductOptionType());

		if (productAttribute.getProductOption() == null) {
			return null;
		}

		attribute.setType(productAttribute.getProductOption().getProductOptionType());
		attribute.setLanguage(language.getCode());
		attribute.setName(productAttribute.getProductOption().getName());
		attribute.setCode(productAttribute.getProductOption().getCode());


		return attribute;

	}

	private List<ReadableProduct> relatedItems(MerchantStoreItem store, ProductItem product, LanguageItem language) throws Exception {


		ReadableProductPopulator populator = new ReadableProductPopulator();
		populator.setPricingService(pricingService);
		populator.setimageUtils(imageUtils);

		List<ProductRelationshipItem> relatedItems = productRelationshipService.getByType(store, product, ProductRelationshipType.RELATED_ITEM);
		if (relatedItems != null && relatedItems.size() > 0) {
			List<ReadableProduct> items = new ArrayList<ReadableProduct>();
			for (ProductRelationshipItem relationship : relatedItems) {
				ProductItem relatedProduct = relationship.getRelatedProduct();
				ReadableProduct proxyProduct = populator.populate(relatedProduct, new ReadableProduct(), store, language);
				items.add(proxyProduct);
			}
			return items;
		}

		return null;
	}


}
