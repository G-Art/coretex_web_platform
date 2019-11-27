package com.coretex.shop.admin.controller.customers;

import com.coretex.core.business.services.customer.CustomerService;
import com.coretex.core.business.services.reference.country.CountryService;
import com.coretex.core.business.services.reference.language.LanguageService;
import com.coretex.core.business.services.reference.zone.ZoneService;
import com.coretex.core.business.services.system.EmailService;
import com.coretex.core.business.services.user.GroupService;
import com.coretex.core.business.utils.ajax.AjaxPageableResponse;
import com.coretex.core.business.utils.ajax.AjaxResponse;
import com.coretex.core.data.web.Menu;
import com.coretex.core.model.customer.CustomerCriteria;
import com.coretex.core.model.customer.CustomerList;
import com.coretex.enums.commerce_core_model.GroupTypeEnum;
import com.coretex.items.commerce_core_model.CustomerItem;
import com.coretex.items.commerce_core_model.GroupItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.ZoneItem;
import com.coretex.items.core.CountryItem;
import com.coretex.items.core.LocaleItem;
import com.coretex.shop.constants.Constants;
import com.coretex.shop.store.controller.customer.facade.CustomerFacade;
import com.coretex.shop.utils.EmailUtils;
import com.coretex.shop.utils.LabelUtils;
import com.coretex.shop.utils.LocaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;


