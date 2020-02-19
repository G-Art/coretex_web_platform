package com.coretex.newpost.controllers;

//@Controller
public class NewPostShippingServiceController {

//	@Resource
//	private DeliveryService deliveryService;
//
//	@Resource
//	private ItemService itemService;
//
//	@Resource
//	private NewPostDeliveryServiceDataPopulator newPostDeliveryServiceDataPopulator;
//
//	@Resource
//	private NewPostApiService newPostApiService;

//	private static final Logger LOGGER = LoggerFactory.getLogger(NewPostShippingServiceController.class);

//	@PreAuthorize("hasRole('SHIPPING')")
//	@RequestMapping(value = "/admin/shipping/method/{code}", method = RequestMethod.GET)
//	public String displayShippingMethod(@PathVariable("code") String code, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
//
//		this.setMenu(model, request);
//		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute("ADMIN_STORE");
//		DeliveryServiceItem deliveryServiceItem = deliveryService.getByCode(code);
//
//		var result = newPostDeliveryServiceDataPopulator.populate((NewPostDeliveryServiceItem) deliveryServiceItem, store, null);
//		model.addAttribute("deliveryService", result);
//		model.addAttribute("includeFragment", "/fragment/shipping/type/novaposhtaMethod.jsp");
//
//		List<String> environments = new ArrayList<>();
//		return "shipping-method";
//	}

//	@PreAuthorize("hasRole('SHIPPING')")
//	@RequestMapping(value = "/admin/shipping/method/{code}/switch", method = RequestMethod.PUT)
//	public String switchActivationShippingService(@PathVariable("code") String code, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
//		this.setMenu(model, request);
//		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute("ADMIN_STORE");
//		DeliveryServiceItem deliveryServiceItem = deliveryService.getByCode(code);
//		deliveryServiceItem.setActive(BooleanUtils.negate(deliveryServiceItem.getActive()));
//		itemService.save(deliveryServiceItem);
//		return UrlBasedViewResolver.REDIRECT_URL_PREFIX + "/admin/shipping/method/" + code;
//	}

//	@RequestMapping(value = Constants.SHOP_URI + "/order/dservice/{code}/city", method = RequestMethod.GET)
//	@ResponseBody
//	public Object getNewPostCities(@PathVariable("code") String code, @RequestParam("q") String query) throws Exception {
//		var deliveryServiceItem = deliveryService.getByCode(code);
//		SettlementsProperties settlements = new SettlementsProperties();
//		settlements.setFindByString(query);
//		settlements.setWarehouse("1");
//		var result = newPostApiService.getNewPostAddressApiService().getSettlements((NewPostDeliveryServiceItem) deliveryServiceItem, settlements);
//
//		return result.getData();
//	}

//	@RequestMapping(value = Constants.SHOP_URI + "/order/dservice/{code}/office", method = RequestMethod.GET)
//	@ResponseBody
//	public Object getNewPostOfficeBySettlement(@PathVariable("code") String code, @RequestParam("ref") String settlement) throws Exception {
//		var deliveryServiceItem = deliveryService.getByCode(code);
//		var options = new WarehousesProperties();
//		options.setSettlementRef(settlement);
//		var result = newPostApiService.getNewPostAddressApiService().getWarehouses((NewPostDeliveryServiceItem) deliveryServiceItem, options);
//
//		return result.getData();
//	}

//	@PreAuthorize("hasRole('SHIPPING')")
//	@RequestMapping(value = "/admin/shipping/method/save", method = RequestMethod.POST)
//	public String saveDeliveryService(@ModelAttribute("deliveryService") NewPostDeliveryServiceData deliveryService, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
//
//		NewPostDeliveryServiceItem deliveryServiceItem = (NewPostDeliveryServiceItem) this.deliveryService.getByUUID(deliveryService.getUuid());
//
//		deliveryService.getName().forEach((key, value) -> deliveryServiceItem.setName(value, LocaleUtils.toLocale(key)));
//
//		deliveryServiceItem.setEndpoint(deliveryService.getEndpoint());
//		deliveryServiceItem.setApiKey(deliveryService.getApiKey());
//		deliveryServiceItem.setActive(deliveryService.getActive());
//
//		itemService.save(deliveryServiceItem);
//
//		return UrlBasedViewResolver.REDIRECT_URL_PREFIX + "/admin/shipping/method/" + deliveryServiceItem.getCode();
//	}

//	@PreAuthorize("hasRole('SHIPPING')")
//	@RequestMapping(value = "/admin/shipping/method/{code}/type/{typeCode}", method = RequestMethod.GET)
//	public String switchActivationShippingType(@PathVariable("code") String code, @PathVariable("typeCode") String typeCode, Model model, HttpServletRequest request, HttpServletResponse response) {
//		NewPostDeliveryServiceItem deliveryServiceItem = (NewPostDeliveryServiceItem) deliveryService.getByCode(code);
//		var type = deliveryServiceItem.getDeliveryTypes().stream().filter(newPostDeliveryTypeItem -> newPostDeliveryTypeItem.getCode().equals(typeCode)).findAny();
//
//		if (type.isPresent()) {
//			var postDeliveryTypeItem = type.get();
//			postDeliveryTypeItem.setActive(BooleanUtils.negate(postDeliveryTypeItem.getActive()));
//			itemService.save(postDeliveryTypeItem);
//		}
//
//		return UrlBasedViewResolver.REDIRECT_URL_PREFIX + "/admin/shipping/method/" + code;
//	}


//	private void setMenu(Model model, HttpServletRequest request) {
//
//		//display menu
//		Map<String, String> activeMenus = new HashMap<>();
//		activeMenus.put("shipping", "shipping");
//		activeMenus.put("shipping-methods", "shipping-methods");
//
//		@SuppressWarnings("unchecked")
//		Map<String, Menu> menus = (Map<String, Menu>) request.getAttribute("MENUMAP");
//
//		Menu currentMenu = menus.get("shipping");
//		model.addAttribute("currentMenu", currentMenu);
//		model.addAttribute("activeMenus", activeMenus);
//		//
//
//	}
}
