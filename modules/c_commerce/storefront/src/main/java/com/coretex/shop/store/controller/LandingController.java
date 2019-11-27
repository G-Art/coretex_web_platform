package com.coretex.shop.store.controller;

import com.coretex.core.business.services.catalog.product.PricingService;
import com.coretex.core.business.services.catalog.product.relationship.ProductRelationshipService;
import com.coretex.core.business.services.content.ContentService;
import com.coretex.core.business.services.merchant.MerchantStoreService;
import com.coretex.core.model.catalog.product.relationship.ProductRelationshipType;
import com.coretex.items.core.LocaleItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.ProductItem;
import com.coretex.items.commerce_core_model.ProductRelationshipItem;
import com.coretex.shop.constants.Constants;
import com.coretex.shop.model.catalog.product.ReadableProduct;
import com.coretex.shop.model.shop.Breadcrumb;
import com.coretex.shop.model.shop.BreadcrumbItem;
import com.coretex.shop.model.shop.BreadcrumbItemType;
import com.coretex.shop.model.shop.PageInformation;
import com.coretex.shop.populator.catalog.ReadableProductPopulator;
import com.coretex.shop.utils.DateUtil;
import com.coretex.shop.utils.ImageFilePath;
import com.coretex.shop.utils.LabelUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


@Controller
public class LandingController {


	private final static String LANDING_PAGE = "LANDING_PAGE";


	@Resource
	private ContentService contentService;

	@Resource
	private ProductRelationshipService productRelationshipService;


	@Resource
	private LabelUtils messages;

	@Resource
	private PricingService pricingService;

	@Resource
	private MerchantStoreService merchantService;

	@Resource
	@Qualifier("img")
	private ImageFilePath imageUtils;

	private static final Logger LOGGER = LoggerFactory.getLogger(LandingController.class);
	private final static String HOME_LINK_CODE = "HOME";

	@RequestMapping(value = {Constants.SHOP_URI + "/home.html", Constants.SHOP_URI + "/", Constants.SHOP_URI}, method = RequestMethod.GET)
	public String displayLanding(Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {

		LocaleItem language = (LocaleItem) request.getAttribute(Constants.LANGUAGE);

		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.MERCHANT_STORE);

		request.setAttribute(Constants.LINK_CODE, HOME_LINK_CODE);


		/** Rebuild breadcrumb **/
		BreadcrumbItem item = new BreadcrumbItem();
		item.setItemType(BreadcrumbItemType.HOME);
		item.setLabel(messages.getMessage(Constants.HOME_MENU_KEY, locale));
		item.setUrl(Constants.HOME_URL);


		Breadcrumb breadCrumb = new Breadcrumb();
		breadCrumb.setLanguage(language);

		List<BreadcrumbItem> items = new ArrayList<BreadcrumbItem>();
		items.add(item);

		breadCrumb.setBreadCrumbs(items);
		request.getSession().setAttribute(Constants.BREADCRUMB, breadCrumb);
		request.setAttribute(Constants.BREADCRUMB, breadCrumb);
		/** **/
		ReadableProductPopulator populator = new ReadableProductPopulator();
		populator.setPricingService(pricingService);
		populator.setimageUtils(imageUtils);


		//featured items
		List<ProductRelationshipItem> relationships = productRelationshipService.getByType(store, ProductRelationshipType.FEATURED_ITEM, language);
		List<ReadableProduct> featuredItems = new ArrayList<ReadableProduct>();
		Date today = new Date();
		for (ProductRelationshipItem relationship : relationships) {
			ProductItem product = relationship.getRelatedProduct();
			if (product.getAvailable() && DateUtil.dateBeforeEqualsDate(product.getDateAvailable(), today)) {
				ReadableProduct proxyProduct = populator.populate(product, new ReadableProduct(), store, language);
				featuredItems.add(proxyProduct);
			}
		}

		String tmpl = store.getStoreTemplate();
		if (StringUtils.isBlank(tmpl)) {
			tmpl = "exoticamobilia";
		}


		model.addAttribute("featuredItems", featuredItems);

		/** template **/
		StringBuilder template = new StringBuilder().append("landing.").append(tmpl);
		return template.toString();
	}

	@RequestMapping(value = {Constants.SHOP_URI + "/stub.html"}, method = RequestMethod.GET)
	public String displayHomeStub(Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {
		return "index";
	}

	@RequestMapping(value = Constants.SHOP_URI + "/{store}", method = RequestMethod.GET)
	public String displayStoreLanding(@PathVariable final String store, HttpServletRequest request, HttpServletResponse response) {

		try {

			request.getSession().invalidate();
			request.getSession().removeAttribute(Constants.MERCHANT_STORE);

			MerchantStoreItem merchantStore = merchantService.getByCode(store);
			if (merchantStore != null) {
				request.getSession().setAttribute(Constants.MERCHANT_STORE, merchantStore);
			} else {
				LOGGER.error("MerchantStoreItem does not exist for store code " + store);
			}

		} catch (Exception e) {
			LOGGER.error("Error occured while getting store code " + store, e);
		}


		return "redirect:" + Constants.SHOP_URI;
	}


}
