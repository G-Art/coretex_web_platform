package com.coretex.shop.admin.controller.merchant;


import com.coretex.core.business.services.content.ContentService;
import com.coretex.core.business.services.merchant.MerchantStoreService;
import com.coretex.core.business.services.reference.language.LanguageService;
import com.coretex.items.commerce_core_model.ContentItem;
import com.coretex.enums.commerce_core_model.ContentTypeEnum;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.core.data.merchant.StoreLanding;
import com.coretex.core.data.merchant.StoreLandingDescription;
import com.coretex.core.data.web.Menu;
import com.coretex.shop.constants.Constants;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class StoreLandingController {

	@Resource
	MerchantStoreService merchantStoreService;

	@Resource
	LanguageService languageService;

	@Resource
	ContentService contentService;

	@PreAuthorize("hasRole('STORE')")
	@RequestMapping(value = "/admin/store/storeLanding.html", method = RequestMethod.GET)
	public String displayStoreLanding(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		setMenu(model, request);

		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);

		List<LanguageItem> languages = store.getLanguages();

		ContentItem content = contentService.getByCode("LANDING_PAGE", store);
		StoreLanding landing = new StoreLanding();


		model.addAttribute("store", store);
		model.addAttribute("storeLanding", landing);


		return "admin-store-landing";
	}

	@PreAuthorize("hasRole('STORE')")
	@RequestMapping(value = "/admin/store/saveLanding.html", method = RequestMethod.POST)
	public String saveStoreLanding(@Valid @ModelAttribute("storeLanding") StoreLanding storeLanding, BindingResult result, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		setMenu(model, request);

		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);


		if (result.hasErrors()) {
			return "admin-store-landing";
		}

		//get original store
		ContentItem content = contentService.getByCode("LANDING_PAGE", store);

		if (content == null) {
			content = new ContentItem();
			content.setVisible(true);
			content.setContentType(ContentTypeEnum.SECTION);
			content.setCode("LANDING_PAGE");
			content.setMerchantStore(store);
		}


		//List<LanguageItem> languages = store.getLanguages();

		Map<String, LanguageItem> langs = languageService.getLanguagesMap();
		
		
		
/*		for(LanguageItem l : languages) {
			
			StoreLandingDescription landingDescription = null;
			for(ContentDescription desc : content.getDescriptions()) {
					if(desc.getLanguage().getCode().equals(l.getCode())) {
						landingDescription = new StoreLandingDescription();
						landingDescription.setDescription(desc.getMetatagDescription());
						landingDescription.setHomePageContent(desc.getDescription());
						landingDescription.setKeywords(desc.getMetatagKeywords());
						landingDescription.setTitle(desc.getName());//name is a not empty
						landingDescription.setLanguage(desc.getLanguage());
					}
			}
		
			
			if(landingDescription==null) {
				landingDescription = new StoreLandingDescription();
				landingDescription.setLanguage(l);
			}
			

			
			descriptions.add(landingDescription);
		}
		
		landing.setDescriptions(descriptions);*/


		List<StoreLandingDescription> descriptions = storeLanding.getDescriptions();


		contentService.saveOrUpdate(content);

		model.addAttribute("success", "success");

		return "admin-store-landing";
	}

	private void setMenu(Model model, HttpServletRequest request) throws Exception {

		//display menu
		Map<String, String> activeMenus = new HashMap<String, String>();
		activeMenus.put("store", "store");
		activeMenus.put("storeLanding", "storeLanding");


		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>) request.getAttribute("MENUMAP");

		Menu currentMenu = menus.get("store");
		model.addAttribute("currentMenu", currentMenu);
		model.addAttribute("activeMenus", activeMenus);
		//

	}

}
