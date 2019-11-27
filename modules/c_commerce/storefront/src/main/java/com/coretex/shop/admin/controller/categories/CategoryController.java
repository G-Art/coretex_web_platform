package com.coretex.shop.admin.controller.categories;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.coretex.items.commerce_core_model.CategoryItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.core.LocaleItem;
import com.coretex.shop.admin.controller.AbstractController;
import com.coretex.shop.admin.forms.CategoryForm;
import com.coretex.shop.admin.mapppers.CategoryFormMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.coretex.core.business.services.catalog.category.CategoryService;
import com.coretex.core.business.utils.ajax.AjaxResponse;
import com.coretex.core.data.web.Menu;
import com.coretex.shop.constants.Constants;
import com.coretex.shop.utils.LabelUtils;


@Controller
public class CategoryController extends AbstractController {

	private static final Logger LOGGER = LoggerFactory.getLogger(CategoryController.class);

	@Resource
	private CategoryFormMapper categoryFormMapper;

	@Resource
	private CategoryService categoryService;

	@Resource
	private LabelUtils messages;

	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value = "/admin/categories/editCategory.html", method = RequestMethod.GET)
	public String displayCategoryEdit(@RequestParam("id") String categoryId, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return displayCategory(UUID.fromString(categoryId), model, request, response);
	}

	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value = "/admin/categories/createCategory.html", method = RequestMethod.GET)
	public String displayCategoryCreate(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return displayCategory(null, model, request, response);

	}

	private String displayCategory(UUID categoryId, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		//display menu
		setMenu(model, request);

		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);
		LocaleItem language = (LocaleItem) request.getAttribute("LANGUAGE");

		//get parent categories
		List<CategoryItem> categories = categoryService.listByStore(store, language);

		CategoryItem category = new CategoryItem();

		if (categoryId != null) {//edit mode
			category = categoryService.getByUUID(categoryId);


			if (category == null || !category.getMerchantStore().getUuid().equals(store.getUuid())) {
				return "catalogue-categories";
			}
		} else {

			category.setVisible(true);

		}

		model.addAttribute("category", categoryFormMapper.fromCategoryItem(category));
		model.addAttribute("categories", categories);

		return "catalogue-categories-category";
	}

	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value = "/admin/categories/save.html", method = RequestMethod.POST)
	public String saveCategory(@Valid @ModelAttribute("category") CategoryForm categoryForm, BindingResult result, Model model, HttpServletRequest request) throws Exception {

		LocaleItem language = (LocaleItem) request.getAttribute("LANGUAGE");

		//display menu
		setMenu(model, request);

		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);

		var category = categoryFormMapper.toCategoryItem(categoryForm);

		if (category.getUuid() != null) { //edit entry

			//get from DB
			CategoryItem currentCategory = categoryService.getByUUID(category.getUuid());

			if (currentCategory == null || !currentCategory.getMerchantStore().getUuid().equals(store.getUuid())) {
				return "catalogue-categories";
			}

		}

		//save to DB
		category.setMerchantStore(store);
		//}

		if (result.hasErrors()) {
			return "catalogue-categories-category";
		}

		categoryService.saveOrUpdate(category);


		//ajust lineage and depth
		if (category.getParent() != null) {

			CategoryItem parent = new CategoryItem();
			parent.setUuid(category.getParent().getUuid());
			parent.setMerchantStore(store);

			categoryService.addChild(parent, category);

		}


		//get parent categories
		List<CategoryItem> categories = categoryService.listByStore(store, language);
		model.addAttribute("categories", categories);


		model.addAttribute("success", "success");
		return "catalogue-categories-category";
	}


	//category list
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value = "/admin/categories/categories.html", method = RequestMethod.GET)
	public String displayCategories(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {


		setMenu(model, request);

		//does nothing, ajax subsequent request

		return "catalogue-categories";
	}

	@SuppressWarnings({"unchecked"})
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value = "/admin/categories/paging.html", method = RequestMethod.POST)
	public @ResponseBody
	ResponseEntity<String> pageCategories(HttpServletRequest request) {
		String categoryName = request.getParameter("name");
		String categoryCode = request.getParameter("code");


		AjaxResponse resp = new AjaxResponse();


		try {

			LocaleItem language = (LocaleItem) request.getAttribute("LANGUAGE");


			MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);

			List<CategoryItem> categories = null;

			if (!StringUtils.isBlank(categoryName)) {


				categories = categoryService.getByName(store, categoryName, language);

			} else if (!StringUtils.isBlank(categoryCode)) {

				categoryService.listByCodes(store, new ArrayList<>(Arrays.asList(categoryCode)), language);

			} else {

				categories = categoryService.listByStore(store, language);

			}


			for (CategoryItem category : categories) {

				@SuppressWarnings("rawtypes")
				Map entry = new HashMap();
				entry.put("categoryId", category.getUuid().toString());

				entry.put("name", category.getName());
				entry.put("code", category.getCode());
				entry.put("visible", category.getVisible());
				resp.addDataEntry(entry);


			}

			resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);


		} catch (Exception e) {
			LOGGER.error("Error while paging categories", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}

		String returnString = resp.toJSONString();

		return new ResponseEntity<>(returnString, HttpStatus.OK);
	}

	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value = "/admin/categories/hierarchy.html", method = RequestMethod.GET)
	public String displayCategoryHierarchy(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {


		setMenu(model, request);

		//get the list of categories
		LocaleItem language = (LocaleItem) request.getAttribute("LANGUAGE");
		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);

		List<CategoryItem> categories = categoryService.listByStore(store, language);

		model.addAttribute("categories", categories);

		return "catalogue-categories-hierarchy";
	}

	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value = "/admin/categories/remove.html", method = RequestMethod.POST)
	public @ResponseBody
	ResponseEntity<String> deleteCategory(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		String sid = request.getParameter("categoryId");

		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);

		AjaxResponse resp = new AjaxResponse();


		try {

			UUID id = UUID.fromString(sid);

			CategoryItem category = categoryService.getByUUID(id);

			if (category == null || !category.getMerchantStore().getUuid().equals(store.getUuid())) {

				resp.setStatusMessage(messages.getMessage("message.unauthorized", locale));
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);

			} else {

				categoryService.delete(category);
				resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);

			}


		} catch (Exception e) {
			LOGGER.error("Error while deleting category", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}

		String returnString = resp.toJSONString();
		return new ResponseEntity<>(returnString, HttpStatus.OK);
	}

	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value = "/admin/categories/moveCategory.html", method = RequestMethod.POST)
	public @ResponseBody
	ResponseEntity<String> moveCategory(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		String parentid = request.getParameter("parentId");
		String childid = request.getParameter("childId");

		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);

		AjaxResponse resp = new AjaxResponse();
		try {

			UUID parentId = UUID.fromString(parentid);
			UUID childId = UUID.fromString(childid);

			CategoryItem child = categoryService.getByUUID(childId);
			CategoryItem parent = categoryService.getByUUID(parentId);

			if (Objects.nonNull(child) && Objects.nonNull(child.getParent()) && child.getParent().getUuid().equals(parentId)) {
				resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);
			}

			if (child == null || parent == null || child.getMerchantStore().getUuid() != store.getUuid() || parent.getMerchantStore().getUuid() != store.getUuid()) {
				resp.setStatusMessage(messages.getMessage("message.unauthorized", locale));

				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
				String returnString = resp.toJSONString();
				return new ResponseEntity<>(returnString, HttpStatus.OK);
			}

			if (child.getMerchantStore().getUuid() != store.getUuid() || parent.getMerchantStore().getUuid() != store.getUuid()) {
				resp.setStatusMessage(messages.getMessage("message.unauthorized", locale));
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
				String returnString = resp.toJSONString();
				return new ResponseEntity<>(returnString, HttpStatus.OK);
			}

			categoryService.addChild(parent, child);
			resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);

		} catch (Exception e) {
			LOGGER.error("Error while moving category", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}

		String returnString = resp.toJSONString();

		return new ResponseEntity<>(returnString, HttpStatus.OK);
	}

	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value = "/admin/categories/checkCategoryCode.html", method = RequestMethod.POST)
	public @ResponseBody
	ResponseEntity<String> checkCategoryCode(HttpServletRequest request) {
		String code = request.getParameter("code");
		String id = request.getParameter("id");


		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);


		AjaxResponse resp = new AjaxResponse();

		if (StringUtils.isBlank(code)) {
			resp.setStatus(AjaxResponse.CODE_ALREADY_EXIST);
			String returnString = resp.toJSONString();
			return new ResponseEntity<>(returnString, HttpStatus.OK);
		}


		try {

			CategoryItem category = categoryService.getByCode(store, code);

			if (category != null && StringUtils.isBlank(id)) {
				resp.setStatus(AjaxResponse.CODE_ALREADY_EXIST);
				String returnString = resp.toJSONString();
				return new ResponseEntity<>(returnString, HttpStatus.OK);
			}


			if (category != null && !StringUtils.isBlank(id)) {
				try {
					UUID lid = UUID.fromString(id);

					if (category.getCode().equals(code) && category.getUuid().equals(lid)) {
						resp.setStatus(AjaxResponse.CODE_ALREADY_EXIST);
						String returnString = resp.toJSONString();
						return new ResponseEntity<>(returnString, HttpStatus.OK);
					}
				} catch (Exception e) {
					resp.setStatus(AjaxResponse.CODE_ALREADY_EXIST);
					String returnString = resp.toJSONString();
					return new ResponseEntity<>(returnString, HttpStatus.OK);
				}

			}


			resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);

		} catch (Exception e) {
			LOGGER.error("Error while getting category", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}

		String returnString = resp.toJSONString();

		return new ResponseEntity<>(returnString, HttpStatus.OK);
	}

	private void setMenu(Model model, HttpServletRequest request) {

		//display menu
		Map<String, String> activeMenus = new HashMap<String, String>();
		activeMenus.put("catalogue", "catalogue");
		activeMenus.put("catalogue-categories", "catalogue-categories");

		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>) request.getAttribute("MENUMAP");

		Menu currentMenu = menus.get("catalogue");
		model.addAttribute("currentMenu", currentMenu);
		model.addAttribute("activeMenus", activeMenus);
		//

	}

}
