package com.coretex.newpost.controllers;

import com.coretex.core.activeorm.services.ItemService;
import com.coretex.core.business.constants.Constants;
import com.coretex.core.business.services.shipping.DeliveryService;
import com.coretex.core.data.web.Menu;
import com.coretex.items.commerce_core_model.DeliveryServiceItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.newpost.NewPostDeliveryServiceItem;
import com.coretex.newpost.api.NewPostApiService;
import com.coretex.newpost.api.address.data.properties.SettlementsProperties;
import com.coretex.newpost.api.address.data.properties.WarehousesProperties;
import com.coretex.newpost.data.NewPostDeliveryServiceData;
import com.coretex.newpost.populators.NewPostDeliveryServiceDataPopulator;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.LocaleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class NewPostShippingServiceController {

	@Resource
	private DeliveryService deliveryService;

	@Resource
	private ItemService itemService;

	@Resource
	private NewPostDeliveryServiceDataPopulator newPostDeliveryServiceDataPopulator;

	@Resource
	private NewPostApiService newPostApiService;

	private static final Logger LOGGER = LoggerFactory.getLogger(NewPostShippingServiceController.class);

	@PreAuthorize("hasRole('SHIPPING')")
	@RequestMapping(value = "/admin/shipping/method/{code}", method = RequestMethod.GET)
	public String displayShippingMethod(@PathVariable("code") String code, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		this.setMenu(model, request);
		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute("ADMIN_STORE");
		DeliveryServiceItem deliveryServiceItem = deliveryService.getByCode(code);

		var result = newPostDeliveryServiceDataPopulator.populate((NewPostDeliveryServiceItem) deliveryServiceItem, store, null);
		model.addAttribute("deliveryService", result);
		model.addAttribute("includeFragment", "/fragment/shipping/type/novaposhtaMethod.jsp");

		List<String> environments = new ArrayList<>();
		return "shipping-method";
	}

	@PreAuthorize("hasRole('SHIPPING')")
	@RequestMapping(value = "/admin/shipping/method/{code}/switch", method = RequestMethod.PUT)
	public String switchActivationShippingService(@PathVariable("code") String code, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		this.setMenu(model, request);
		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute("ADMIN_STORE");
		DeliveryServiceItem deliveryServiceItem = deliveryService.getByCode(code);
		deliveryServiceItem.setActive(BooleanUtils.negate(deliveryServiceItem.getActive()));
		itemService.save(deliveryServiceItem);
		return UrlBasedViewResolver.REDIRECT_URL_PREFIX + "/admin/shipping/method/" + code;
	}

	@RequestMapping(value = Constants.SHOP_URI + "/order/dservice/{code}/city", method = RequestMethod.GET)
	@ResponseBody
	public Object getNewPostCities(@PathVariable("code") String code, @RequestParam("q") String query) throws Exception {
		var deliveryServiceItem = deliveryService.getByCode(code);
		SettlementsProperties settlements = new SettlementsProperties();
		settlements.setFindByString(query);
		settlements.setWarehouse("1");
		var result = newPostApiService.getNewPostAddressApiService().getSettlements((NewPostDeliveryServiceItem) deliveryServiceItem, settlements);

		return result.getData();
	}

	@RequestMapping(value = Constants.SHOP_URI + "/order/dservice/{code}/office", method = RequestMethod.GET)
	@ResponseBody
	public Object getNewPostOfficeBySettlement(@PathVariable("code") String code, @RequestParam("ref") String settlement) throws Exception {
		var deliveryServiceItem = deliveryService.getByCode(code);
		var options = new WarehousesProperties();
		options.setSettlementRef(settlement);
		var result = newPostApiService.getNewPostAddressApiService().getWarehouses((NewPostDeliveryServiceItem) deliveryServiceItem, options);

		return result.getData();
	}

	@PreAuthorize("hasRole('SHIPPING')")
	@RequestMapping(value = "/admin/shipping/method/save", method = RequestMethod.POST)
	public String saveDeliveryService(@ModelAttribute("deliveryService") NewPostDeliveryServiceData deliveryService, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		NewPostDeliveryServiceItem deliveryServiceItem = (NewPostDeliveryServiceItem) this.deliveryService.getByUUID(deliveryService.getUuid());

		deliveryService.getName().forEach((key, value) -> deliveryServiceItem.setName(value, LocaleUtils.toLocale(key)));

		deliveryServiceItem.setEndpoint(deliveryService.getEndpoint());
		deliveryServiceItem.setApiKey(deliveryService.getApiKey());
		deliveryServiceItem.setActive(deliveryService.getActive());

		itemService.save(deliveryServiceItem);

		return UrlBasedViewResolver.REDIRECT_URL_PREFIX + "/admin/shipping/method/" + deliveryServiceItem.getCode();
	}

	@PreAuthorize("hasRole('SHIPPING')")
	@RequestMapping(value = "/admin/shipping/method/{code}/type/{typeCode}", method = RequestMethod.GET)
	public String switchActivationShippingType(@PathVariable("code") String code, @PathVariable("typeCode") String typeCode, Model model, HttpServletRequest request, HttpServletResponse response) {
		NewPostDeliveryServiceItem deliveryServiceItem = (NewPostDeliveryServiceItem) deliveryService.getByCode(code);
		var type = deliveryServiceItem.getDeliveryTypes().stream().filter(newPostDeliveryTypeItem -> newPostDeliveryTypeItem.getCode().equals(typeCode)).findAny();

		if (type.isPresent()) {
			var postDeliveryTypeItem = type.get();
			postDeliveryTypeItem.setActive(BooleanUtils.negate(postDeliveryTypeItem.getActive()));
			itemService.save(postDeliveryTypeItem);
		}

		return UrlBasedViewResolver.REDIRECT_URL_PREFIX + "/admin/shipping/method/" + code;
	}


	private void setMenu(Model model, HttpServletRequest request) {

		//display menu
		Map<String, String> activeMenus = new HashMap<>();
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