@Controller
public class CustomerController {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);

	private static final String CUSTOMER_ID_PARAMETER = "customer";


	@Resource
	private LabelUtils messages;

	@Resource
	private GroupService groupService;

	@Resource
	private CustomerService customerService;

	@Resource
	private CountryService countryService;

	@Resource
	private ZoneService zoneService;

	@Resource
	private LanguageService languageService;

	@Resource
	@Named("passwordEncoder")
	private PasswordEncoder passwordEncoder;

	@Resource
	private EmailService emailService;

	@Resource
	private EmailUtils emailUtils;

	@Resource
	private CustomerFacade customerFacade;


	/**
	 * CustomerItem details
	 *
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasRole('CUSTOMER')")
	@RequestMapping(value = "/admin/customers/customer.html", method = RequestMethod.GET)
	public String displayCustomer(String id, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		//display menu
		this.setMenu(model, request);

		//get groups
		List<GroupItem> groups = new ArrayList<GroupItem>();
		List<GroupItem> userGroups = groupService.listGroup(GroupTypeEnum.CUSTOMER);
		for (GroupItem group : userGroups) {
			groups.add(group);
		}

		model.addAttribute("groups", groups);

		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);

		List<LocaleItem> languages = languageService.getLanguages();

		model.addAttribute("languages", languages);

		CustomerItem customer = null;

		//if request.attribute contains id then get this customer from customerService
		if (id != null) {//edit mode

			//get from DB
			customer = customerService.getByUUID(UUID.fromString(id));
			if (customer == null) {
				return "redirect:/admin/customers/list.html";
			}
			if (!customer.getMerchantStore().getUuid().equals(store.getUuid())) {
				return "redirect:/admin/customers/list.html";
			}

		} else {
			customer = new CustomerItem();
		}
		//get list of countries (see merchant controller)
		LocaleItem language = (LocaleItem) request.getAttribute("LANGUAGE");
		//get countries
		List<CountryItem> countries = countryService.getCountries(language);

		//get list of zones
		List<ZoneItem> zones = zoneService.list();

		model.addAttribute("zones", zones);
		model.addAttribute("countries", countries);
		model.addAttribute("customer", customer);
		return "admin-customer";

	}

	@PreAuthorize("hasRole('CUSTOMER')")
	@RequestMapping(value = "/admin/customers/save.html", method = RequestMethod.POST)
	public String saveCustomer(@Valid @ModelAttribute("customer") CustomerItem customer, BindingResult result, Model model, HttpServletRequest request, Locale locale) throws Exception {

		this.setMenu(model, request);

		String email_regEx = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b";
		Pattern pattern = Pattern.compile(email_regEx);

		LocaleItem language = (LocaleItem) request.getAttribute("LANGUAGE");
		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);
		List<LocaleItem> languages = languageService.getLanguages();

		model.addAttribute("languages", languages);

		//get groups
		List<GroupItem> groups = new ArrayList<GroupItem>();
		List<GroupItem> userGroups = groupService.listGroup(GroupTypeEnum.CUSTOMER);
		for (GroupItem group : userGroups) {
			groups.add(group);
		}

		model.addAttribute("groups", groups);

		//get countries
		List<CountryItem> countries = countryService.getCountries(language);


		if (!StringUtils.isBlank(customer.getEmail())) {
			java.util.regex.Matcher matcher = pattern.matcher(customer.getEmail());

			if (!matcher.find()) {
				ObjectError error = new ObjectError("customerEmailAddress", messages.getMessage("Email.customer.EmailAddress", locale));
				result.addError(error);
			}
		} else {
			ObjectError error = new ObjectError("customerEmailAddress", messages.getMessage("NotEmpty.customer.EmailAddress", locale));
			result.addError(error);
		}


		if (StringUtils.isBlank(customer.getBilling().getFirstName())) {
			ObjectError error = new ObjectError("billingFirstName", messages.getMessage("NotEmpty.customer.billingFirstName", locale));
			result.addError(error);
		}

		if (StringUtils.isBlank(customer.getBilling().getLastName())) {
			ObjectError error = new ObjectError("billingLastName", messages.getMessage("NotEmpty.customer.billingLastName", locale));
			result.addError(error);
		}

		if (StringUtils.isBlank(customer.getBilling().getAddress())) {
			ObjectError error = new ObjectError("billingAddress", messages.getMessage("NotEmpty.customer.billingStreetAddress", locale));
			result.addError(error);
		}

		if (StringUtils.isBlank(customer.getBilling().getCity())) {
			ObjectError error = new ObjectError("billingCity", messages.getMessage("NotEmpty.customer.billingCity", locale));
			result.addError(error);
		}

		if (customer.getShowBillingStateList().equalsIgnoreCase("yes") && customer.getBilling().getZone().getCode() == null) {
			ObjectError error = new ObjectError("billingState", messages.getMessage("NotEmpty.customer.billingState", locale));
			result.addError(error);

		} else if (customer.getShowBillingStateList().equalsIgnoreCase("no") && customer.getBilling().getState() == null) {
			ObjectError error = new ObjectError("billingState", messages.getMessage("NotEmpty.customer.billingState", locale));
			result.addError(error);

		}

		if (StringUtils.isBlank(customer.getBilling().getPostalCode())) {
			ObjectError error = new ObjectError("billingPostalCode", messages.getMessage("NotEmpty.customer.billingPostCode", locale));
			result.addError(error);
		}

		//check if error from the @valid
		if (result.hasErrors()) {
			model.addAttribute("countries", countries);
			return "admin-customer";
		}

		CustomerItem newCustomer = new CustomerItem();

		if (customer.getUuid() != null) {
			newCustomer = customerService.getByUUID(customer.getUuid());

			if (newCustomer == null) {
				return "redirect:/admin/customers/list.html";
			}

			if (!newCustomer.getMerchantStore().getUuid().equals(store.getUuid())) {
				return "redirect:/admin/customers/list.html";
			}


		} else {
			//  new customer set marchant_Id
			MerchantStoreItem merchantStore = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);
			newCustomer.setMerchantStore(merchantStore);
		}

		List<GroupItem> submitedGroups = customer.getGroups();
		Set<UUID> ids = new HashSet<>();
		for (GroupItem group : submitedGroups) {
			ids.add(group.getUuid());
		}

		List<GroupItem> newGroups = groupService.listGroupByIds(ids);
		newCustomer.setGroups(newGroups);


		newCustomer.setEmail(customer.getEmail());

		//get CustomerItem country/zone
		CountryItem deliveryCountry = countryService.getByCode(customer.getDelivery().getCountry().getIsoCode());
		CountryItem billingCountry = countryService.getByCode(customer.getBilling().getCountry().getIsoCode());

		ZoneItem deliveryZone = customer.getDelivery().getZone();
		ZoneItem billingZone = customer.getBilling().getZone();


		if ("yes".equalsIgnoreCase(customer.getShowDeliveryStateList())) {
			if (customer.getDelivery().getZone() != null) {
				deliveryZone = zoneService.getByCode(customer.getDelivery().getZone().getCode());
				customer.getDelivery().setState(null);
			}

		} else if ("no".equalsIgnoreCase(customer.getShowDeliveryStateList())) {
			if (customer.getDelivery().getState() != null) {
				deliveryZone = null;
				customer.getDelivery().setState(customer.getDelivery().getState());
			}
		}

		if ("yes".equalsIgnoreCase(customer.getShowBillingStateList())) {
			if (customer.getBilling().getZone() != null) {
				billingZone = zoneService.getByCode(customer.getBilling().getZone().getCode());
				customer.getBilling().setState(null);
			}

		} else if ("no".equalsIgnoreCase(customer.getShowBillingStateList())) {
			if (customer.getBilling().getState() != null) {
				billingZone = null;
				customer.getBilling().setState(customer.getBilling().getState());
			}
		}


		newCustomer.setDefaultLanguage(customer.getDefaultLanguage());

		customer.getDelivery().setZone(deliveryZone);
		customer.getDelivery().setCountry(deliveryCountry);
		newCustomer.setDelivery(customer.getDelivery());

		customer.getBilling().setZone(billingZone);
		customer.getBilling().setCountry(billingCountry);
		newCustomer.setBilling(customer.getBilling());

		customerService.saveOrUpdate(newCustomer);

		model.addAttribute("customer", newCustomer);
		model.addAttribute("countries", countries);
		model.addAttribute("success", "success");

		return "admin-customer";

	}

	/**
	 * Deserves shop and admin
	 *
	 * @param request
	 * @param locale
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasRole('CUSTOMER')")
	@RequestMapping(value = {"/admin/customers/attributes/save.html"}, method = RequestMethod.POST)
	public @ResponseBody
	ResponseEntity<String> saveCustomerAttributes(HttpServletRequest request, Locale locale) throws Exception {


		AjaxResponse resp = new AjaxResponse();
		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);

		//1=1&2=on&3=eeee&4=on&customer=1

		@SuppressWarnings("rawtypes")
		Enumeration parameterNames = request.getParameterNames();

		CustomerItem customer = null;

		while (parameterNames.hasMoreElements()) {

			String parameterName = (String) parameterNames.nextElement();
			String parameterValue = request.getParameter(parameterName);
			if (CUSTOMER_ID_PARAMETER.equals(parameterName)) {
				customer = customerService.getByUUID(UUID.fromString(parameterValue));
				break;
			}
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

		resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
		String returnString = resp.toJSONString();
		return new ResponseEntity<String>(returnString, HttpStatus.OK);


	}


	/**
	 * List of customers
	 *
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/customers/list.html", method = RequestMethod.GET)
	public String displayCustomers(Model model, HttpServletRequest request) throws Exception {


		this.setMenu(model, request);

		return "admin-customers";


	}


	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/admin/customers/page.html", method = RequestMethod.POST)
	public @ResponseBody
	ResponseEntity<String> pageCustomers(HttpServletRequest request, HttpServletResponse response) {


		AjaxPageableResponse resp = new AjaxPageableResponse();

		//LocaleItem language = (LocaleItem)request.getAttribute("LANGUAGE");
		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);


		try {


			//Map<String,CountryItem> countriesMap = countryService.getCountriesMap(language);


			int startRow = Integer.parseInt(request.getParameter("_startRow"));
			int endRow = Integer.parseInt(request.getParameter("_endRow"));
			String email = request.getParameter("email");
			String name = request.getParameter("name");
			String firstName = request.getParameter("firstName");
			String lastName = request.getParameter("lastName");
			String country = request.getParameter("country");


			CustomerCriteria criteria = new CustomerCriteria();
			criteria.setStartIndex(startRow);
			criteria.setMaxCount(endRow);

			if (!StringUtils.isBlank(email)) {
				criteria.setEmail(email);
			}

			if (!StringUtils.isBlank(name)) {
				criteria.setName(name);
			}

			if (!StringUtils.isBlank(country)) {
				criteria.setCountry(country);
			}

			if (!StringUtils.isBlank(firstName)) {
				criteria.setFirstName(firstName);
			}

			if (!StringUtils.isBlank(lastName)) {
				criteria.setLastName(lastName);
			}


			CustomerList customerList = customerService.getListByStore(store, criteria);

			if (customerList.getCustomers() != null) {

				for (CustomerItem customer : customerList.getCustomers()) {
					@SuppressWarnings("rawtypes")
					Map entry = new HashMap();
					entry.put("id", customer.getUuid().toString());
					entry.put("firstName", customer.getBilling().getFirstName());
					entry.put("lastName", customer.getBilling().getLastName());
					entry.put("email", customer.getEmail());
					entry.put("country", customer.getBilling().getCountry().getIsoCode());
					resp.addDataEntry(entry);

				}

			}

		} catch (Exception e) {
			LOGGER.error("Error while paging orders", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}

		String returnString = resp.toJSONString();
		return new ResponseEntity<String>(returnString, HttpStatus.OK);


	}


	@PreAuthorize("hasRole('CUSTOMER')")
	@RequestMapping(value = "/admin/customers/resetPassword.html", method = RequestMethod.POST)
	public @ResponseBody
	ResponseEntity<String> resetPassword(HttpServletRequest request, HttpServletResponse response) {

		String customerId = request.getParameter("customerId");

		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);
		AjaxResponse resp = new AjaxResponse();


		try {

			UUID id = UUID.fromString(customerId);

			CustomerItem customer = customerService.getByUUID(id);

			if (customer == null) {
				resp.setErrorString("CustomerItem does not exist");
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
				String returnString = resp.toJSONString();
				return new ResponseEntity<String>(returnString, HttpStatus.OK);
			}

			if (!customer.getMerchantStore().getUuid().equals(store.getUuid())) {
				resp.setErrorString("Invalid customer id");
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
				String returnString = resp.toJSONString();
				return new ResponseEntity<String>(returnString, HttpStatus.OK);
			}

			LocaleItem userLanguage = customer.getDefaultLanguage();

			customerFacade.resetPassword(customer, store, userLanguage);

			resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);

		} catch (Exception e) {
			LOGGER.error("An exception occured while changing password", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}


		String returnString = resp.toJSONString();
		return new ResponseEntity<String>(returnString, HttpStatus.OK);


	}


	@PreAuthorize("hasRole('CUSTOMER')")
	@RequestMapping(value = "/admin/customers/setCredentials.html", method = RequestMethod.POST)
	public @ResponseBody
	ResponseEntity<String> setCredentials(HttpServletRequest request, HttpServletResponse response) {

		String customerId = request.getParameter("customerId");
		String userName = request.getParameter("userName");
		String password = request.getParameter("password");

		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);
		AjaxResponse resp = new AjaxResponse();

		try {

			UUID id = UUID.fromString(customerId);

			CustomerItem customer = customerService.getByUUID(id);

			if (customer == null) {
				resp.setErrorString("CustomerItem does not exist");
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
				String returnString = resp.toJSONString();
				return new ResponseEntity<>(returnString, HttpStatus.OK);
			}

			if (!customer.getMerchantStore().getUuid().equals(store.getUuid())) {
				resp.setErrorString("Invalid customer id");
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
				String returnString = resp.toJSONString();
				return new ResponseEntity<>(returnString, HttpStatus.OK);
			}

			if (StringUtils.isBlank(userName) || StringUtils.isBlank(password)) {
				resp.setErrorString("Invalid username or password");
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
				String returnString = resp.toJSONString();
				return new ResponseEntity<>(returnString, HttpStatus.OK);
			}

			LocaleItem userLanguage = customer.getDefaultLanguage();

			Locale customerLocale = LocaleUtils.getLocale(userLanguage);

			String encodedPassword = passwordEncoder.encode(password);

			customer.setPassword(encodedPassword);
			customer.setFirstName(userName);

			customerService.saveOrUpdate(customer);

			//send email
			
/*			try {

				//creation of a user, send an email
				String[] storeEmail = {store.getStoreEmailAddress()};
				
				
				Map<String, String> templateTokens = emailUtils.createEmailObjectsMap(request.getContextPath(), store, messages, customerLocale);
				templateTokens.put(EmailConstants.LABEL_HI, messages.getMessage("label.generic.hi", customerLocale));
		        templateTokens.put(EmailConstants.EMAIL_CUSTOMER_FIRSTNAME, customer.getBilling().getFirstName());
		        templateTokens.put(EmailConstants.EMAIL_CUSTOMER_LASTNAME, customer.getBilling().getLastName());
				templateTokens.put(EmailConstants.EMAIL_RESET_PASSWORD_TXT, messages.getMessage("email.customer.resetpassword.text", customerLocale));
				templateTokens.put(EmailConstants.EMAIL_CONTACT_OWNER, messages.getMessage("email.contactowner", storeEmail, customerLocale));
				templateTokens.put(EmailConstants.EMAIL_PASSWORD_LABEL, messages.getMessage("label.generic.password",customerLocale));
				templateTokens.put(EmailConstants.EMAIL_CUSTOMER_PASSWORD, password);


				Email email = new Email();
				email.setFrom(store.getStorename());
				email.setFromEmail(store.getStoreEmailAddress());
				email.setSubject(messages.getMessage("label.generic.changepassword",customerLocale));
				email.setTo(customer.getEmailAddress());
				email.setTemplateName(RESET_PASSWORD_TPL);
				email.setTemplateTokens(templateTokens);
	
	
				
				emailService.sendHtmlEmail(store, email);
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
			
			} catch (Exception e) {
				LOGGER.error("Cannot send email to user",e);
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			}*/


		} catch (Exception e) {
			LOGGER.error("An exception occured while changing password", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}


		String returnString = resp.toJSONString();
		return new ResponseEntity<>(returnString, HttpStatus.OK);


	}

	private void setMenu(Model model, HttpServletRequest request) throws Exception {

		//display menu
		Map<String, String> activeMenus = new HashMap<String, String>();
		activeMenus.put("customer", "customer");
		activeMenus.put("customer-list", "customer-list");

		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>) request.getAttribute("MENUMAP");

		Menu currentMenu = menus.get("customer");
		model.addAttribute("currentMenu", currentMenu);
		model.addAttribute("activeMenus", activeMenus);


		//

	}


}
