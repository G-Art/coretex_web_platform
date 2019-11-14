package com.coretex.shop.admin.controller.shipping;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.annotation.Resource;

import com.coretex.items.commerce_core_model.MerchantStoreItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.coretex.core.model.shipping.ShippingConfiguration;
import com.coretex.core.model.shipping.ShippingType;
import com.coretex.core.business.services.shipping.ShippingService;
import com.coretex.shop.admin.controller.ControllerConstants;
import com.coretex.core.data.web.Menu;
import com.coretex.shop.constants.Constants;
import com.coretex.shop.utils.LabelUtils;

@Controller
public class ShippingPackagingController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ShippingPackagingController.class);


	@Resource
	private ShippingService shippingService;

	@Resource
	LabelUtils messages;


	/**
	 * Displays shipping packaging
	 *
	 * @param request
	 * @param response
	 * @param locale
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasRole('SHIPPING')")
	@RequestMapping(value = "/admin/shipping/shippingPackaging.html", method = RequestMethod.GET)
	public String displayShippingPackaging(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {


		this.setMenu(model, request);

		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);

		ShippingConfiguration shippingConfiguration = shippingService.getShippingConfiguration(store);

		if (shippingConfiguration == null) {
			shippingConfiguration = new ShippingConfiguration();
			shippingConfiguration.setShippingType(ShippingType.INTERNATIONAL);
		}

		model.addAttribute("configuration", shippingConfiguration);
		model.addAttribute("store", store);
		return ControllerConstants.Tiles.Shipping.shippingPackaging;


	}

	/**
	 * Saves shipping packaging
	 *
	 * @param configuration
	 * @param result
	 * @param model
	 * @param request
	 * @param response
	 * @param locale
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasRole('SHIPPING')")
	@RequestMapping(value = "/admin/shipping/saveShippingPackaging.html", method = RequestMethod.POST)
	public String saveShippingPackaging(@ModelAttribute("configuration") ShippingConfiguration configuration, BindingResult result, Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {


		this.setMenu(model, request);
		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);

		//get original configuration
		ShippingConfiguration shippingConfiguration = shippingService.getShippingConfiguration(store);

		if (shippingConfiguration == null) {
			shippingConfiguration = new ShippingConfiguration();
		}

		DecimalFormat df = new DecimalFormat("#.##");
		String sweight = df.format(configuration.getBoxWeight());
		double weight = Double.parseDouble(sweight);

		shippingConfiguration.setBoxHeight(configuration.getBoxHeight());
		shippingConfiguration.setBoxLength(configuration.getBoxLength());
		shippingConfiguration.setBoxWeight(weight);
		shippingConfiguration.setBoxWidth(configuration.getBoxWidth());

		shippingConfiguration.setShipPackageType(configuration.getShipPackageType());


		shippingService.saveShippingConfiguration(shippingConfiguration, store);

		model.addAttribute("configuration", configuration);
		model.addAttribute("success", "success");
		return ControllerConstants.Tiles.Shipping.shippingPackaging;


	}

	private void setMenu(Model model, HttpServletRequest request) throws Exception {

		//display menu
		Map<String, String> activeMenus = new HashMap<String, String>();
		activeMenus.put("shipping", "shipping");
		activeMenus.put("shipping-packages", "shipping-packages");

		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>) request.getAttribute("MENUMAP");

		Menu currentMenu = menus.get("shipping");
		model.addAttribute("currentMenu", currentMenu);
		model.addAttribute("activeMenus", activeMenus);
		//

	}


}
