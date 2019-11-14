package com.coretex.shop.admin.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.core.CountryItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.coretex.core.business.services.reference.country.CountryService;
import com.coretex.core.business.services.user.UserService;
import com.coretex.items.commerce_core_model.UserItem;
import com.coretex.shop.constants.Constants;


@Controller
public class AdminController {

	@Resource
	CountryService countryService;

	@Resource
	UserService userService;

	@PreAuthorize("hasRole('AUTH')")
	@RequestMapping(value = {"/admin/home.html", "/admin/", "/admin"}, method = RequestMethod.GET)
	public String displayDashboard(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		LanguageItem language = (LanguageItem) request.getAttribute("LANGUAGE");

		//display menu
		Map<String, String> activeMenus = new HashMap<String, String>();
		activeMenus.put("home", "home");

		model.addAttribute("activeMenus", activeMenus);


		//get store information
		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);

		Map<String, CountryItem> countries = countryService.getCountriesMap(language);

		CountryItem storeCountry = store.getCountry();
		CountryItem country = countries.get(storeCountry.getIsoCode());

		String sCurrentUser = request.getRemoteUser();
		UserItem currentUser = userService.getByUserName(sCurrentUser);

		model.addAttribute("store", store);
		model.addAttribute("country", country);
		model.addAttribute("user", currentUser);
		//get last 10 orders
		//OrderCriteria orderCriteria = new OrderCriteria();
		//orderCriteria.setMaxCount(10);
		//orderCriteria.setOrderBy(CriteriaOrderBy.DESC);

		return ControllerConstants.Tiles.adminDashboard;
	}


}
