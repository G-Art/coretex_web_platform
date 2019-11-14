package com.coretex.shop.admin.controller.content;

import com.coretex.core.business.services.catalog.product.relationship.ProductRelationshipService;
import com.coretex.core.business.services.content.ContentService;
import com.coretex.core.business.services.reference.language.LanguageService;
import com.coretex.core.business.utils.ajax.AjaxResponse;
import com.coretex.enums.commerce_core_model.ContentTypeEnum;
import com.coretex.items.commerce_core_model.ProductRelationshipItem;
import com.coretex.items.commerce_core_model.ContentItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.shop.admin.controller.ControllerConstants;
import com.coretex.core.data.web.Menu;
import com.coretex.shop.constants.Constants;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.*;

@Controller
public class ContentPagesController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ContentPagesController.class);

	@Resource
	private ContentService contentService;

	@Resource
	LanguageService languageService;

	@Resource
	ProductRelationshipService productRelationshipService;


	@PreAuthorize("hasRole('CONTENT')")
	@RequestMapping(value = "/admin/content/pages/list.html", method = RequestMethod.GET)
	public String listContentPages(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		setMenu(model, request);

		return ControllerConstants.Tiles.Content.contentPages;


	}

	@PreAuthorize("hasRole('CONTENT')")
	@RequestMapping(value = "/admin/content/pages/create.html", method = RequestMethod.GET)
	public String createPage(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		setMenu(model, request);
		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);
		ContentItem content = new ContentItem();
		content.setMerchantStore(store);
		content.setContentType(ContentTypeEnum.PAGE);


		List<ProductRelationshipItem> relationships = productRelationshipService.getGroups(store);
		if (!CollectionUtils.isEmpty(relationships)) {
			model.addAttribute("productGroups", relationships);
		}


		model.addAttribute("content", content);


		return ControllerConstants.Tiles.Content.contentPagesDetails;


	}

	@PreAuthorize("hasRole('CONTENT')")
	@RequestMapping(value = "/admin/content/pages/details.html", method = RequestMethod.GET)
	public String getContentDetails(@RequestParam("id") String id, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		setMenu(model, request);
		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);
		ContentItem content = contentService.getById(UUID.fromString(id));


		if (content == null) {
			LOGGER.error("ContentItem entity null for id " + id);
			return "redirect:/admin/content/pages/listContent.html";
		}

		if (!content.getMerchantStore().getUuid().equals(store.getUuid())) {
			LOGGER.error("ContentItem id " + id + " does not belong to merchant " + store.getUuid());
			return "redirect:/admin/content/pages/listContent.html";
		}

		if (!content.getContentType().name().equals(ContentTypeEnum.PAGE.name())) {
			LOGGER.error("This controller does not handle content type " + content.getContentType().name());
			return "redirect:/admin/content/pages/listContent.html";
		}

		model.addAttribute("content", content);

		List<ProductRelationshipItem> relationships = productRelationshipService.getGroups(store);
		if (!CollectionUtils.isEmpty(relationships)) {
			model.addAttribute("productGroups", relationships);
		}

		return ControllerConstants.Tiles.Content.contentPagesDetails;


	}


	@PreAuthorize("hasRole('CONTENT')")
	@RequestMapping(value = "/admin/content/remove.html", method = RequestMethod.POST)
	public @ResponseBody
	ResponseEntity<String> removeContent(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		String id = request.getParameter("id");

		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);

		AjaxResponse resp = new AjaxResponse();


		try {

			//get the content first
			UUID lid = UUID.fromString(id);

			ContentItem dbContent = contentService.getById(lid);

			if (dbContent == null) {
				LOGGER.error("Invalid content id ", id);
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
				String returnString = resp.toJSONString();
				return new ResponseEntity<String>(returnString, HttpStatus.OK);
			}

			if (dbContent != null && !dbContent.getMerchantStore().getUuid().equals(store.getUuid())) {
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
				String returnString = resp.toJSONString();
				return new ResponseEntity<String>(returnString, HttpStatus.OK);
			}

			contentService.delete(dbContent);

			resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);

		} catch (Exception e) {
			LOGGER.error("Error while deleting product", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}

		String returnString = resp.toJSONString();
		return new ResponseEntity<String>(returnString, HttpStatus.OK);
	}


	@SuppressWarnings({"unchecked"})
	@PreAuthorize("hasRole('CONTENT')")
	@RequestMapping(value = "/admin/content/page.html", method = RequestMethod.POST)
	public @ResponseBody
	ResponseEntity<String> pageStaticContent(@RequestParam("contentType") String contentType, HttpServletRequest request, HttpServletResponse response) {
		AjaxResponse resp = new AjaxResponse();

		try {


			MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);

			LanguageItem language = (LanguageItem) request.getAttribute("LANGUAGE");


			ContentTypeEnum cType = ContentTypeEnum.PAGE;
			if (ContentTypeEnum.BOX.name().equals(contentType)) {
				cType = ContentTypeEnum.BOX;
			}
			List<ContentItem> contentList = contentService.listByType(cType, store, language);

			if (contentList != null) {

				for (ContentItem content : contentList) {


					@SuppressWarnings("rawtypes")
					Map entry = new HashMap();
					entry.put("id", content.getUuid());
					entry.put("code", content.getCode());
					entry.put("name", content.getName());
					resp.addDataEntry(entry);

				}

			}

			resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);

		} catch (Exception e) {
			LOGGER.error("Error while paging content", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}

		String returnString = resp.toJSONString();
		return new ResponseEntity<String>(returnString, HttpStatus.OK);
	}


	@PreAuthorize("hasRole('CONTENT')")
	@RequestMapping(value = "/admin/content/pages/save.html", method = RequestMethod.POST)
	public String saveContent(@Valid @ModelAttribute ContentItem content, BindingResult result, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		setMenu(model, request);

		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);

		if (result.hasErrors()) {
			return ControllerConstants.Tiles.Content.contentPagesDetails;
		}

		Map<String, LanguageItem> langs = languageService.getLanguagesMap();

		if (content.getSortOrder() == null) {
			content.setSortOrder(0);
		}

		content.setContentType(ContentTypeEnum.PAGE);
		content.setMerchantStore(store);

		contentService.saveOrUpdate(content);

		List<ProductRelationshipItem> relationships = productRelationshipService.getGroups(store);
		if (!CollectionUtils.isEmpty(relationships)) {
			model.addAttribute("productGroups", relationships);
		}


		model.addAttribute("content", content);
		model.addAttribute("success", "success");
		return ControllerConstants.Tiles.Content.contentPagesDetails;


	}

	/**
	 * Check if the content code filled in by the
	 * user is unique
	 *
	 * @param request
	 * @param response
	 * @param locale
	 * @return
	 */
	@PreAuthorize("hasRole('CONTENT')")
	@RequestMapping(value = "/admin/content/checkContentCode.html", method = RequestMethod.POST)
	public @ResponseBody
	ResponseEntity<String> checkContentCode(HttpServletRequest request, HttpServletResponse response, Locale locale) {

		String code = request.getParameter("code");
		String id = request.getParameter("id");

		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);

		AjaxResponse resp = new AjaxResponse();

		if (StringUtils.isBlank(code)) {
			resp.setStatus(AjaxResponse.CODE_ALREADY_EXIST);
			String returnString = resp.toJSONString();
			return new ResponseEntity<String>(returnString, HttpStatus.OK);
		}

		try {

			ContentItem content = contentService.getByCode(code, store);


			if (!StringUtils.isBlank(id)) {
				try {
					UUID lid = UUID.fromString(id);

					if (content != null && content.getCode().equals(code) && content.getUuid().equals(lid)) {
						resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
						String returnString = resp.toJSONString();
						return new ResponseEntity<String>(returnString, HttpStatus.OK);
					}
				} catch (Exception e) {
					resp.setStatus(AjaxResponse.CODE_ALREADY_EXIST);
					String returnString = resp.toJSONString();
					return new ResponseEntity<String>(returnString, HttpStatus.OK);
				}

			} else {
				if (content != null) {
					resp.setStatus(AjaxResponse.CODE_ALREADY_EXIST);
					String returnString = resp.toJSONString();
					return new ResponseEntity<String>(returnString, HttpStatus.OK);
				}
			}


			resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);

		} catch (Exception e) {
			LOGGER.error("Error while getting category", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}

		String returnString = resp.toJSONString();
		return new ResponseEntity<String>(returnString, HttpStatus.OK);
	}


	private void setMenu(Model model, HttpServletRequest request) throws Exception {

		//display menu
		Map<String, String> activeMenus = new HashMap<String, String>();
		activeMenus.put("content", "content");
		activeMenus.put("content-pages", "content-pages");

		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>) request.getAttribute("MENUMAP");

		Menu currentMenu = menus.get("content");
		model.addAttribute("currentMenu", currentMenu);
		model.addAttribute("activeMenus", activeMenus);
		//

	}


}
