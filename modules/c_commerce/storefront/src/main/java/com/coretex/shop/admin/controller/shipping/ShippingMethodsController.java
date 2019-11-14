package com.coretex.shop.admin.controller.shipping;

import com.coretex.core.business.services.shipping.DeliveryService;
import com.coretex.core.business.services.shipping.ShippingService;
import com.coretex.core.data.shipping.DeliveryServiceData;
import com.coretex.core.data.web.Menu;
import com.coretex.core.model.system.IntegrationConfiguration;
import com.coretex.core.populators.DeliveryServiceDataPopulator;
import com.coretex.items.commerce_core_model.DeliveryServiceItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.shop.admin.controller.AbstractController;
import com.coretex.shop.admin.controller.ControllerConstants;
import com.coretex.shop.constants.Constants;
import com.coretex.shop.utils.LabelUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class ShippingMethodsController extends AbstractController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ShippingMethodsController.class);

	@Resource
	private DeliveryService deliveryService;

	@Resource
	private ShippingService shippingService;

	@Resource
	private LabelUtils messages;

	@Resource(name = "specificDataPopuators")
	private Map<String, DeliveryServiceDataPopulator<DeliveryServiceItem, DeliveryServiceData>> specificDataPopuators;

	/**
	 * Configures the shipping shows shipping methods
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasRole('SHIPPING')")
	@RequestMapping(value = "/admin/shipping/shippingMethods.html", method = RequestMethod.GET)
	public String displayShippingMethods(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		this.setMenu(model, request);
		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);

		var deliveryServices = deliveryService.getAvailableDeliveryServicesForStore(store);

		List<? extends DeliveryServiceData> collect = deliveryServices.stream()
				.map(deliveryService -> specificDataPopuators.get(deliveryService.getItemContext().getTypeCode()).populate(deliveryService, store, null))
				.collect(Collectors.toList());
		model.addAttribute("deliveryServices", collect);

		return "shipping-methods";

	}

	@PreAuthorize("hasRole('SHIPPING')")
	@RequestMapping(value = "/admin/shipping/shippingMethod.html", method = RequestMethod.GET)
	public String displayShippingMethod(@RequestParam("code") String code, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		this.setMenu(model, request);
		DeliveryServiceItem deliveryServiceItem = deliveryService.getByCode(code);

		List<String> environments = new ArrayList<>();
		environments.add(com.coretex.core.business.constants.Constants.TEST_ENVIRONMENT);
		environments.add(com.coretex.core.business.constants.Constants.PRODUCTION_ENVIRONMENT);

		model.addAttribute("deliveryService", deliveryServiceItem);
		model.addAttribute("environments", environments);
		return ControllerConstants.Tiles.Shipping.shippingMethod;

	}

	@PreAuthorize("hasRole('SHIPPING')")
	@RequestMapping(value = "/admin/shipping/saveShippingMethod.html", method = RequestMethod.POST)
	public String saveShippingMethod(@ModelAttribute("configuration") IntegrationConfiguration configuration, BindingResult result, Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {


		this.setMenu(model, request);
		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);

		String moduleCode = configuration.getModuleCode();
		LOGGER.debug("Saving module code " + moduleCode);

		List<String> environments = new ArrayList<String>();
		environments.add(com.coretex.core.business.constants.Constants.TEST_ENVIRONMENT);
		environments.add(com.coretex.core.business.constants.Constants.PRODUCTION_ENVIRONMENT);

		model.addAttribute("environments", environments);
		model.addAttribute("configuration", configuration);

		try {
			shippingService.saveShippingQuoteModuleConfiguration(configuration, store);
		} catch (Exception e) {

			throw new RuntimeException(e);
		}


		model.addAttribute("success", "success");
		return ControllerConstants.Tiles.Shipping.shippingMethod;


	}


	@RequestMapping(value = "/admin/shipping/deleteShippingMethod.html", method = RequestMethod.POST)
	public String deleteShippingMethod(@RequestParam("code") String code, Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {

		this.setMenu(model, request);
		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);
		shippingService.removeShippingQuoteModuleConfiguration(code, store);

		return "redirect:/admin/shipping/shippingMethods.html";

	}


	private void setMenu(Model model, HttpServletRequest request) throws Exception {

		//display menu
		Map<String, String> activeMenus = new HashMap<String, String>();
		activeMenus.put("shipping", "shipping");
		activeMenus.put("shipping-methods", "shipping-methods");

		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>) request.getAttribute("MENUMAP");

		Menu currentMenu = menus.get("shipping");
		model.addAttribute("currentMenu", currentMenu);
		model.addAttribute("activeMenus", activeMenus);
		//

	}


}
