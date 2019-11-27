package com.coretex.shop.admin.controller.shipping;

import com.coretex.core.business.services.reference.country.CountryService;
import com.coretex.core.business.services.reference.zone.ZoneService;
import com.coretex.core.business.services.shipping.ShippingOriginService;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.core.CountryItem;
import com.coretex.items.core.LocaleItem;
import com.coretex.items.commerce_core_model.ZoneItem;
import com.coretex.items.commerce_core_model.ShippingOriginItem;
import com.coretex.core.data.web.Menu;
import com.coretex.shop.constants.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


@Controller
public class ShippingOriginController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ShippingOriginController.class);


	@Resource
	private ShippingOriginService shippingOriginService;

	@Resource
	private CountryService countryService;

	@Resource
	private ZoneService zoneService;

	/**
	 * Configures the shipping mode, shows shipping countries
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasRole('SHIPPING')")
	@RequestMapping(value = "/admin/shipping/origin/get.html", method = RequestMethod.GET)
	public String displayShippingOrigin(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		this.setMenu(model, request);

		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);
		LocaleItem language = (LocaleItem) request.getAttribute("LANGUAGE");

		ShippingOriginItem shippingOrigin = shippingOriginService.getByStore(store);

		List<CountryItem> countries = countryService.getCountries(language);

		if (shippingOrigin == null) {
			shippingOrigin = new ShippingOriginItem();
			shippingOrigin.setCountry(store.getCountry());
			shippingOrigin.setState(store.getStoreStateProvince());
			shippingOrigin.setZone(store.getZone());
		}

		model.addAttribute("countries", countries);
		model.addAttribute("origin", shippingOrigin);
		return "shipping-origin";


	}

	@PreAuthorize("hasRole('SHIPPING')")
	@RequestMapping(value = "/admin/shipping/origin/post.html", method = RequestMethod.POST)
	public String saveShippingOrigin(@Valid @ModelAttribute("origin") ShippingOriginItem origin, Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {

		this.setMenu(model, request);
		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);

		LocaleItem language = (LocaleItem) request.getAttribute("LANGUAGE");
		List<CountryItem> countries = countryService.getCountries(language);

		ShippingOriginItem shippingOrigin = shippingOriginService.getByStore(store);
		if (shippingOrigin != null) {
			origin.setUuid(shippingOrigin.getUuid());
		}

		origin.setMerchantStore(store);

		CountryItem country = countryService.getByCode(origin.getCountry().getIsoCode());
		origin.setCountry(country);

		if (origin.getZone() != null) {
			ZoneItem zone = zoneService.getByCode(origin.getZone().getCode());
			origin.setZone(zone);
		}

		if (shippingOrigin != null) {
			shippingOriginService.update(origin);
		} else {
			shippingOriginService.save(origin);
		}

		model.addAttribute("countries", countries);
		model.addAttribute("origin", origin);
		model.addAttribute("success", "success");
		return "shipping-origin";

	}

	@PreAuthorize("hasRole('SHIPPING')")
	@RequestMapping(value = "/admin/shipping/origin/delete.html", method = RequestMethod.POST)
	public String deleteShippingOrigin(@ModelAttribute("origin") ShippingOriginItem origin, Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {

		this.setMenu(model, request);
		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);

		ShippingOriginItem shippingOrigin = shippingOriginService.getByStore(store);

		LocaleItem language = (LocaleItem) request.getAttribute("LANGUAGE");
		List<CountryItem> countries = countryService.getCountries(language);


		if (shippingOrigin != null && origin != null) {
			if (shippingOrigin.getUuid().equals(origin.getUuid())) {
				shippingOriginService.delete(shippingOrigin);
				model.addAttribute("success", "success");
			} else {
				return "redirect:/admin/shipping/origin/get.html";
			}
		} else {
			return "redirect:/admin/shipping/origin/get.html";
		}

		model.addAttribute("countries", countries);
		model.addAttribute("origin", null);
		model.addAttribute("success", "success");
		return "shipping-origin";

	}

	private void setMenu(Model model, HttpServletRequest request) throws Exception {

		//display menu
		Map<String, String> activeMenus = new HashMap<String, String>();
		activeMenus.put("shipping", "shipping");
		activeMenus.put("shipping-origin", "shipping-origin");

		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>) request.getAttribute("MENUMAP");

		Menu currentMenu = menus.get("shipping");
		model.addAttribute("currentMenu", currentMenu);
		model.addAttribute("activeMenus", activeMenus);
		//

	}


}
