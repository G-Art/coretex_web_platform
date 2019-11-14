package com.coretex.shop.admin.controller.configurations;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.coretex.items.commerce_core_model.MerchantStoreItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.coretex.core.business.services.system.MerchantConfigurationService;
import com.coretex.core.model.system.MerchantConfig;
import com.coretex.core.data.web.Menu;
import com.coretex.shop.constants.Constants;


@Controller
public class SystemConfigurationController {

	private static final Logger LOGGER = LoggerFactory.getLogger(SystemConfigurationController.class);

	@Resource
	private MerchantConfigurationService merchantConfigurationService;

	@Resource
	Environment env;


	@PreAuthorize("hasRole('AUTH')")
	@RequestMapping(value = "/admin/configuration/system.html", method = RequestMethod.GET)
	public String displaySysyemConfgurations(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		setMenu(model, request);
		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);
		MerchantConfig merchantConfiguration = merchantConfigurationService.getMerchantConfig(store);

		if (merchantConfiguration == null) {
			merchantConfiguration = new MerchantConfig();
		}

		model.addAttribute("store", store);
		model.addAttribute("configuration", merchantConfiguration);
		return com.coretex.shop.admin.controller.ControllerConstants.Tiles.Configuration.system;
	}

	@PreAuthorize("hasRole('AUTH')")
	@RequestMapping(value = "/admin/configuration/saveSystemConfiguration.html", method = RequestMethod.POST)
	public String saveSystemConfigurations(@ModelAttribute("configuration") MerchantConfig merchantConfiguration, BindingResult result, Model model, HttpServletRequest request, Locale locale) throws Exception {
		setMenu(model, request);

		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);
		merchantConfigurationService.saveMerchantConfig(merchantConfiguration, store);
		model.addAttribute("success", "success");
		model.addAttribute("store", store);
		model.addAttribute("configuration", merchantConfiguration);
		return com.coretex.shop.admin.controller.ControllerConstants.Tiles.Configuration.system;

	}


	private void setMenu(Model model, HttpServletRequest request) throws Exception {

		Map<String, String> activeMenus = new HashMap<String, String>();
		activeMenus.put("configuration", "configuration");
		activeMenus.put("system-configurations", "system-configurations");

		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>) request.getAttribute("MENUMAP");

		Menu currentMenu = menus.get("configuration");
		model.addAttribute("currentMenu", currentMenu);
		model.addAttribute("activeMenus", activeMenus);
	}
}
