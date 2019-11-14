package com.coretex.shop.store.controller.customer;

import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.business.services.customer.CustomerService;
import com.coretex.core.business.services.customer.attribute.CustomerAttributeService;
import com.coretex.core.business.services.customer.attribute.CustomerOptionService;
import com.coretex.core.business.services.customer.attribute.CustomerOptionSetService;
import com.coretex.core.business.services.customer.attribute.CustomerOptionValueService;
import com.coretex.core.business.services.order.OrderService;
import com.coretex.core.business.services.reference.country.CountryService;
import com.coretex.core.business.services.reference.language.LanguageService;
import com.coretex.core.business.services.reference.zone.ZoneService;
import com.coretex.core.business.utils.ajax.AjaxResponse;
import com.coretex.items.commerce_core_model.CustomerItem;
import com.coretex.items.commerce_core_model.CustomerAttributeItem;
import com.coretex.items.commerce_core_model.CustomerOptionItem;
import com.coretex.core.model.customer.attribute.CustomerOptionType;
import com.coretex.items.commerce_core_model.CustomerOptionValueItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.core.CountryItem;
import com.coretex.items.commerce_core_model.LanguageItem;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.*;

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
	private CustomerOptionService customerOptionService;

	@Resource
	private CustomerOptionValueService customerOptionValueService;

	@Resource
	private CustomerOptionSetService customerOptionSetService;

	@Resource
	private CustomerAttributeService customerAttributeService;

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


		LanguageItem lang = languageUtils.getRequestLanguage(request, response);

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

		List<CustomerAttributeItem> customerAttributes = customerAttributeService.getByCustomer(store, customer);
		Map<UUID, CustomerAttributeItem> customerAttributesMap = new HashMap<>();

		for (CustomerAttributeItem attr : customerAttributes) {
			customerAttributesMap.put(attr.getCustomerOption().getUuid(), attr);
		}

		parameterNames = request.getParameterNames();

		while (parameterNames.hasMoreElements()) {

			String parameterName = (String) parameterNames.nextElement();
			String parameterValue = request.getParameter(parameterName);
			try {

				String[] parameterKey = parameterName.split("-");
				CustomerOptionItem customerOption = null;
				CustomerOptionValueItem customerOptionValue = null;


				if (CUSTOMER_ID_PARAMETER.equals(parameterName)) {
					continue;
				}

				if (parameterKey.length > 1) {
					//parse key - value
					String key = parameterKey[0];
					String value = parameterKey[1];
					//should be on
					customerOption = customerOptionService.getById(UUID.fromString(key));
					customerOptionValue = customerOptionValueService.getById(UUID.fromString(value));


				} else {
					customerOption = customerOptionService.getById(UUID.fromString(parameterName));
					customerOptionValue = customerOptionValueService.getById(UUID.fromString(parameterValue));

				}

				//get the attribute
				//CustomerAttributeItem attribute = customerAttributeService.getByCustomerOptionId(store, customer.getUuid(), customerOption.getUuid());
				CustomerAttributeItem attribute = customerAttributesMap.get(customerOption.getUuid());
				if (attribute == null) {
					attribute = new CustomerAttributeItem();
					attribute.setCustomer(customer);
					attribute.setCustomerOption(customerOption);
				} else {
					customerAttributes.remove(attribute);
				}

				if (customerOption.getCustomerOptionType().equals(CustomerOptionType.Text.name())) {
					if (!StringUtils.isBlank(parameterValue)) {
						attribute.setCustomerOptionValue(customerOptionValue);
						attribute.setTextValue(parameterValue);
					} else {
						attribute.setTextValue(null);
					}
				} else {
					attribute.setCustomerOptionValue(customerOptionValue);
				}


				if (attribute.getUuid() != null) {
					if (attribute.getCustomerOptionValue() == null) {
						customerAttributeService.delete(attribute);
					} else {
						customerAttributeService.update(attribute);
					}
				} else {
					customerAttributeService.save(attribute);
				}


			} catch (Exception e) {
				LOGGER.error("Cannot get parameter information " + parameterName, e);
			}

		}

		//and now the remaining to be removed
		for (CustomerAttributeItem attr : customerAttributes) {
			customerAttributeService.delete(attr);
		}

		//refresh customer
		CustomerItem c = customerService.getById(customer.getUuid());
		super.setSessionAttribute(Constants.CUSTOMER, c, request);

		resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
		String returnString = resp.toJSONString();
		return new ResponseEntity<String>(returnString, HttpStatus.OK);


	}

	@PreAuthorize("hasRole('AUTH_CUSTOMER')")
	@RequestMapping(value = "/billing.html", method = RequestMethod.GET)
	public String displayCustomerBillingAddress(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {


		MerchantStoreItem store = getSessionAttribute(Constants.MERCHANT_STORE, request);
		LanguageItem language = getSessionAttribute(Constants.LANGUAGE, request);

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


		LanguageItem language = getSessionAttribute(Constants.LANGUAGE, request);
		customerFacade.updateAddress(customer.getUuid(), store, address, language);

		CustomerItem c = customerService.getById(customer.getUuid());
		super.setSessionAttribute(Constants.CUSTOMER, c, request);

		model.addAttribute("success", "success");

		return template.toString();

	}


	@ModelAttribute("countries")
	protected List<CountryItem> getCountries(final HttpServletRequest request) {

		LanguageItem language = (LanguageItem) request.getAttribute("LANGUAGE");
		try {
			if (language == null) {
				language = (LanguageItem) request.getAttribute("LANGUAGE");
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
