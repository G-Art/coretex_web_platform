package com.coretex.shop.store.controller.items;

import com.coretex.core.business.services.catalog.product.manufacturer.ManufacturerService;
import com.coretex.items.commerce_core_model.ManufacturerItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.core.LocaleItem;
import com.coretex.shop.constants.Constants;
import com.coretex.shop.model.catalog.manufacturer.ReadableManufacturer;
import com.coretex.shop.model.shop.PageInformation;
import com.coretex.shop.populator.manufacturer.ReadableManufacturerPopulator;
import com.coretex.shop.store.controller.ControllerConstants;
import com.coretex.shop.utils.PageBuilderUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * Drives various product listings
 *
 * @author carlsamson
 */
@Controller
public class ListItemsController {

	@Resource
	ManufacturerService manufacturerService;

	private static final Logger LOGGER = LoggerFactory.getLogger(ListItemsController.class);

	@RequestMapping("/shop/listing/{url}.html")
	public String displayListingPage(@PathVariable String url, Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {

		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.MERCHANT_STORE);

		LocaleItem language = (LocaleItem) request.getAttribute("LANGUAGE");

		//ManufacturerItem manufacturer = manufacturerService.getByUrl(store, language, url); // this needs to be checked

		ManufacturerItem manufacturer = null;

		if (manufacturer == null) {
			LOGGER.error("No manufacturer found for url " + url);
			//redirect on page not found
			return PageBuilderUtils.build404(store);

		}

		ReadableManufacturer readableManufacturer = new ReadableManufacturer();

		ReadableManufacturerPopulator populator = new ReadableManufacturerPopulator();
		readableManufacturer = populator.populate(manufacturer, readableManufacturer, store, language);

		//meta information
		PageInformation pageInformation = new PageInformation();
//		pageInformation.setPageDescription(readableManufacturer.getDescription().getMetaDescription());
//		pageInformation.setPageKeywords(readableManufacturer.getDescription().getKeyWords());
//		pageInformation.setPageTitle(readableManufacturer.getDescription().getTitle());
//		pageInformation.setPageUrl(readableManufacturer.getDescription().getFriendlyUrl());

		model.addAttribute("manufacturer", readableManufacturer);

		/** template **/
		StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Items.items_manufacturer).append(".").append(store.getStoreTemplate());

		return template.toString();
	}


}
