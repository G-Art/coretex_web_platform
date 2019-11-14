package com.coretex.shop.admin.controller.products;

import com.coretex.core.business.services.catalog.product.attribute.ProductOptionValueService;
import com.coretex.core.business.services.content.ContentService;
import com.coretex.core.business.services.reference.language.LanguageService;
import com.coretex.core.business.utils.ajax.AjaxResponse;
import com.coretex.items.commerce_core_model.ProductOptionValueItem;
import com.coretex.core.model.content.FileContentType;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.core.data.web.Menu;
import com.coretex.shop.constants.Constants;
import com.coretex.shop.utils.ImageFilePath;
import com.coretex.shop.utils.LabelUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
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
public class OptionsValueController {

	@Resource
	LanguageService languageService;


	@Resource
	ProductOptionValueService productOptionValueService;

	@Resource
	LabelUtils messages;

	@Resource
	private ContentService contentService;

	@Resource
	@Qualifier("img")
	private ImageFilePath imageUtils;

	private static final Logger LOGGER = LoggerFactory.getLogger(OptionsValueController.class);


	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value = "/admin/options/optionvalues.html", method = RequestMethod.GET)
	public String displayOptions(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {


		setMenu(model, request);

		//subsequent ajax call


		return "catalogue-optionsvalues-list";


	}

	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value = "/admin/options/editOptionValue.html", method = RequestMethod.GET)
	public String displayOptionEdit(@RequestParam("id") String optionId, HttpServletRequest request, HttpServletResponse response, Model model, Locale locale) throws Exception {
		return displayOption(UUID.fromString(optionId), request, response, model, locale);
	}

	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value = "/admin/options/createOptionValue.html", method = RequestMethod.GET)
	public String displayOption(HttpServletRequest request, HttpServletResponse response, Model model, Locale locale) throws Exception {
		return displayOption(null, request, response, model, locale);
	}

	private String displayOption(UUID optionId, HttpServletRequest request, HttpServletResponse response, Model model, Locale locale) throws Exception {


		this.setMenu(model, request);
		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);

		ProductOptionValueItem option = new ProductOptionValueItem();

		if (optionId != null) {//edit mode


			option = productOptionValueService.getById(store, optionId);

			if (option == null) {
				return "redirect:/admin/options/optionvalues.html";
			}


		}

		model.addAttribute("optionValue", option);
		return "catalogue-optionsvalues-details";


	}


	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value = "/admin/options/saveOptionValue.html", method = RequestMethod.POST)
	public String saveOption(@Valid @ModelAttribute("optionValue") ProductOptionValueItem optionValue, BindingResult result, Model model, HttpServletRequest request, Locale locale) throws Exception {


		//display menu
		setMenu(model, request);

		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);
		ProductOptionValueItem dbEntity = null;

		if (optionValue.getUuid() != null) { //edit entry

			//get from DB
			dbEntity = productOptionValueService.getById(store, optionValue.getUuid());

			if (dbEntity == null) {
				return "redirect:/admin/options/optionsvalues.html";
			}


		} else {

			//validate if it contains an existing code
			ProductOptionValueItem byCode = productOptionValueService.getByCode(store, optionValue.getCode());
			if (byCode != null) {
				ObjectError error = new ObjectError("code", messages.getMessage("message.code.exist", locale));
				result.addError(error);
			}

		}


		optionValue.setMerchantStore(store);


		if (result.hasErrors()) {
			return "catalogue-optionsvalues-details";
		}


		productOptionValueService.saveOrUpdate(optionValue);


		model.addAttribute("success", "success");
		return "catalogue-optionsvalues-details";
	}


	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value = "/admin/optionsvalues/paging.html", method = RequestMethod.POST)
	public @ResponseBody
	ResponseEntity<String> pageOptions(HttpServletRequest request, HttpServletResponse response) {

		String optionName = request.getParameter("name");


		AjaxResponse resp = new AjaxResponse();


		try {


			LanguageItem language = (LanguageItem) request.getAttribute("LANGUAGE");

			MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);

			List<ProductOptionValueItem> options = null;

			if (!StringUtils.isBlank(optionName)) {

				//productOptionValueService.getByName(store, optionName, language);

			} else {

				options = productOptionValueService.listByStore(store, language);

			}


			for (ProductOptionValueItem option : options) {

				@SuppressWarnings("rawtypes")
				Map entry = new HashMap();
				entry.put("optionValueId", option.getUuid());

				entry.put("name", option.getName());
				//entry.put("image", new StringBuilder().append(store.getCode()).append("/").append(FileContentType.PROPERTY.name()).append("/").append(option.getProductOptionValueImage()).toString());
				entry.put("image", imageUtils.buildProductPropertyImageUtils(store, option.getProductOptionValueImage()));
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

	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value = "/admin/optionsvalues/remove.html", method = RequestMethod.POST)
	public @ResponseBody
	ResponseEntity<String> deleteOptionValue(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		String sid = request.getParameter("optionValueId");

		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);

		AjaxResponse resp = new AjaxResponse();


		try {

			UUID id = UUID.fromString(sid);

			ProductOptionValueItem entity = productOptionValueService.getById(store, id);

			if (entity == null || !entity.getMerchantStore().getUuid().equals(store.getUuid())) {

				resp.setStatusMessage(messages.getMessage("message.unauthorized", locale));
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);

			} else {

				productOptionValueService.delete(entity);
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

	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value = "/admin/optionsvalues/removeImage.html", method = RequestMethod.POST)
	public @ResponseBody
	ResponseEntity<String> removeImage(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		String optionValueId = request.getParameter("optionId");

		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);

		AjaxResponse resp = new AjaxResponse();


		try {

			UUID id = UUID.fromString(optionValueId);

			ProductOptionValueItem optionValue = productOptionValueService.getById(store, id);

			contentService.removeFile(store.getCode(), FileContentType.PROPERTY, optionValue.getProductOptionValueImage());

			store.setStoreLogo(null);
			optionValue.setProductOptionValueImage(null);
			productOptionValueService.update(optionValue);


		} catch (Exception e) {
			LOGGER.error("Error while deleting product", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}

		String returnString = resp.toJSONString();

		return new ResponseEntity<String>(returnString, HttpStatus.OK);
	}


	private void setMenu(Model model, HttpServletRequest request) throws Exception {

		//display menu
		Map<String, String> activeMenus = new HashMap<String, String>();
		activeMenus.put("catalogue", "catalogue");
		activeMenus.put("catalogue-options", "catalogue-options");

		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>) request.getAttribute("MENUMAP");

		Menu currentMenu = menus.get("catalogue");
		model.addAttribute("currentMenu", currentMenu);
		model.addAttribute("activeMenus", activeMenus);
		//

	}

}
