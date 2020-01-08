package com.coretex.shop.admin.controller.merchant;


import com.coretex.core.business.modules.email.Email;
import com.coretex.core.business.services.merchant.MerchantStoreService;
import com.coretex.core.business.services.reference.country.CountryService;
import com.coretex.core.business.services.reference.currency.CurrencyService;
import com.coretex.core.business.services.reference.language.LanguageService;
import com.coretex.core.business.services.reference.zone.ZoneService;
import com.coretex.core.business.services.system.EmailService;
import com.coretex.core.business.services.user.UserService;
import com.coretex.core.business.utils.ajax.AjaxResponse;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.core.CountryItem;
import com.coretex.items.cx_core.CurrencyItem;
import com.coretex.items.core.LocaleItem;
import com.coretex.items.cx_core.ZoneItem;
import com.coretex.items.cx_core.UserItem;
import com.coretex.shop.admin.controller.ControllerConstants;
import com.coretex.shop.admin.model.reference.Size;
import com.coretex.shop.admin.model.reference.Weight;
import com.coretex.core.data.web.Menu;
import com.coretex.shop.constants.Constants;
import com.coretex.shop.constants.EmailConstants;
import com.coretex.shop.model.shop.PersistableMerchantStore;
import com.coretex.shop.model.shop.ReadableMerchantStore;
import com.coretex.shop.populator.store.PersistableMerchantStorePopulator;
import com.coretex.shop.populator.store.ReadableMerchantStorePopulator;
import com.coretex.shop.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.*;

import static com.coretex.core.business.constants.Constants.DEFAULT_STORE;

@Controller
public class MerchantStoreController {

	private static final Logger LOGGER = LoggerFactory.getLogger(MerchantStoreController.class);

	@Resource
	private MerchantStoreService merchantStoreService;

	@Resource
	private CountryService countryService;

	@Resource
	private ZoneService zoneService;

	@Resource
	private LanguageService languageService;

	@Resource
	private CurrencyService currencyService;

	@Resource
	private UserService userService;

	@Resource
	private LabelUtils messages;

	@Resource
	private EmailService emailService;

	@Resource
	private EmailUtils emailUtils;

	@Resource
	private FilePathUtils filePathUtils;

	@Resource
	private ImageFilePathUtils imageFilePathUtils;



	private final static String NEW_STORE_TMPL = "email_template_new_store.ftl";

