package com.coretex.shop.store.controller.customer;

import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.business.services.customer.CustomerService;
import com.coretex.core.business.services.order.OrderService;
import com.coretex.core.business.services.reference.country.CountryService;
import com.coretex.core.business.services.reference.language.LanguageService;
import com.coretex.core.business.services.reference.zone.ZoneService;
import com.coretex.core.business.utils.ajax.AjaxResponse;
import com.coretex.items.commerce_core_model.CustomerItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.core.CountryItem;
import com.coretex.items.core.LocaleItem;
import com.coretex.shop.constants.Constants;
import com.coretex.shop.model.customer.CustomerEntity;
import com.coretex.shop.model.customer.CustomerPassword;
import com.coretex.shop.model.customer.ReadableCustomer;
import com.coretex.shop.model.customer.address.Address;
import com.coretex.shop.populator.customer.ReadableCustomerPopulator;
import com.coretex.shop.store.controller.AbstractController;
import com.coretex.shop.store.controller.ControllerConstants;
import com.coretex.shop.store.controller.customer.facade.CustomerFacade;
import com.coretex.shop.store.controller.order.facade.OrderFacade;
import com.coretex.shop.utils.EmailTemplatesUtils;
import com.coretex.shop.utils.LabelUtils;
import com.coretex.shop.utils.LanguageUtils;
import com.coretex.shop.utils.LocaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;

/**
 * Entry point for logged in customers
 *
 * @author Carl Samson
 */
@Controller
@RequestMapping("/shop/customer")
public class CustomerAccountController extends AbstractController {

