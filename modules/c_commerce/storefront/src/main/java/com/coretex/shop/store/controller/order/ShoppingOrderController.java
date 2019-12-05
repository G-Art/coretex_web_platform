package com.coretex.shop.store.controller.order;

import com.coretex.core.activeorm.services.ItemService;

import com.coretex.core.business.repositories.area.CityDao;
import com.coretex.core.business.services.catalog.product.PricingService;
import com.coretex.core.business.services.catalog.product.ProductService;
import com.coretex.core.business.services.customer.CustomerService;
import com.coretex.core.business.services.order.OrderService;
import com.coretex.core.business.services.order.orderproduct.OrderProductDownloadService;
import com.coretex.core.business.services.reference.country.CountryService;
import com.coretex.core.business.services.reference.language.LanguageService;
import com.coretex.core.business.services.reference.zone.ZoneService;
import com.coretex.core.business.services.shipping.DeliveryService;
import com.coretex.core.business.services.shipping.ShippingService;
import com.coretex.core.business.services.shoppingcart.ShoppingCartService;
import com.coretex.core.model.order.OrderTotalSummary;
import com.coretex.core.model.shipping.ShippingMetaData;
import com.coretex.core.model.shipping.ShippingOption;
import com.coretex.core.model.shipping.ShippingSummary;
import com.coretex.items.commerce_core_model.BillingItem;
import com.coretex.items.commerce_core_model.CityItem;
import com.coretex.items.commerce_core_model.CustomerItem;
import com.coretex.items.commerce_core_model.DeliveryItem;
import com.coretex.items.commerce_core_model.DeliveryServiceItem;
import com.coretex.items.core.LocaleItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.OrderItem;
import com.coretex.items.commerce_core_model.OrderProductDownloadItem;
import com.coretex.items.commerce_core_model.OrderTotalItem;
import com.coretex.items.commerce_core_model.ProductItem;
import com.coretex.items.commerce_core_model.ShoppingCartEntryItem;
import com.coretex.items.commerce_core_model.ShoppingCartItem;
import com.coretex.items.commerce_core_model.ZoneItem;
import com.coretex.items.core.CountryItem;
import com.coretex.items.newpost.NewPostDeliveryServiceItem;
import com.coretex.newpost.api.NewPostApiService;
import com.coretex.newpost.dao.NewPostDeliveryTypeDao;
import com.coretex.newpost.data.NewPostDeliveryServiceData;
import com.coretex.newpost.facades.NewPostFacade;
import com.coretex.newpost.populators.NewPostDeliveryServiceDataPopulator;
import com.coretex.shop.constants.Constants;
import com.coretex.shop.model.customer.AnonymousCustomer;
import com.coretex.shop.model.customer.PersistableCustomer;
import com.coretex.shop.model.order.ReadableShopOrder;
import com.coretex.shop.model.order.ShopOrder;
import com.coretex.shop.model.order.shipping.ReadableShippingSummary;
import com.coretex.shop.model.order.total.ReadableOrderTotal;
import com.coretex.shop.model.shoppingcart.ShoppingCartData;
import com.coretex.shop.populator.customer.PersistableCustomerPopulator;
import com.coretex.shop.populator.order.ReadableOrderTotalPopulator;
import com.coretex.shop.populator.order.ReadableShippingSummaryPopulator;
import com.coretex.shop.populator.order.ReadableShopOrderPopulator;
import com.coretex.shop.store.controller.AbstractController;
import com.coretex.shop.store.controller.ControllerConstants;
import com.coretex.shop.store.controller.customer.facade.CustomerFacade;
import com.coretex.shop.store.controller.order.facade.OrderFacade;
import com.coretex.shop.store.controller.shoppingCart.facade.ShoppingCartFacade;
import com.coretex.shop.utils.EmailTemplatesUtils;
import com.coretex.shop.utils.LabelUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;


/**
 * Displays checkout form and deals with ajax user input
 *
 * @author carlsamson
 */
