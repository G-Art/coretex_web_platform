package com.coretex.shop.admin.controller.customers;

import com.coretex.core.business.services.customer.attribute.CustomerOptionValueService;
import com.coretex.core.business.services.reference.language.LanguageService;
import com.coretex.core.business.utils.ajax.AjaxResponse;
import com.coretex.items.commerce_core_model.CustomerOptionValueItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.shop.admin.controller.ControllerConstants;
import com.coretex.core.data.web.Menu;
import com.coretex.shop.constants.Constants;
import com.coretex.shop.utils.LabelUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.*;

@Controller
public class CustomerOptionsValueController {

	@Resource
	LanguageService languageService;


	@Resource
	private CustomerOptionValueService customerOptionValueService;

	@Resource
	LabelUtils messages;

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerOptionsValueController.class);

	/**
	 * Displays the list of customer options values
	 *
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasRole('CUSTOMER')")
	@RequestMapping(value = "/admin/customers/options/values/list.html", method = RequestMethod.GET)
	public String displayOptionValues(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {


		setMenu(model, request);
		return ControllerConstants.Tiles.Customer.optionsValuesList;


	}

	/**
	 * Display an option value in edit mode
	 *
	 * @param id
	 * @param request
	 * @param response
	 * @param model
	 * @param locale
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasRole('CUSTOMER')")
	@RequestMapping(value = "/admin/customers/options/values/edit.html", method = RequestMethod.GET)
	public String displayOptionValueEdit(@RequestParam("id") String id, HttpServletRequest request, HttpServletResponse response, Model model, Locale locale) throws Exception {
		return displayOption(UUID.fromString(id), request, response, model, locale);
	}

	@PreAuthorize("hasRole('CUSTOMER')")
	@RequestMapping(value = "/admin/customers/options/values/create.html", method = RequestMethod.GET)
	public String displayOptionValueCreate(HttpServletRequest request, HttpServletResponse response, Model model, Locale locale) throws Exception {
		return displayOption(null, request, response, model, locale);
	}

	private String displayOption(UUID id, HttpServletRequest request, HttpServletResponse response, Model model, Locale locale) throws Exception {

		this.setMenu(model, request);
		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);

		List<LanguageItem> languages = store.getLanguages();

		CustomerOptionValueItem option = new CustomerOptionValueItem();

		if (id != null) {//edit mode


			option = customerOptionValueService.getById(id);


			if (option == null) {
				return "redirect:/admin/customers/options/values/list.html";
			}

			if (!option.getMerchantStore().getUuid().equals(store.getUuid())) {
				return "redirect:/admin/customers/options/values/list.html";
			}


		}

		model.addAttribute("optionValue", option);
		return ControllerConstants.Tiles.Customer.optionsValueDetails;


	}


	@PreAuthorize("hasRole('CUSTOMER')")
	@RequestMapping(value = "/admin/customers/options/values/save.html", method = RequestMethod.POST)
	public String saveOption(@Valid @ModelAttribute("optionValue") CustomerOptionValueItem optionValue, BindingResult result, Model model, HttpServletRequest request, Locale locale) throws Exception {


		//display menu
		setMenu(model, request);

		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);
		CustomerOptionValueItem dbEntity = null;

		if (optionValue.getUuid() != null) { //edit entry

			//get from DB
			dbEntity = customerOptionValueService.getById(optionValue.getUuid());

			if (dbEntity == null) {
				return "redirect:/admin/customers/options/values/list.html";
			}

			if (!dbEntity.getMerchantStore().getUuid().equals(store.getUuid())) {
				return "redirect:/admin/customers/options/values/list.html";
			}
		}

		//validate if it contains an existing code
		CustomerOptionValueItem byCode = customerOptionValueService.getByCode(store, optionValue.getCode());
		if (byCode != null && optionValue.getUuid() == null) {
			ObjectError error = new ObjectError("code", messages.getMessage("message.code.exist", locale));
			result.addError(error);
		}


		Map<String, LanguageItem> langs = languageService.getLanguagesMap();


		optionValue.setMerchantStore(store);


		if (result.hasErrors()) {
			return ControllerConstants.Tiles.Customer.optionsValueDetails;
		}


		customerOptionValueService.saveOrUpdate(optionValue);

		model.addAttribute("success", "success");
		return ControllerConstants.Tiles.Customer.optionsValueDetails;
	}


	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('CUSTOMER')")
	@RequestMapping(value = "/admin/customers/options/values/paging.html", method = RequestMethod.POST)
	public @ResponseBody
	ResponseEntity<String> pageOptions(HttpServletRequest request, HttpServletResponse response) {

		AjaxResponse resp = new AjaxResponse();


		try {


			LanguageItem language = (LanguageItem) request.getAttribute("LANGUAGE");

			MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);

			List<CustomerOptionValueItem> options = null;


			options = customerOptionValueService.listByStore(store, language);

			for (CustomerOptionValueItem option : options) {

				@SuppressWarnings("rawtypes")
				Map entry = new HashMap();
				entry.put("id", option.getUuid());
				entry.put("code", option.getCode());
				entry.put("name", option.getName());
				resp.addDataEntry(entry);


			}

			resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);


		} catch (Exception e) {
			LOGGER.error("Error while paging options", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}

		String returnString = resp.toJSONString();
		return new ResponseEntity<String>(returnString, HttpStatus.OK);


	}

	@PreAuthorize("hasRole('CUSTOMER')")
	@RequestMapping(value = "/admin/customers/options/values/remove.html", method = RequestMethod.POST)
	public @ResponseBody
	ResponseEntity<String> deleteOptionValue(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		String sid = request.getParameter("id");

		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);

		AjaxResponse resp = new AjaxResponse();


		try {

			UUID id = UUID.fromString(sid);

			CustomerOptionValueItem entity = customerOptionValueService.getById(id);

			if (entity == null || !entity.getMerchantStore().getUuid().equals(store.getUuid())) {
				resp.setStatusMessage(messages.getMessage("message.unauthorized", locale));
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			} else {
				customerOptionValueService.delete(entity);
				resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);

			}


		} catch (Exception e) {
			LOGGER.error("Error while deleting option", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}

		String returnString = resp.toJSONString();
		return new ResponseEntity<String>(returnString, HttpStatus.OK);
	}


	private void setMenu(Model model, HttpServletRequest request) throws Exception {

		//display menu
		Map<String, String> activeMenus = new HashMap<String, String>();
		activeMenus.put("customer", "customer");
		activeMenus.put("customer-options", "customer-options");

		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>) request.getAttribute("MENUMAP");

		Menu currentMenu = menus.get("customer");
		model.addAttribute("currentMenu", currentMenu);
		model.addAttribute("activeMenus", activeMenus);
		//

	}

}
