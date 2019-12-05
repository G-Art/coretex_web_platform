package com.coretex.shop.admin.controller.merchant;

import com.coretex.core.data.web.Menu;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.shop.constants.Constants;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
public class StoreLandingController {

	@PreAuthorize("hasRole('STORE')")
	@RequestMapping(value = "/admin/store/storeLanding.html", method = RequestMethod.GET)
	public String displayStoreLanding(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		setMenu(model, request);

		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);


		model.addAttribute("store", store);

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