@Controller
@RequestMapping(Constants.SHOP_URI + "/order")
public class ShoppingOrderController extends AbstractController {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ShoppingOrderController.class);

	@Resource
	private ItemService itemService;

	@Resource
	private ShoppingCartFacade shoppingCartFacade;

	@Resource
	private ShoppingCartService shoppingCartService;


	@Resource
	private CustomerService customerService;

	@Resource
	private ShippingService shippingService;

	@Resource
	private OrderService orderService;

	@Resource
	private CountryService countryService;

	@Resource
	private ZoneService zoneService;

	@Resource
	private OrderFacade orderFacade;

	@Resource
	private CustomerFacade customerFacade;

	@Resource
	private LabelUtils messages;

	@Resource
	private PricingService pricingService;

	@Resource
	private ProductService productService;

	@Resource
	private PasswordEncoder passwordEncoder;

	@Resource
	private AuthenticationManager customerAuthenticationManager;

	@Resource
	private EmailTemplatesUtils emailTemplatesUtils;

	@Resource
	private DeliveryService deliveryService;

	@Resource
	private OrderProductDownloadService orderProdctDownloadService;

	@Resource
	private NewPostDeliveryServiceDataPopulator newPostDeliveryServiceDataPopulator;

	@Resource
	private NewPostDeliveryTypeDao newPostDeliveryTypeDao;

	@Resource
	private NewPostApiService newPostApiService;

	@Resource
	private NewPostFacade newPostFacade;

	@Resource
	private CityDao cityDao;

	@Resource
	private LanguageService languageService;


	@RequestMapping("/checkout.html")
	public String displayCheckout(@CookieValue("cart") String cookie, Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {

		LocaleItem language = (LocaleItem) request.getAttribute("LANGUAGE");
		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.MERCHANT_STORE);
		CustomerItem customer = getSessionService().getSessionAttribute(Constants.CUSTOMER, CustomerItem.class);

		//check if an existing order exist
		ShopOrder order = null;
		order = getSessionService().getSessionAttribute(Constants.ORDER, ShopOrder.class);

		//Get the cart from the DB
		String shoppingCartCode = getSessionService().getSessionAttribute(Constants.SHOPPING_CART, String.class);
		ShoppingCartItem cart = null;

		if (StringUtils.isBlank(shoppingCartCode)) {

			if (cookie == null) {//session expired and cookie null, nothing to do
				return "redirect:/shop/cart/shoppingCart.html";
			}
			String[] merchantCookie = cookie.split("_");
			String merchantStoreCode = merchantCookie[0];
			if (!merchantStoreCode.equals(store.getCode())) {
				return "redirect:/shop/cart/shoppingCart.html";
			}
			shoppingCartCode = merchantCookie[1];

		}


		var shoppingCartData = shoppingCartFacade.getShoppingCartData(shoppingCartCode, store, language);
		cart = shoppingCartFacade.getShoppingCartModel(shoppingCartCode, store);


		if (cart == null && customer != null) {
			cart = shoppingCartFacade.getShoppingCartModel(customer, store);
		}
		boolean allAvailables = true;
		//Filter items, delete unavailable
		Set<ShoppingCartEntryItem> availables = new HashSet<ShoppingCartEntryItem>();
		//Take out items no more available
		Set<ShoppingCartEntryItem> items = cart.getLineItems();
		for (ShoppingCartEntryItem item : items) {

			UUID id = item.getProduct().getUuid();
			ProductItem p = productService.getByUUID(id);
			if (p.getAvailable()) {
				availables.add(item);
			} else {
				allAvailables = false;
			}
		}
		cart.setLineItems(availables);

		if (!allAvailables) {
			shoppingCartFacade.saveOrUpdateShoppingCart(cart);
		}

		getSessionService().setSessionAttribute(Constants.SHOPPING_CART, cart.getShoppingCartCode());

		if (shoppingCartCode == null && cart == null) {//error
			return "redirect:/shop/cart/shoppingCart.html";
		}


		if (customer != null) {
			if (!cart.getCustomerId().equals(customer.getUuid())) {
				return "redirect:/shop/shoppingCart.html";
			}
		} else {
			customer = orderFacade.initEmptyCustomer(store);
			AnonymousCustomer anonymousCustomer = (AnonymousCustomer) request.getAttribute(Constants.ANONYMOUS_CUSTOMER);
			if (anonymousCustomer != null && anonymousCustomer.getBilling() != null) {
				BillingItem billing = customer.getBilling();
				billing.setCity(anonymousCustomer.getBilling().getCity());
				Map<String, CountryItem> countriesMap = countryService.getCountriesMap(language);
				CountryItem anonymousCountry = countriesMap.get(anonymousCustomer.getBilling().getCountry());
				if (anonymousCountry != null) {
					billing.setCountry(anonymousCountry);
				}
				Map<String, ZoneItem> zonesMap = zoneService.getZones(language);
				ZoneItem anonymousZone = zonesMap.get(anonymousCustomer.getBilling().getZone());
				if (anonymousZone != null) {
					billing.setZone(anonymousZone);
				}
				if (anonymousCustomer.getBilling().getPostalCode() != null) {
					billing.setPostalCode(anonymousCustomer.getBilling().getPostalCode());
				}
				customer.setBilling(billing);
			}
		}


		if (CollectionUtils.isEmpty(items)) {
			return "redirect:/shop/shoppingCart.html";
		}

		if (order == null) {
			order = orderFacade.initializeOrder(store, customer, cart, language);
		}

		List<? extends DeliveryServiceItem> availableDeliveryServicesForStore = deliveryService.getAvailableDeliveryServicesForStore(store);

//		TODO: create facade implementation for all delivery services in future
		List<NewPostDeliveryServiceData> newPostDeliveryServiceData = availableDeliveryServicesForStore
				.stream()
				.filter(ds -> ds instanceof NewPostDeliveryServiceItem)
				.map(ds -> newPostDeliveryServiceDataPopulator.populate((NewPostDeliveryServiceItem) ds, store, language))
				.collect(Collectors.toList());

		model.addAttribute("deliveryServices", newPostDeliveryServiceData);

		//readable shopping cart items for order summary box
		ShoppingCartData shoppingCart = shoppingCartFacade.getShoppingCartData(cart, language);
		model.addAttribute("cart", shoppingCart);
		//order total
		OrderTotalSummary orderTotalSummary = orderFacade.calculateOrderTotal(store, order, language);
		cart.setSubTotal(orderTotalSummary.getSubTotal());
		itemService.save(cart);
		order.setOrderTotalSummary(orderTotalSummary);
		//if order summary has to be re-used
		getSessionService().setSessionAttribute(Constants.ORDER_SUMMARY, orderTotalSummary);

		model.addAttribute("order", order);

		/** template **/
		StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Checkout.checkout).append(".").append(store.getStoreTemplate());
		return template.toString();

	}


	@RequestMapping("/commitPreAuthorized.html")
	public String commitPreAuthorizedOrder(Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {

		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.MERCHANT_STORE);
		LocaleItem language = (LocaleItem) request.getAttribute("LANGUAGE");
		ShopOrder order = super.getSessionAttribute(Constants.ORDER, request);
		if (order == null) {
			StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Pages.timeout).append(".").append(store.getStoreTemplate());
			return template.toString();
		}

		@SuppressWarnings("unchecked")
		Map<String, Object> configs = (Map<String, Object>) request.getAttribute(Constants.REQUEST_CONFIGS);

		if (configs != null && configs.containsKey(Constants.DEBUG_MODE)) {
			Boolean debugMode = (Boolean) configs.get(Constants.DEBUG_MODE);
			if (debugMode) {
				try {
					ObjectMapper mapper = new ObjectMapper();
					String jsonInString = mapper.writeValueAsString(order);
					LOGGER.debug("Commit pre-authorized order -> " + jsonInString);
				} catch (Exception de) {
					LOGGER.error(de.getMessage());
				}
			}
		}


		try {

			OrderTotalSummary totalSummary = super.getSessionAttribute(Constants.ORDER_SUMMARY, request);

			if (totalSummary == null) {
				totalSummary = orderFacade.calculateOrderTotal(store, order, language);
				super.setSessionAttribute(Constants.ORDER_SUMMARY, totalSummary, request);
			}


			order.setOrderTotalSummary(totalSummary);

			//already validated, proceed with commit
			OrderItem orderModel = this.commitOrder(order, request, locale);
			super.setSessionAttribute(Constants.ORDER_ID, orderModel.getUuid(), request);

			return "redirect:/shop/order/confirmation.html";

		} catch (Exception e) {
			LOGGER.error("Error while commiting order", e);
			throw e;

		}

	}


	private OrderItem commitOrder(ShopOrder order, HttpServletRequest request, Locale locale) throws Exception {

		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.MERCHANT_STORE);
		LocaleItem language = (LocaleItem) request.getAttribute("LANGUAGE");
		String shoppingCartCode = getSessionService().getSessionAttribute(Constants.SHOPPING_CART, String.class);

		String userName = null;
		String password = null;

		var cart = shoppingCartFacade.getShoppingCartModel(shoppingCartCode, store);


		/** set username and password to persistable object **/
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		CustomerItem authCustomer = null;
		if (auth != null &&
				request.isUserInRole("AUTH_CUSTOMER")) {
			authCustomer = customerFacade.getCustomerByUserName(auth.getName(), store);

		}

		CustomerItem modelCustomer = customerService.getByEmail(order.getEmail());
		if (authCustomer == null) {//not authenticated, create a new volatile user

			if (Objects.isNull(modelCustomer)) {
				modelCustomer = orderFacade.initEmptyCustomer(store);
				customerFacade.setCustomerModelDefaultProperties(modelCustomer, store);
			}
			userName = modelCustomer.getFirstName();
			LOGGER.debug("About to persist volatile customer to database.");
			modelCustomer.setEmail(order.getEmail());
			modelCustomer.setAnonymous(true);
			modelCustomer.setLogin(order.getEmail());

			var deliveryItem = new DeliveryItem();
			deliveryItem.setCity(order.getCityCode().toString());
			deliveryItem.setTelephone(order.getPhone());
			deliveryItem.setCountry(store.getCountry());
			deliveryItem.setFirstName(order.getUserName());

			modelCustomer.setDelivery(deliveryItem);

			customerService.saveOrUpdate(modelCustomer);
//			} else {//use existing customer
//				modelCustomer = customerFacade.populateCustomerModel(authCustomer, customer, store, language);
//			}
		}


		OrderItem modelOrder = null;

		modelOrder = orderFacade.processOrder(order, modelCustomer, store, language);

		//save order id in session
		super.setSessionAttribute(Constants.ORDER_ID, modelOrder.getUuid(), request);
		//set a unique token for confirmation
		super.setSessionAttribute(Constants.ORDER_ID_TOKEN, modelOrder.getUuid(), request);


		//get cart
		String cartCode = super.getSessionAttribute(Constants.SHOPPING_CART, request);
		if (StringUtils.isNotBlank(cartCode)) {

			shoppingCartFacade.deleteShoppingCart(cartCode, store);

		}


		//cleanup the order objects
		super.removeAttribute(Constants.ORDER, request);
		super.removeAttribute(Constants.ORDER_SUMMARY, request);
		super.removeAttribute(Constants.INIT_TRANSACTION_KEY, request);
		super.removeAttribute(Constants.SHIPPING_OPTIONS, request);
		super.removeAttribute(Constants.SHIPPING_SUMMARY, request);
		super.removeAttribute(Constants.SHOPPING_CART, request);


		try {

			//check if any downloads exist for this order6
			List<OrderProductDownloadItem> orderProductDownloads = orderProdctDownloadService.getByOrderId(modelOrder.getUuid());
			if (CollectionUtils.isNotEmpty(orderProductDownloads)) {

				LOGGER.debug("Is user authenticated ? ", auth.isAuthenticated());
				if (auth != null &&
						request.isUserInRole("AUTH_CUSTOMER")) {
					//already authenticated
				} else {
					//authenticate
					customerFacade.authenticate(modelCustomer, userName, password);
					super.setSessionAttribute(Constants.CUSTOMER, modelCustomer, request);
				}

			}

			//send order confirmation email to customer
			emailTemplatesUtils.sendOrderEmail(modelCustomer.getEmail(), modelCustomer, modelOrder, locale, language, store, request.getContextPath());

			if (orderService.hasDownloadFiles(modelOrder)) {
				emailTemplatesUtils.sendOrderDownloadEmail(modelCustomer, modelOrder, store, locale, request.getContextPath());

			}

			//send order confirmation email to merchant
			emailTemplatesUtils.sendOrderEmail(store.getStoreEmailAddress(), modelCustomer, modelOrder, locale, language, store, request.getContextPath());


		} catch (Exception e) {
			LOGGER.error("Error while post processing order", e);
		}


		return modelOrder;


	}


	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/commitOrder.html", method = RequestMethod.POST)
	public String commitOrder(@CookieValue("cart") String cookie, @Valid @ModelAttribute(value = "order") ShopOrder order, BindingResult bindingResult, Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {

		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.MERCHANT_STORE);
		LocaleItem language = (LocaleItem) request.getAttribute("LANGUAGE");
		//validate if session has expired

		model.addAttribute("order", order);

		Map<String, Object> configs = (Map<String, Object>) request.getAttribute(Constants.REQUEST_CONFIGS);

		if (configs != null && configs.containsKey(Constants.DEBUG_MODE)) {
			Boolean debugMode = (Boolean) configs.get(Constants.DEBUG_MODE);
			if (debugMode) {
				try {
					ObjectMapper mapper = new ObjectMapper();
					String jsonInString = mapper.writeValueAsString(order);
					LOGGER.debug("Commit order -> " + jsonInString);
				} catch (Exception de) {
					LOGGER.error(de.getMessage());
				}
			}
		}

		try {

			/**
			 *
			 * Retrieve shopping cart and metadata
			 * (information required to process order)
			 *
			 * - Cart rerieved from cookie or from user session
			 * - Retrieves payment metadata
			 */
			ShippingMetaData shippingMetaData = shippingService.getShippingMetaData(store);
			model.addAttribute("shippingMetaData", shippingMetaData);
			//basic stuff
			String shoppingCartCode = (String) request.getSession().getAttribute(Constants.SHOPPING_CART);
			if (shoppingCartCode == null) {

				if (cookie == null) {//session expired and cookie null, nothing to do
					StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Pages.timeout).append(".").append(store.getStoreTemplate());
					return template.toString();
				}
				String[] merchantCookie = cookie.split("_");
				String merchantStoreCode = merchantCookie[0];
				if (!merchantStoreCode.equals(store.getCode())) {
					StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Pages.timeout).append(".").append(store.getStoreTemplate());
					return template.toString();
				}
				shoppingCartCode = merchantCookie[1];
			}
			ShoppingCartItem cart;

			if (StringUtils.isBlank(shoppingCartCode)) {
				StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Pages.timeout).append(".").append(store.getStoreTemplate());
				return template.toString();
			}
			cart = shoppingCartFacade.getShoppingCartModel(shoppingCartCode, store);

			//readable shopping cart items for order summary box
			ShoppingCartData shoppingCart = shoppingCartFacade.getShoppingCartData(cart, language);
			model.addAttribute("cart", shoppingCart);

			Set<ShoppingCartEntryItem> items = cart.getLineItems();
			List<ShoppingCartEntryItem> cartItems = new ArrayList<ShoppingCartEntryItem>(items);
			order.setShoppingCartItems(cartItems);


			List<CountryItem> countries = countryService.getCountries(language);
			model.addAttribute("countries", countries);

			//set shipping summary
			if (order.getSelectedShippingOption() != null) {
				ShippingSummary summary = (ShippingSummary) request.getSession().getAttribute(Constants.SHIPPING_SUMMARY);
				@SuppressWarnings("unchecked")
				List<ShippingOption> options = (List<ShippingOption>) request.getSession().getAttribute(Constants.SHIPPING_OPTIONS);

				ReadableShippingSummary readableSummary = new ReadableShippingSummary();
				ReadableShippingSummaryPopulator readableSummaryPopulator = new ReadableShippingSummaryPopulator();
				readableSummaryPopulator.setPricingService(pricingService);
				readableSummaryPopulator.populate(summary, readableSummary, store, language);


				if (!CollectionUtils.isEmpty(options)) {

					//get submitted shipping option
					ShippingOption quoteOption = null;
					ShippingOption selectedOption = order.getSelectedShippingOption();

					//check if selectedOption exist
					for (ShippingOption shipOption : options) {
						if (!StringUtils.isBlank(shipOption.getOptionId()) && shipOption.getOptionId().equals(selectedOption.getOptionId())) {
							quoteOption = shipOption;
						}

					}
					if (quoteOption == null) {
						quoteOption = options.get(0);
					}

					readableSummary.setSelectedShippingOption(quoteOption);
					readableSummary.setShippingOptions(options);
					summary.setShippingOption(quoteOption.getOptionId());
					summary.setShipping(quoteOption.getOptionPrice());

				}

				order.setShippingSummary(summary);
			}


			/**
			 * Calculate order total summary
			 */

			OrderTotalSummary totalSummary = super.getSessionAttribute(Constants.ORDER_SUMMARY, request);

