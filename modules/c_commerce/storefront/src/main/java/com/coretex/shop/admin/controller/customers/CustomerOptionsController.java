package com.coretex.shop.admin.controller.customers;

import com.coretex.core.business.services.customer.attribute.CustomerOptionService;
import com.coretex.core.business.services.reference.language.LanguageService;
import com.coretex.core.business.utils.ajax.AjaxResponse;
import com.coretex.items.commerce_core_model.CustomerOptionItem;
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
public class CustomerOptionsController {

	@Resource
	private LanguageService languageService;

	@Resource
	private CustomerOptionService customerOptionService;

	@Resource
	private LabelUtils messages;

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerOptionsController.class);


	@PreAuthorize("hasRole('CUSTOMER')")
	@RequestMapping(value = "/admin/customers/options/list.html", method = RequestMethod.GET)
	public String displayOptions(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		setMenu(model, request);
		return ControllerConstants.Tiles.Customer.optionsList;


	}

	@PreAuthorize("hasRole('CUSTOMER')")
	@RequestMapping(value = "/admin/customers/options/edit.html", method = RequestMethod.GET)
	public String displayOptionEdit(@RequestParam("id") String id, HttpServletRequest request, HttpServletResponse response, Model model, Locale locale) throws Exception {
		return displayOption(UUID.fromString(id), request, response, model, locale);
	}

	@PreAuthorize("hasRole('CUSTOMER')")
	@RequestMapping(value = "/admin/customers/options/create.html", method = RequestMethod.GET)
	public String displayOptionCreate(HttpServletRequest request, HttpServletResponse response, Model model, Locale locale) throws Exception {
		return displayOption(null, request, response, model, locale);
	}

	private String displayOption(UUID optionId, HttpServletRequest request, HttpServletResponse response, Model model, Locale locale) throws Exception {

		this.setMenu(model, request);
		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);

		CustomerOptionItem option = null;

		if (optionId != null) {//edit mode


			option = customerOptionService.getById(optionId);


			if (option == null) {
				return "redirect:/admin/customers/options/list.html";
			}

			if (!option.getMerchantStore().getUuid().equals(store.getUuid())) {
				return "redirect:/admin/customers/options/list.html";
			}


		}

		model.addAttribute("option", option);
		return ControllerConstants.Tiles.Customer.optionDetails;


	}


	@PreAuthorize("hasRole('CUSTOMER')")
	@RequestMapping(value = "/admin/customers/options/save.html", method = RequestMethod.POST)
	public String saveOption(@Valid @ModelAttribute("option") CustomerOptionItem option, BindingResult result, Model model, HttpServletRequest request, Locale locale) throws Exception {


		//display menu
		setMenu(model, request);

		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);
		CustomerOptionItem dbEntity = null;

		if (option.getUuid() != null) { //edit entry

			//get from DB
			dbEntity = customerOptionService.getById(option.getUuid());

			if (dbEntity == null) {
				return "redirect:/admin/options/options.html";
			}
		}

		//validate if it contains an existing code
		CustomerOptionItem byCode = customerOptionService.getByCode(store, option.getCode());
		if (byCode != null && option.getUuid() == null) {
			ObjectError error = new ObjectError("code", messages.getMessage("message.code.exist", locale));
			result.addError(error);
		}


		option.setMerchantStore(store);


		if (result.hasErrors()) {
			return ControllerConstants.Tiles.Customer.optionDetails;
		}


		customerOptionService.saveOrUpdate(option);


		model.addAttribute("success", "success");
		return ControllerConstants.Tiles.Customer.optionDetails;
	}


	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('CUSTOMER')")
	@RequestMapping(value = "/admin/customers/options/paging.html", method = RequestMethod.POST)
	public @ResponseBody
	ResponseEntity<String> pageOptions(HttpServletRequest request, HttpServletResponse response) {

		AjaxResponse resp = new AjaxResponse();


		try {


			LanguageItem language = (LanguageItem) request.getAttribute("LANGUAGE");

			MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);

			List<CustomerOptionItem> options;


			options = customerOptionService.listByStore(store, language);


			for (CustomerOptionItem option : options) {

				@SuppressWarnings("rawtypes")
				Map entry = new HashMap();
				entry.put("id", option.getUuid().toString());
				entry.put("name", option.getName());
				entry.put("type", option.getCustomerOptionType());
				entry.put("active", option.getActive());
				entry.put("public", option.getPublicOption());
				resp.addDataEntry(entry);


			}

			resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);


		} catch (Exception e) {
			LOGGER.error("Error while paging options", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}

		String returnString = resp.toJSONString();
		return new ResponseEntity<>(returnString, HttpStatus.OK);


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

	@PreAuthorize("hasRole('CUSTOMER')")
	@RequestMapping(value = "/admin/customers/options/remove.html", method = RequestMethod.POST)
	public @ResponseBody
	ResponseEntity<String> deleteOption(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		String sid = request.getParameter("id");

		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);

		AjaxResponse resp = new AjaxResponse();


		try {

			UUID id = UUID.fromString(sid);

			CustomerOptionItem entity = customerOptionService.getById(id);

			if (entity == null || !entity.getMerchantStore().getUuid().equals(store.getUuid())) {

				resp.setStatusMessage(messages.getMessage("message.unauthorized", locale));
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);

			} else {

				customerOptionService.delete(entity);
				resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);

			}


		} catch (Exception e) {
			LOGGER.error("Error while deleting option", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}

		String returnString = resp.toJSONString();
		return new ResponseEntity<>(returnString, HttpStatus.OK);
	}

}
