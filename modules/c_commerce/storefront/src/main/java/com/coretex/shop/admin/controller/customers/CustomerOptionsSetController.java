package com.coretex.shop.admin.controller.customers;

import com.coretex.items.commerce_core_model.CustomerOptionItem;
import com.coretex.items.commerce_core_model.CustomerOptionSetItem;
import com.coretex.items.commerce_core_model.CustomerOptionValueItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.coretex.core.business.services.customer.attribute.CustomerOptionService;
import com.coretex.core.business.services.customer.attribute.CustomerOptionSetService;
import com.coretex.core.business.services.customer.attribute.CustomerOptionValueService;
import com.coretex.core.business.services.reference.language.LanguageService;
import com.coretex.core.business.utils.ajax.AjaxResponse;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

@Controller
public class CustomerOptionsSetController {

	@Resource
	private LanguageService languageService;

	@Resource
	private CustomerOptionSetService customerOptionSetService;

	@Resource
	private CustomerOptionService customerOptionService;

	@Resource
	private CustomerOptionValueService customerOptionValueService;

	@Resource
	private LabelUtils messages;

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerOptionsSetController.class);


	@PreAuthorize("hasRole('CUSTOMER')")
	@RequestMapping(value = "/admin/customers/optionsset/list.html", method = RequestMethod.GET)
	public String displayOptions(Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {

		LanguageItem language = languageService.toLanguage(locale);


		this.setMenu(model, request);
		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);


		//get options 
		List<CustomerOptionItem> options = customerOptionService.listByStore(store, language);


		//get values
		List<CustomerOptionValueItem> optionsValues = customerOptionValueService.listByStore(store, language);


		CustomerOptionSetItem optionSet = new CustomerOptionSetItem();

		model.addAttribute("optionSet", optionSet);
		model.addAttribute("options", options);
		model.addAttribute("optionsValues", optionsValues);
		return ControllerConstants.Tiles.Customer.optionsSet;


	}


	@PreAuthorize("hasRole('CUSTOMER')")
	@RequestMapping(value = "/admin/customers/optionsset/save.html", method = RequestMethod.POST)
	public String saveOptionSet(@Valid @ModelAttribute("optionSet") CustomerOptionSetItem optionSet, BindingResult result, Model model, HttpServletRequest request, Locale locale) throws Exception {


		//display menu
		setMenu(model, request);

		LanguageItem language = languageService.toLanguage(locale);

		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);


		/** reference objects **/

		//get options 
		List<CustomerOptionItem> options = customerOptionService.listByStore(store, language);


		//get values
		List<CustomerOptionValueItem> optionsValues = customerOptionValueService.listByStore(store, language);


		model.addAttribute("options", options);
		model.addAttribute("optionsValues", optionsValues);

		if (optionSet.getCustomerOption() == null || optionSet.getCustomerOptionValue() == null) {
			model.addAttribute("errorMessageAssociation", messages.getMessage("message.optionset.noassociation", locale));
			ObjectError error = new ObjectError("customerOptionValue.id", messages.getMessage("message.optionset.noassociation", locale));
			result.addError(error);
			return ControllerConstants.Tiles.Customer.optionsSet;
		}

		//see if association already exist
		CustomerOptionItem option = null;

		//get from DB
		//option = customerOptionService.getById(optionSet.getPk().getCustomerOption().getUuid());
		option = customerOptionService.getById(optionSet.getCustomerOption().getUuid());

		if (option == null) {
			return "redirect:/admin/customers/optionsset/list.html";
		}

		//CustomerOptionValueItem optionValue = customerOptionValueService.getById(optionSet.getPk().getCustomerOptionValue().getUuid());
		CustomerOptionValueItem optionValue = customerOptionValueService.getById(optionSet.getCustomerOptionValue().getUuid());

		if (optionValue == null) {
			return "redirect:/admin/customers/optionsset/list.html";
		}


		List<CustomerOptionSetItem> optionsSet = customerOptionSetService.listByStore(store, language);

		if (optionsSet != null && optionsSet.size() > 0) {

			for (CustomerOptionSetItem optSet : optionsSet) {

				//CustomerOptionItem opt = optSet.getPk().getCustomerOption();
				CustomerOptionItem opt = optSet.getCustomerOption();
				//CustomerOptionValueItem optValue = optSet.getPk().getCustomerOptionValue();
				CustomerOptionValueItem optValue = optSet.getCustomerOptionValue();

				//if(opt.getUuid().longValue()==optionSet.getPk().getCustomerOption().getUuid().longValue()
				if (opt.getUuid().equals(optionSet.getCustomerOption().getUuid())
						//&& optValue.getUuid().longValue() == optionSet.getPk().getCustomerOptionValue().getUuid().longValue()) {
						&& optValue.getUuid().equals(optionSet.getCustomerOptionValue().getUuid())) {
					model.addAttribute("errorMessageAssociation", messages.getMessage("message.optionset.optionassociationexists", locale));
					ObjectError error = new ObjectError("customerOptionValue.id", messages.getMessage("message.optionset.optionassociationexists", locale));
					result.addError(error);
					break;
				}
			}
		}

		if (result.hasErrors()) {
			return ControllerConstants.Tiles.Customer.optionsSet;
		}


		//optionSet.getPk().setCustomerOption(option);
		optionSet.setCustomerOption(option);
		//optionSet.getPk().setCustomerOptionValue(optionValue);
		optionSet.setCustomerOptionValue(optionValue);
		customerOptionSetService.create(optionSet);


		model.addAttribute("success", "success");
		return ControllerConstants.Tiles.Customer.optionsSet;
	}


	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('CUSTOMER')")
	@RequestMapping(value = "/admin/customers/optionsset/paging.html", method = RequestMethod.POST)
	public @ResponseBody
	ResponseEntity<String> pageOptionsSet(HttpServletRequest request, HttpServletResponse response) {

		AjaxResponse resp = new AjaxResponse();


		try {


			LanguageItem language = (LanguageItem) request.getAttribute("LANGUAGE");
			MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);
			//List<CustomerOptionItem> options = null;

			List<CustomerOptionSetItem> optionSet = customerOptionSetService.listByStore(store, language);
			//for(CustomerOptionItem option : options) {


			//Set<CustomerOptionSetItem> optionSet = option.getCustomerOptions();

			if (optionSet != null && optionSet.size() > 0) {

				for (CustomerOptionSetItem optSet : optionSet) {

					//CustomerOptionItem customerOption = optSet.getPk().getCustomerOption();
					CustomerOptionItem customerOption = optSet.getCustomerOption();
					//CustomerOptionValueItem customerOptionValue = optSet.getPk().getCustomerOptionValue();
					CustomerOptionValueItem customerOptionValue = optSet.getCustomerOptionValue();

					@SuppressWarnings("rawtypes")
					Map entry = new HashMap();


					entry.put("id", optSet.getUuid());

					entry.put("optionCode", customerOption.getCode());
					entry.put("optionName", customerOption.getName());
					entry.put("optionValueCode", customerOptionValue.getCode());
					entry.put("optionValueName", customerOptionValue.getName());
					entry.put("order", customerOptionValue.getSortOrder());
					resp.addDataEntry(entry);

				}

			}


			//}

			resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);


		} catch (Exception e) {
			LOGGER.error("Error while paging options", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
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

	@PreAuthorize("hasRole('CUSTOMER')")
	@RequestMapping(value = "/admin/customers/optionsset/remove.html", method = RequestMethod.POST)
	public @ResponseBody
	ResponseEntity<String> deleteOptionSet(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		String sid = request.getParameter("id");

		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);
		AjaxResponse resp = new AjaxResponse();


		try {


			UUID optionSetId = UUID.fromString(sid);


			CustomerOptionSetItem entity = customerOptionSetService.getById(optionSetId);
			//if(entity==null || entity.getPk().getCustomerOption().getMerchantStore().getUuid().intValue()!=store.getUuid().intValue()) {
			if (entity == null || !entity.getCustomerOption().getMerchantStore().getUuid().equals(store.getUuid())) {

				resp.setStatusMessage(messages.getMessage("message.unauthorized", locale));
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);

			} else {

				customerOptionSetService.delete(entity);
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


	@PreAuthorize("hasRole('CUSTOMER')")
	@RequestMapping(value = "/admin/customers/optionsset/update.html", method = RequestMethod.POST)
	public @ResponseBody
	ResponseEntity<String> updateOrder(HttpServletRequest request, HttpServletResponse response) {
		String values = request.getParameter("_oldValues");
		String order = request.getParameter("order");

		AjaxResponse resp = new AjaxResponse();

		try {

			/**
			 * Values
			 */
			ObjectMapper mapper = new ObjectMapper();
			@SuppressWarnings("rawtypes")
			Map conf = mapper.readValue(values, Map.class);

			String sid = (String) conf.get("id");

			UUID optionId = UUID.fromString(sid);

			CustomerOptionSetItem entity = customerOptionSetService.getById(optionId);


			if (entity != null) {

				entity.setSortOrder(Integer.parseInt(order));
				customerOptionSetService.update(entity);
				resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);

			}


		} catch (Exception e) {
			LOGGER.error("Error while paging shipping countries", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}

		String returnString = resp.toJSONString();
		return new ResponseEntity<String>(returnString, HttpStatus.OK);
	}


}
