package com.coretex.shop.store.controller.content;

import java.util.Locale;
import java.util.Objects;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.coretex.items.commerce_core_model.MerchantStoreItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.coretex.core.business.services.content.ContentService;
import com.coretex.items.commerce_core_model.ContentItem;
import com.coretex.shop.constants.Constants;
import com.coretex.shop.model.shop.PageInformation;
import com.coretex.shop.store.controller.ControllerConstants;

@Controller
public class ShopContentController {


	@Resource
	private ContentService contentService;


	@RequestMapping("/shop/pages/{friendlyUrl}.html")
	public String displayContent(@PathVariable final String friendlyUrl, Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {

		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.MERCHANT_STORE);


		ContentItem content = contentService.getBySeUrl(store, friendlyUrl);

		if (Objects.nonNull(content)) {
			if (!content.getVisible()) {
				return "redirect:/shop";
			}

			//meta information
			PageInformation pageInformation = new PageInformation();
			pageInformation.setPageDescription(content.getMetatagDescription());
			pageInformation.setPageKeywords(content.getMetatagKeywords());
			pageInformation.setPageTitle(content.getTitle());
			pageInformation.setPageUrl(content.getName());

			request.setAttribute(Constants.REQUEST_PAGE_INFORMATION, pageInformation);


		}

		//TODO breadcrumbs
		request.setAttribute(Constants.LINK_CODE, content.getSeUrl());
		model.addAttribute("content", content);

		if (!StringUtils.isBlank(content.getProductGroup())) {
			model.addAttribute("productGroup", content.getProductGroup());
		}

		/** template **/
		StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Content.content).append(".").append(store.getStoreTemplate());

		return template.toString();


	}

}