	@PreAuthorize("hasRole('STORE_ADMIN')")
	@RequestMapping(value = "/admin/store/list.html", method = RequestMethod.GET)
	public String displayStores(Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {


		setMenu(model, request);
		return ControllerConstants.Tiles.Store.stores;
	}

	@PreAuthorize("hasRole('STORE_ADMIN')")
	@RequestMapping(value = "/admin/store/paging.html", method = RequestMethod.POST)
	public @ResponseBody
	ResponseEntity<String> pageStores(HttpServletRequest request,
									  HttpServletResponse response) {

		AjaxResponse resp = new AjaxResponse();
		try {

			List<MerchantStoreItem> stores = merchantStoreService.list();

			for (MerchantStoreItem store : stores) {

				if (!store.getCode().equals(DEFAULT_STORE)) {
					Map<String, String> entry = new HashMap<String, String>();
					entry.put("storeId", String.valueOf(store.getUuid()));
					entry.put("code", store.getCode());
					entry.put("name", store.getStoreName());
					entry.put("email", store.getStoreEmailAddress());
					resp.addDataEntry(entry);
				}

			}

			resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);

		} catch (Exception e) {
			LOGGER.error("Error while paging products", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}

		String returnString = resp.toJSONString();

		return new ResponseEntity<String>(returnString, HttpStatus.OK);
	}

	@PreAuthorize("hasRole('STORE')")
	@RequestMapping(value = "/admin/store/storeCreate.html", method = RequestMethod.GET)
	public String displayMerchantStoreCreate(Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {


		setMenu(model, request);

		MerchantStoreItem store = new MerchantStoreItem();

		MerchantStoreItem sessionStore = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);
		store.setCurrency(sessionStore.getCurrency());
		store.setCountry(sessionStore.getCountry());
		store.setZone(sessionStore.getZone());
		store.setStoreStateProvince(sessionStore.getStoreStateProvince());
		store.setDomainName(sessionStore.getDomainName());


		return displayMerchantStore(store, model, request, response, locale);
	}

	@PreAuthorize("hasRole('STORE')")
	@RequestMapping(value = "/admin/store/store.html", method = RequestMethod.GET)
	public String displayMerchantStore(Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {

		setMenu(model, request);
		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);
		return displayMerchantStore(store, model, request, response, locale);
	}


	@PreAuthorize("hasRole('STORE')")
	@RequestMapping(value = "/admin/store/editStore.html", method = RequestMethod.GET)
	public String displayMerchantStore(@ModelAttribute("id") String id, Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {

		setMenu(model, request);
		MerchantStoreItem store = merchantStoreService.getByUUID(UUID.fromString(id));
		return displayMerchantStore(store, model, request, response, locale);
	}

	private String displayMerchantStore(MerchantStoreItem store, Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {


		setMenu(model, request);
		LocaleItem language = (LocaleItem) request.getAttribute("LANGUAGE");
		List<LocaleItem> languages = languageService.getLanguages();
		List<CurrencyItem> currencies = currencyService.list();
		Date dt = store.getInBusinessSince();
		if (dt != null) {
			store.setDateBusinessSince(DateUtil.formatDate(dt));
		} else {
			store.setDateBusinessSince(DateUtil.formatDate(new Date()));
		}

		//get countries
		List<CountryItem> countries = countryService.getCountries(language);

		List<Weight> weights = new ArrayList<Weight>();
		weights.add(new Weight("LB", messages.getMessage("label.generic.weightunit.LB", locale)));
		weights.add(new Weight("KG", messages.getMessage("label.generic.weightunit.KG", locale)));

		List<Size> sizes = new ArrayList<Size>();
		sizes.add(new Size("CM", messages.getMessage("label.generic.sizeunit.CM", locale)));
		sizes.add(new Size("IN", messages.getMessage("label.generic.sizeunit.IN", locale)));

		//display menu

		model.addAttribute("countries", countries);
		model.addAttribute("languages", languages);
		model.addAttribute("currencies", currencies);

		model.addAttribute("weights", weights);
		model.addAttribute("sizes", sizes);
		ReadableMerchantStorePopulator populator = new ReadableMerchantStorePopulator();
		populator.setCountryService(countryService);
		populator.setZoneService(zoneService);
		populator.setFilePath(imageFilePathUtils);
		model.addAttribute("store", populator.populate(store, new ReadableMerchantStore(), null, language));


		return "admin-store";


	}


	@PreAuthorize("hasRole('STORE')")
	@RequestMapping(value = "/admin/store/save.html", method = RequestMethod.POST)
	public String saveMerchantStore(@Valid @ModelAttribute("store") PersistableMerchantStore storeData, BindingResult result, Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {

		setMenu(model, request);
		MerchantStoreItem sessionStore = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);



		if (storeData.getId() != null) {
			if (!storeData.getId().equals(sessionStore.getUuid())) {
				return "redirect:/admin/store/store.html";
			}
		}

		var store = merchantStoreService.getByUUID(storeData.getId());


		PersistableMerchantStorePopulator populator = new PersistableMerchantStorePopulator();
		populator.setCountryService(countryService);
		populator.setZoneService(zoneService);
		populator.setLanguageService(languageService);
		populator.setCurrencyService(currencyService);

		populator.populate(storeData, store, sessionStore, sessionStore.getDefaultLanguage());

		Date date;
		if (!StringUtils.isBlank(store.getDateBusinessSince())) {
			try {
				date = DateUtil.getDate(store.getDateBusinessSince());
				store.setInBusinessSince(date);
			} catch (Exception e) {
				ObjectError error = new ObjectError("dateBusinessSince", messages.getMessage("message.invalid.date", locale));
				result.addError(error);
			}
		}

		List<CurrencyItem> currencies = currencyService.list();


		LocaleItem language = (LocaleItem) request.getAttribute("LANGUAGE");
		List<LocaleItem> languages = languageService.getLanguages();

		//get countries
		List<CountryItem> countries = countryService.getCountries(language);

		List<Weight> weights = new ArrayList<Weight>();
		weights.add(new Weight("LB", messages.getMessage("label.generic.weightunit.LB", locale)));
		weights.add(new Weight("KG", messages.getMessage("label.generic.weightunit.KG", locale)));

		List<Size> sizes = new ArrayList<Size>();
		sizes.add(new Size("CM", messages.getMessage("label.generic.sizeunit.CM", locale)));
		sizes.add(new Size("IN", messages.getMessage("label.generic.sizeunit.IN", locale)));

		model.addAttribute("weights", weights);
		model.addAttribute("sizes", sizes);

		model.addAttribute("countries", countries);
		model.addAttribute("languages", languages);
		model.addAttribute("currencies", currencies);


		CountryItem c = store.getCountry();
		List<ZoneItem> zonesList = zoneService.getZones(c, language);

		if ((zonesList == null || zonesList.size() == 0) && StringUtils.isBlank(store.getStoreStateProvince())) {

			ObjectError error = new ObjectError("zone.code", messages.getMessage("merchant.zone.invalid", locale));
			result.addError(error);

		}

		if (result.hasErrors()) {
			return "admin-store";
		}

		//get country
		CountryItem country = store.getCountry();
		country = countryService.getByCode(country.getIsoCode());
		ZoneItem zone = store.getZone();
		if (zone != null) {
			zone = zoneService.getByCode(zone.getCode());
		}
		CurrencyItem currency = store.getCurrency();
		currency = currencyService.getByUUID(currency.getUuid());

		LocaleItem defaultLanguage = store.getDefaultLanguage();
		defaultLanguage = languageService.getByUUID(defaultLanguage.getUuid());
		if (defaultLanguage != null) {
			store.setDefaultLanguage(defaultLanguage);
		}

		Locale storeLocale = LocaleUtils.getLocale(defaultLanguage);

		store.setStoreTemplate(sessionStore.getStoreTemplate());
		store.setCountry(country);
		store.setZone(zone);
		store.setCurrency(currency);
		store.setDefaultLanguage(defaultLanguage);


		merchantStoreService.saveOrUpdate(store);

		if (!store.getCode().equals(sessionStore.getCode())) {//create store
			//send email

			try {


				Map<String, String> templateTokens = emailUtils.createEmailObjectsMap(request.getContextPath(), store, messages, storeLocale);
				templateTokens.put(EmailConstants.EMAIL_NEW_STORE_TEXT, messages.getMessage("email.newstore.text", storeLocale));
				templateTokens.put(EmailConstants.EMAIL_STORE_NAME, messages.getMessage("email.newstore.name", new String[]{store.getStoreName()}, storeLocale));
				templateTokens.put(EmailConstants.EMAIL_ADMIN_STORE_INFO_LABEL, messages.getMessage("email.newstore.info", storeLocale));

				templateTokens.put(EmailConstants.EMAIL_ADMIN_URL_LABEL, messages.getMessage("label.adminurl", storeLocale));
				templateTokens.put(EmailConstants.EMAIL_ADMIN_URL, filePathUtils.buildAdminUri(store, request));


				Email email = new Email();
				email.setFrom(store.getStoreName());
				email.setFromEmail(store.getStoreEmailAddress());
				email.setSubject(messages.getMessage("email.newstore.title", storeLocale));
				email.setTo(store.getStoreEmailAddress());
				email.setTemplateName(NEW_STORE_TMPL);
				email.setTemplateTokens(templateTokens);


				emailService.sendHtmlEmail(store, email);

			} catch (Exception e) {
				LOGGER.error("Cannot send email to user", e);
			}

		}

		sessionStore = merchantStoreService.getByCode(sessionStore.getCode());


		//update session store
		request.getSession().setAttribute(Constants.ADMIN_STORE, sessionStore);


		model.addAttribute("success", "success");
		return displayMerchantStore(store, model, request, response, locale);
	}

	@PreAuthorize("hasRole('AUTH')")
	@RequestMapping(value = "/admin/store/checkStoreCode.html", method = RequestMethod.POST)
	public @ResponseBody
	ResponseEntity<String> checkStoreCode(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		String code = request.getParameter("code");


		AjaxResponse resp = new AjaxResponse();


		try {

			if (StringUtils.isBlank(code)) {
				resp.setStatus(AjaxResponse.CODE_ALREADY_EXIST);
				return new ResponseEntity<String>(resp.toJSONString(), HttpStatus.OK);
			}

			MerchantStoreItem store = merchantStoreService.getByCode(code);


			if (store != null) {
				resp.setStatus(AjaxResponse.CODE_ALREADY_EXIST);
				return new ResponseEntity<String>(resp.toJSONString(), HttpStatus.OK);
			}


			resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);

		} catch (Exception e) {
			LOGGER.error("Error while getting user", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}

		String returnString = resp.toJSONString();


		return new ResponseEntity<String>(returnString, HttpStatus.OK);
	}


	@PreAuthorize("hasRole('STORE_ADMIN')")
	@RequestMapping(value = "/admin/store/remove.html", method = RequestMethod.POST)
	public @ResponseBody
	ResponseEntity<String> removeMerchantStore(HttpServletRequest request, Locale locale) throws Exception {

		String sMerchantStoreId = request.getParameter("storeId");

		AjaxResponse resp = new AjaxResponse();

		try {

			UUID storeId = UUID.fromString(sMerchantStoreId);
			MerchantStoreItem store = merchantStoreService.getByUUID(storeId);

			UserItem user = userService.getByUserName(request.getRemoteUser());

			/**
			 * In order to remove a Store the logged in ser must be SUPERADMIN
			 */

			//check if the user removed has group SUPERADMIN
			boolean isSuperAdmin = false;
			if (UserUtils.userInGroup(user, Constants.GROUP_SUPERADMIN)) {
				isSuperAdmin = true;
			}


			if (!isSuperAdmin) {
				resp.setStatusMessage(messages.getMessage("message.security.caanotremovesuperadmin", locale));
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
				String returnString = resp.toJSONString();
				return new ResponseEntity<String>(returnString, HttpStatus.OK);
			}

			merchantStoreService.delete(store);

			resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);


		} catch (Exception e) {
			LOGGER.error("Error while deleting product price", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}

		String returnString = resp.toJSONString();

		return new ResponseEntity<String>(returnString, HttpStatus.OK);

	}

	private void setMenu(Model model, HttpServletRequest request) throws Exception {

		//display menu
		Map<String, String> activeMenus = new HashMap<String, String>();
		activeMenus.put("store", "store");
		activeMenus.put("storeDetails", "storeDetails");


		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>) request.getAttribute("MENUMAP");

		Menu currentMenu = menus.get("store");
		model.addAttribute("currentMenu", currentMenu);
		model.addAttribute("activeMenus", activeMenus);
		//

	}

}
