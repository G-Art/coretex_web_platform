package com.coretex.shop.admin.controller.products;

import com.coretex.items.commerce_core_model.ProductItem;
import com.coretex.items.commerce_core_model.ProductRelationshipItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.coretex.core.business.services.catalog.category.CategoryService;
import com.coretex.core.business.services.catalog.product.ProductService;
import com.coretex.core.business.services.catalog.product.relationship.ProductRelationshipService;
import com.coretex.core.business.utils.ajax.AjaxPageableResponse;
import com.coretex.core.business.utils.ajax.AjaxResponse;
import com.coretex.items.commerce_core_model.CategoryItem;
import com.coretex.shop.admin.controller.ControllerConstants;
import com.coretex.core.data.web.Menu;
import com.coretex.shop.constants.Constants;
import com.coretex.shop.utils.LabelUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;


@Controller
public class CustomProductGroupsController {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomProductGroupsController.class);

	@Resource
	CategoryService categoryService;

	@Resource
	ProductService productService;

	@Resource
	ProductRelationshipService productRelationshipService;

	@Resource
	LabelUtils messages;

	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value = "/admin/products/groups/list.html", method = RequestMethod.GET)
	public String displayProductGroups(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {


		setMenu(model, request);

		LanguageItem language = (LanguageItem) request.getAttribute("LANGUAGE");
		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);

		ProductRelationshipItem group = new ProductRelationshipItem();


		model.addAttribute("group", group);

		return ControllerConstants.Tiles.Product.customGroups;

	}


	@SuppressWarnings({"rawtypes", "unchecked"})
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value = "/admin/products/groups/paging.html", method = RequestMethod.POST)
	public @ResponseBody
	ResponseEntity<String> pageCustomGroups(HttpServletRequest request, HttpServletResponse response) {


		AjaxResponse resp = new AjaxResponse();

		try {

			MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);


			List<ProductRelationshipItem> relationships = productRelationshipService.getGroups(store);

			for (ProductRelationshipItem relationship : relationships) {

				if (!"FEATURED_ITEM".equals(relationship.getCode())) {//do not add featured items

					Map entry = new HashMap();
					entry.put("code", relationship.getCode());
					entry.put("active", relationship.getActive());

					resp.addDataEntry(entry);

				}

			}


			resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_SUCCESS);

		} catch (Exception e) {
			LOGGER.error("Error while paging products", e);
			resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}

		String returnString = resp.toJSONString();
		return new ResponseEntity<String>(returnString, HttpStatus.OK);


	}

	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value = "/admin/products/groups/save.html", method = RequestMethod.POST)
	public String saveCustomProductGroup(@ModelAttribute("group") ProductRelationshipItem group, BindingResult result, Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {

		setMenu(model, request);
		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);


		//check if group already exist


		if (StringUtils.isBlank(group.getCode())) {
			FieldError fieldError = new FieldError("group", "code", group.getCode(), false, null, null, messages.getMessage("message.group.required", locale));
			result.addError(fieldError);
			return ControllerConstants.Tiles.Product.customGroups;
		}

		//String msg = messages.getMessage("message.group.alerady.exists",locale);
		//String[] messages = {msg};

		String[] messages = {"message.group.alerady.exists"};

		List<ProductRelationshipItem> groups = productRelationshipService.getGroups(store);
		for (ProductRelationshipItem grp : groups) {
			if (grp.getCode().equalsIgnoreCase(group.getCode())) {
				String[] args = {group.getCode()};
				FieldError fieldError = new FieldError("group", "code", group.getCode(), false, messages, args, null);
				result.addError(fieldError);
			}
		}

		if (result.hasErrors()) {
			return ControllerConstants.Tiles.Product.customGroups;
		}

		group.setActive(true);
		group.setStore(store);

		productRelationshipService.addGroup(store, group.getCode());


		model.addAttribute("success", "success");

		return ControllerConstants.Tiles.Product.customGroups;

	}

	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value = "/admin/products/groups/remove.html", method = RequestMethod.POST)
	public @ResponseBody
	ResponseEntity<String> removeCustomProductGroup(HttpServletRequest request, HttpServletResponse response) {

		String groupCode = request.getParameter("code");

		AjaxResponse resp = new AjaxResponse();


		try {
			MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);
			productRelationshipService.deleteGroup(store, groupCode);
			resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);

		} catch (Exception e) {
			LOGGER.error("Error while deleting a group", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}

		String returnString = resp.toJSONString();

		return new ResponseEntity<String>(returnString, HttpStatus.OK);

	}

	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value = "/admin/products/groups/update.html", method = RequestMethod.POST)
	public @ResponseBody
	ResponseEntity<String> activateProductGroup(HttpServletRequest request, HttpServletResponse response) {
		String values = request.getParameter("_oldValues");
		String active = request.getParameter("active");


		AjaxResponse resp = new AjaxResponse();

		try {

			ObjectMapper mapper = new ObjectMapper();
			@SuppressWarnings("rawtypes")
			Map conf = mapper.readValue(values, Map.class);
			String groupCode = (String) conf.get("code");

			MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);

			//get groups
			List<ProductRelationshipItem> groups = productRelationshipService.getGroups(store);

			for (ProductRelationshipItem relation : groups) {
				if (relation.getCode().equals(groupCode)) {
					if ("true".equals(active)) {
						relation.setActive(true);
					} else {
						relation.setActive(false);
					}
					productRelationshipService.saveOrUpdate(relation);
				}
			}
			resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);

		} catch (Exception e) {
			LOGGER.error("Error while updateing groups", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}

		String returnString = resp.toJSONString();

		return new ResponseEntity<String>(returnString, HttpStatus.OK);
	}

	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value = "/admin/products/group/edit.html", method = RequestMethod.GET)
	public String displayCustomProductGroup(@RequestParam("id") String groupCode, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {


		setMenu(model, request);

		LanguageItem language = (LanguageItem) request.getAttribute("LANGUAGE");
		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);

		List<CategoryItem> categories = categoryService.listByStore(store, language);//for categories


		model.addAttribute("group", groupCode);
		model.addAttribute("categories", categories);
		return ControllerConstants.Tiles.Product.customGroupsDetails;

	}


	@SuppressWarnings({"rawtypes", "unchecked"})
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value = "/admin/products/group/details/paging.html", method = RequestMethod.POST)
	public @ResponseBody
	ResponseEntity<String> pageProducts(HttpServletRequest request, HttpServletResponse response) {

		String code = request.getParameter("code");
		AjaxResponse resp = new AjaxResponse();

		try {


			LanguageItem language = (LanguageItem) request.getAttribute("LANGUAGE");
			MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);


			List<ProductRelationshipItem> relationships = productRelationshipService.getByGroup(store, code, language);

			for (ProductRelationshipItem relationship : relationships) {

				ProductItem product = relationship.getRelatedProduct();
				Map entry = new HashMap();
				entry.put("relationshipId", relationship.getUuid());
				entry.put("productId", product.getUuid());


				entry.put("name", product.getName());
				entry.put("sku", product.getSku());
				entry.put("available", product.getAvailable());
				resp.addDataEntry(entry);

			}


			resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_SUCCESS);

		} catch (Exception e) {
			LOGGER.error("Error while paging products", e);
			resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}

		String returnString = resp.toJSONString();

		return new ResponseEntity<String>(returnString, HttpStatus.OK);


	}


	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value = "/admin/products/group/details/addItem.html", method = RequestMethod.POST)
	public @ResponseBody
	ResponseEntity<String> addItem(HttpServletRequest request, HttpServletResponse response) {

		String code = request.getParameter("code");
		String productId = request.getParameter("productId");
		AjaxResponse resp = new AjaxResponse();



		try {


			UUID lProductId = UUID.fromString(productId);

			MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);

			ProductItem product = productService.getById(lProductId);

			if (product == null) {
				resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
				String returnString = resp.toJSONString();
				return new ResponseEntity<>(returnString, HttpStatus.OK);
			}

			if (!product.getMerchantStore().getUuid().equals(store.getUuid())) {
				resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
				String returnString = resp.toJSONString();
				return new ResponseEntity<>(returnString, HttpStatus.OK);
			}


			ProductRelationshipItem relationship = new ProductRelationshipItem();
			relationship.setActive(true);
			relationship.setCode(code);
			relationship.setStore(store);
			relationship.setRelatedProduct(product);

			productRelationshipService.saveOrUpdate(relationship);


			resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_SUCCESS);

		} catch (Exception e) {
			LOGGER.error("Error while paging products", e);
			resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}

		String returnString = resp.toJSONString();
		return new ResponseEntity<String>(returnString, HttpStatus.OK);

	}

	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value = "/admin/products/group/details/removeItem.html", method = RequestMethod.POST)
	public @ResponseBody
	ResponseEntity<String> removeItem(HttpServletRequest request, HttpServletResponse response) {

		String code = request.getParameter("code");
		String productId = request.getParameter("productId");
		AjaxResponse resp = new AjaxResponse();



		try {


			UUID lproductId = UUID.fromString(productId);

			MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);

			ProductItem product = productService.getById(lproductId);

			if (product == null) {
				resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
				String returnString = resp.toJSONString();
				return new ResponseEntity<>(returnString, HttpStatus.OK);
			}

			if (!product.getMerchantStore().getUuid().equals(store.getUuid())) {
				resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
				String returnString = resp.toJSONString();
				return new ResponseEntity<>(returnString, HttpStatus.OK);
			}


			ProductRelationshipItem relationship = null;
			List<ProductRelationshipItem> relationships = productRelationshipService.getByGroup(store, code);

			for (ProductRelationshipItem r : relationships) {
				if (r.getRelatedProduct().getUuid().equals(lproductId)) {
					relationship = r;
					break;
				}
			}

			if (relationship == null) {
				resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
				String returnString = resp.toJSONString();
				return new ResponseEntity<>(returnString, HttpStatus.OK);
			}

			if (!relationship.getStore().getUuid().equals(store.getUuid())) {
				resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
				String returnString = resp.toJSONString();
				return new ResponseEntity<>(returnString, HttpStatus.OK);
			}


			productRelationshipService.delete(relationship);


			resp.setStatus(AjaxPageableResponse.RESPONSE_OPERATION_COMPLETED);

		} catch (Exception e) {
			LOGGER.error("Error while paging products", e);
			resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}

		String returnString = resp.toJSONString();
		return new ResponseEntity<String>(returnString, HttpStatus.OK);

	}

	private void setMenu(Model model, HttpServletRequest request) throws Exception {

		//display menu
		Map<String, String> activeMenus = new HashMap<String, String>();
		activeMenus.put("catalogue", "catalogue");
		activeMenus.put("catalogue-products-group", "catalogue-products-group");

		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>) request.getAttribute("MENUMAP");

		Menu currentMenu = menus.get("catalogue");
		model.addAttribute("currentMenu", currentMenu);
		model.addAttribute("activeMenus", activeMenus);
		//

	}

}