	private static final String CUSTOMER_ID_PARAMETER = "customer";
	private static final String BILLING_SECTION = "/shop/customer/billing.html";

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerAccountController.class);

	@Resource
	private CustomerService customerService;

	@Resource
	private LanguageService languageService;

	@Resource
	private LanguageUtils languageUtils;

	@Resource
	private PasswordEncoder passwordEncoder;


	@Resource
	private CountryService countryService;

	@Resource
	private EmailTemplatesUtils emailTemplatesUtils;


	@Resource
	private ZoneService zoneService;

	@Resource
	private CustomerFacade customerFacade;

	@Resource
	private OrderService orderService;

	@Resource
	private OrderFacade orderFacade;

	@Resource
	private LabelUtils messages;


	/**
	 * Dedicated customer logon page
	 *
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/customLogon.html", method = RequestMethod.GET)
	public String displayLogon(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {


		MerchantStoreItem store = getSessionAttribute(Constants.MERCHANT_STORE, request);


		//dispatch to dedicated customer logon

		/** template **/
		StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Customer.customerLogon).append(".").append(store.getStoreTemplate());

		return template.toString();

	}


	@RequestMapping(value = "/accountSummary.json", method = RequestMethod.GET)
	public @ResponseBody
	ReadableCustomer customerInformation(@RequestParam String userName, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {


		MerchantStoreItem store = getSessionAttribute(Constants.MERCHANT_STORE, request);

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		CustomerItem customer = null;
		if (auth != null &&
				request.isUserInRole("AUTH_CUSTOMER")) {
			customer = customerFacade.getCustomerByUserName(auth.getName(), store);

		} else {
			response.sendError(401, "CustomerItem not authenticated");
			return null;
		}

		if (StringUtils.isBlank(userName)) {
			response.sendError(403, "CustomerItem name required");
			return null;
		}

		if (customer == null) {
			response.sendError(401, "CustomerItem not authenticated");
			return null;
		}

		if (!customer.getFirstName().equals(userName)) {
			response.sendError(401, "CustomerItem not authenticated");
			return null;
		}


		ReadableCustomer readableCustomer = new ReadableCustomer();


		LocaleItem lang = languageUtils.getRequestLanguage(request, response);

		ReadableCustomerPopulator readableCustomerPopulator = new ReadableCustomerPopulator();
		readableCustomerPopulator.populate(customer, readableCustomer, store, lang);

		return readableCustomer;

	}

	@PreAuthorize("hasRole('AUTH_CUSTOMER')")
	@RequestMapping(value = "/account.html", method = RequestMethod.GET)
	public String displayCustomerAccount(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {


		MerchantStoreItem store = getSessionAttribute(Constants.MERCHANT_STORE, request);


		/** template **/
		StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Customer.customer).append(".").append(store.getStoreTemplate());

		return template.toString();

	}

	@PreAuthorize("hasRole('AUTH_CUSTOMER')")
	@RequestMapping(value = "/password.html", method = RequestMethod.GET)
	public String displayCustomerChangePassword(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {


		MerchantStoreItem store = getSessionAttribute(Constants.MERCHANT_STORE, request);

		CustomerPassword customerPassword = new CustomerPassword();
		model.addAttribute("password", customerPassword);

		/** template **/
		StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Customer.changePassword).append(".").append(store.getStoreTemplate());

		return template.toString();

	}

	@PreAuthorize("hasRole('AUTH_CUSTOMER')")
	@RequestMapping(value = "/changePassword.html", method = RequestMethod.POST)
	public String changePassword(@Valid @ModelAttribute(value = "password") CustomerPassword password, BindingResult bindingResult, Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {


		MerchantStoreItem store = getSessionAttribute(Constants.MERCHANT_STORE, request);

		/** template **/
		StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Customer.changePassword).append(".").append(store.getStoreTemplate());

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		CustomerItem customer = null;
		if (auth != null &&
				request.isUserInRole("AUTH_CUSTOMER")) {
			customer = customerFacade.getCustomerByUserName(auth.getName(), store);

		}

		if (customer == null) {
			return "redirect:/" + Constants.SHOP_URI;
		}

		String currentPassword = password.getCurrentPassword();

		BCryptPasswordEncoder encoder = (BCryptPasswordEncoder) passwordEncoder;
		if (!encoder.matches(currentPassword, customer.getPassword())) {
			FieldError error = new FieldError("password", "password", messages.getMessage("message.invalidpassword", locale));
			bindingResult.addError(error);
		}


		if (bindingResult.hasErrors()) {
			LOGGER.info("found {} validation error while validating customer password",
					bindingResult.getErrorCount());
			return template.toString();

		}

		CustomerPassword customerPassword = new CustomerPassword();
		model.addAttribute("password", customerPassword);

		String newPassword = password.getPassword();
		String encodedPassword = passwordEncoder.encode(newPassword);

		customer.setPassword(encodedPassword);

		customerService.saveOrUpdate(customer);

		emailTemplatesUtils.changePasswordNotificationEmail(customer, store, LocaleUtils.getLocale(customer.getDefaultLanguage()), request.getContextPath());

		model.addAttribute("success", "success");

		return template.toString();

	}


	/**
	 * Manage the edition of customer attributes
	 *
	 * @param request
	 * @param locale
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasRole('AUTH_CUSTOMER')")
	@RequestMapping(value = {"/attributes/save.html"}, method = RequestMethod.POST)
	public @ResponseBody
	ResponseEntity<String> saveCustomerAttributes(HttpServletRequest request, Locale locale) throws Exception {


		AjaxResponse resp = new AjaxResponse();


		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.MERCHANT_STORE);

		//1=1&2=on&3=eeee&4=on&customer=1

		@SuppressWarnings("rawtypes")
		Enumeration parameterNames = request.getParameterNames();


		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		CustomerItem customer = null;
		if (auth != null &&
				request.isUserInRole("AUTH_CUSTOMER")) {
			customer = customerFacade.getCustomerByUserName(auth.getName(), store);

		}

		if (customer == null) {
			LOGGER.error("CustomerItem id [customer] is not defined in the parameters");
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			String returnString = resp.toJSONString();
			return new ResponseEntity<String>(returnString, HttpStatus.OK);
		}


		if (!customer.getMerchantStore().getUuid().equals(store.getUuid())) {
			LOGGER.error("CustomerItem id does not belong to current store");
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			String returnString = resp.toJSONString();
			return new ResponseEntity<String>(returnString, HttpStatus.OK);
		}

		parameterNames = request.getParameterNames();

		while (parameterNames.hasMoreElements()) {

			String parameterName = (String) parameterNames.nextElement();
			String parameterValue = request.getParameter(parameterName);
			try {

				String[] parameterKey = parameterName.split("-");


				if (CUSTOMER_ID_PARAMETER.equals(parameterName)) {
					continue;
				}

			} catch (Exception e) {
				LOGGER.error("Cannot get parameter information " + parameterName, e);
			}

		}

		//refresh customer
		CustomerItem c = customerService.getByUUID(customer.getUuid());
		super.setSessionAttribute(Constants.CUSTOMER, c, request);

		resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
		String returnString = resp.toJSONString();
		return new ResponseEntity<String>(returnString, HttpStatus.OK);


	}

	@PreAuthorize("hasRole('AUTH_CUSTOMER')")
	@RequestMapping(value = "/billing.html", method = RequestMethod.GET)
	public String displayCustomerBillingAddress(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {


		MerchantStoreItem store = getSessionAttribute(Constants.MERCHANT_STORE, request);
		LocaleItem language = getSessionAttribute(Constants.LANGUAGE, request);

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		CustomerItem customer = null;
		if (auth != null &&
				request.isUserInRole("AUTH_CUSTOMER")) {
			customer = customerFacade.getCustomerByUserName(auth.getName(), store);

		}

		if (customer == null) {
			return "redirect:/" + Constants.SHOP_URI;
		}


		CustomerEntity customerEntity = customerFacade.getCustomerDataByUserName(customer.getFirstName(), store, language);
		if (customer != null) {
			model.addAttribute("customer", customerEntity);
		}


		/** template **/
		StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Customer.Billing).append(".").append(store.getStoreTemplate());

		return template.toString();

	}

	@PreAuthorize("hasRole('AUTH_CUSTOMER')")
	@RequestMapping(value = "/editAddress.html", method = {RequestMethod.GET, RequestMethod.POST})
	public String editAddress(final Model model, final HttpServletRequest request,
							  @RequestParam(value = "billingAddress", required = false) Boolean billingAddress) throws Exception {
		MerchantStoreItem store = getSessionAttribute(Constants.MERCHANT_STORE, request);

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		CustomerItem customer = null;
		if (auth != null &&
				request.isUserInRole("AUTH_CUSTOMER")) {
			customer = customerFacade.getCustomerByUserName(auth.getName(), store);

		}

		if (customer == null) {
			return "redirect:/" + Constants.SHOP_URI;
		}


		Address address = customerFacade.getAddress(customer.getUuid(), store, billingAddress);
		model.addAttribute("address", address);
		model.addAttribute("customerId", customer.getUuid());
		StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Customer.EditAddress).append(".").append(store.getStoreTemplate());
		return template.toString();
	}


	@PreAuthorize("hasRole('AUTH_CUSTOMER')")
	@RequestMapping(value = "/updateAddress.html", method = {RequestMethod.GET, RequestMethod.POST})
	public String updateCustomerAddress(@Valid
										@ModelAttribute("address") Address address, BindingResult bindingResult, final Model model, final HttpServletRequest request,
										@RequestParam(value = "billingAddress", required = false) Boolean billingAddress) throws Exception {

		MerchantStoreItem store = getSessionAttribute(Constants.MERCHANT_STORE, request);

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		CustomerItem customer = null;
		if (auth != null &&
				request.isUserInRole("AUTH_CUSTOMER")) {
			customer = customerFacade.getCustomerByUserName(auth.getName(), store);

		}

		StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Customer.EditAddress).append(".").append(store.getStoreTemplate());

		if (customer == null) {
			return "redirect:/" + Constants.SHOP_URI;
		}

		model.addAttribute("address", address);
		model.addAttribute("customerId", customer.getUuid());


		if (bindingResult.hasErrors()) {
			LOGGER.info("found {} error(s) while validating  customer address ",
					bindingResult.getErrorCount());
			return template.toString();
		}


		LocaleItem language = getSessionAttribute(Constants.LANGUAGE, request);
		customerFacade.updateAddress(customer.getUuid(), store, address, language);

		CustomerItem c = customerService.getByUUID(customer.getUuid());
		super.setSessionAttribute(Constants.CUSTOMER, c, request);

		model.addAttribute("success", "success");

		return template.toString();

	}


	@ModelAttribute("countries")
	protected List<CountryItem> getCountries(final HttpServletRequest request) {

		LocaleItem language = (LocaleItem) request.getAttribute("LANGUAGE");
		try {
			if (language == null) {
				language = (LocaleItem) request.getAttribute("LANGUAGE");
			}

			if (language == null) {
				language = languageService.getByCode(Constants.DEFAULT_LANGUAGE);
			}

			List<CountryItem> countryList = countryService.getCountries(language);
			return countryList;
		} catch (ServiceException e) {
			LOGGER.error("Error while fetching country list ", e);

		}
		return Collections.emptyList();
	}

	//@ModelAttribute("zones")
	//public List<ZoneItem> getZones(final HttpServletRequest request){
	//    return zoneService.list();
	//}


}