//			if (totalSummary == null) {
//				totalSummary = orderFacade.calculateOrderTotal(store, cart, order, language);;
//				super.setSessionAttribute(Constants.ORDER_SUMMARY, totalSummary, request);
//			}


			order.setOrderTotalSummary(totalSummary);

			if (bindingResult.hasErrors()) {
				LOGGER.info("found {} validation error while validating in customer registration ",
						bindingResult.getErrorCount());
				String message = null;
				List<ObjectError> errors = bindingResult.getAllErrors();
				if (!CollectionUtils.isEmpty(errors)) {
					for (ObjectError error : errors) {
						message = error.getDefaultMessage();
						break;
					}
				}
				model.addAttribute("errorMessages", message);
				StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Checkout.checkout).append(".").append(store.getStoreTemplate());
				return template.toString();

			}

			@SuppressWarnings("unused")
			OrderItem modelOrder = this.commitOrder(order, request, locale);


		} catch (Exception e) {
			LOGGER.error("Error while commiting order", e);
			throw e;
		}

		//redirect to completd
		return "redirect:/shop/order/confirmation.html";


	}

	/**
	 * Calculates the order total following price variation like changing a shipping option
	 *
	 * @param order
	 * @param request
	 * @param response
	 * @param locale
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = {"/calculateOrderTotal.json"}, method = RequestMethod.POST)
	public @ResponseBody
	ReadableShopOrder calculateOrderTotal(@ModelAttribute(value = "order") ShopOrder order, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {

		LocaleItem language = (LocaleItem) request.getAttribute("LANGUAGE");
		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.MERCHANT_STORE);
		CustomerItem customer = getSessionService().getSessionAttribute(Constants.CUSTOMER, CustomerItem.class);
		String shoppingCartCode = getSessionAttribute(Constants.SHOPPING_CART, request);

		if (isNull(customer)) {
			customer = orderFacade.initEmptyCustomer(store);
			AnonymousCustomer anonymousCustomer = (AnonymousCustomer) request.getAttribute(Constants.ANONYMOUS_CUSTOMER);
			if (anonymousCustomer != null && anonymousCustomer.getBilling() != null) {
				BillingItem billing = customer.getBilling();
				billing.setCity(anonymousCustomer.getBilling().getCity());
				Map<String, CountryItem> countriesMap = countryService.getCountriesMap(language);
				CountryItem anonymousCountry = countriesMap.get(anonymousCustomer.getBilling().getCountry());
				if (anonymousCountry != null) {
					billing.setCountry(anonymousCountry);
				}
				Map<String, ZoneItem> zonesMap = zoneService.getZones(language);
				ZoneItem anonymousZone = zonesMap.get(anonymousCustomer.getBilling().getZone());
				if (anonymousZone != null) {
					billing.setZone(anonymousZone);
				}
				if (anonymousCustomer.getBilling().getPostalCode() != null) {
					billing.setPostalCode(anonymousCustomer.getBilling().getPostalCode());
				}
				customer.setBilling(billing);
			}
		}

		Validate.notNull(shoppingCartCode, "shoppingCartCode does not exist in the session");

		ReadableShopOrder readableOrder = new ReadableShopOrder();
		try {

			//re-generate cart
			ShoppingCartItem cart = shoppingCartFacade.getShoppingCartModel(shoppingCartCode, store);

			cart.setEmail(order.getEmail());
			cart.setPhone(order.getPhone());
			cart.setUserName(order.getUserName());
			if (Objects.nonNull(order.getCityCode())) {
				var city = cityDao.findByCode(order.getCityCode().toString());
				if (isNull(city)) {
					city = new CityItem();
					city.setCode(order.getCityCode().toString());
					city.setName(order.getCity(), languageService.toLocale(language, store));
				}
				cart.setSettlement(city);

			}
			if (Objects.nonNull(order.getDeliveryMethod())) {
				var dt = newPostDeliveryTypeDao.findByCode(order.getDeliveryMethod());
				cart.setDeliveryType(dt);
			}

			ReadableShopOrderPopulator populator = new ReadableShopOrderPopulator();
			populator.populate(order, readableOrder, store, language);

			//set list of shopping cart items for core price calculation
			List<ShoppingCartEntryItem> items = new ArrayList<ShoppingCartEntryItem>(cart.getLineItems());
			order.setShoppingCartItems(items);

			PersistableCustomerPopulator customerPopulator = new PersistableCustomerPopulator();
			var persistableCustomer = customerPopulator.populate(customer, new PersistableCustomer(), store, language);

			order.setCustomer(persistableCustomer);

			if (Objects.nonNull(order.getDeliveryMethod())) {
				ShippingSummary shippingSummary = newPostFacade.calculateShippingSummary(order.getDeliveryMethod(), order.getCityCode().toString(), cart);
				order.setShippingSummary(shippingSummary);
			}

			//order total calculation
			OrderTotalSummary orderTotalSummary = orderFacade.calculateOrderTotal(store, order, language);
			cart.setSubTotal(orderTotalSummary.getSubTotal());
			itemService.save(cart);

			super.setSessionAttribute(Constants.ORDER_SUMMARY, orderTotalSummary, request);


			ReadableOrderTotalPopulator totalPopulator = new ReadableOrderTotalPopulator();
			totalPopulator.setMessages(messages);
			totalPopulator.setPricingService(pricingService);

			List<ReadableOrderTotal> subtotals = new ArrayList<ReadableOrderTotal>();
			for (OrderTotalItem total : orderTotalSummary.getTotals()) {
				if (total.getOrderTotalCode() == null || !total.getOrderTotalCode().equals("order.total.total")) {
					ReadableOrderTotal t = new ReadableOrderTotal();
					totalPopulator.populate(total, t, store, language);
					subtotals.add(t);
				} else {//grand total
					ReadableOrderTotal ot = new ReadableOrderTotal();
					totalPopulator.populate(total, ot, store, language);
					readableOrder.setGrandTotal(ot.getTotal());
				}
			}


			readableOrder.setSubTotals(subtotals);

		} catch (Exception e) {
			LOGGER.error("Error while getting shipping quotes", e);
			readableOrder.setErrorMessage(messages.getMessage("message.error", locale));
		}

		return readableOrder;
	}


}