package com.coretex.shop.admin.controller.content;

import com.coretex.core.business.services.content.ContentService;
import com.coretex.core.business.services.reference.language.LanguageService;
import com.coretex.items.commerce_core_model.ContentItem;
import com.coretex.enums.commerce_core_model.ContentTypeEnum;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.shop.admin.controller.ControllerConstants;
import com.coretex.core.data.web.Menu;
import com.coretex.shop.constants.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.*;

@Controller
public class ContentBoxesController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ContentBoxesController.class);

	@Resource
	private ContentService contentService;

	@Resource
	LanguageService languageService;

	@ModelAttribute("boxPositions")
	public Set<Map.Entry<String, String>> boxPositions() {
		final Map<String, String> map = new HashMap<String, String>();

		map.put("LEFT", "LEFT");
		map.put("RIGHT", "RIGHT");


		return (map.entrySet());
	}


	@PreAuthorize("hasRole('CONTENT')")
	@RequestMapping(value = "/admin/content/boxes/list.html", method = RequestMethod.GET)
	public String listContentBoxes(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		setMenu(model, request);

		model.addAttribute("boxes", true);
		return ControllerConstants.Tiles.Content.contentPages;


	}

	@PreAuthorize("hasRole('CONTENT')")
	@RequestMapping(value = "/admin/content/boxes/create.html", method = RequestMethod.GET)
	public String createBox(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		model.addAttribute("boxes", true);
		setMenu(model, request);
		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);
		ContentItem content = new ContentItem();
		content.setMerchantStore(store);
		content.setContentType(ContentTypeEnum.BOX);

		//add positions
		List<String> positions = new ArrayList<String>();
		positions.add("LEFT");
		positions.add("RIGHT");

		model.addAttribute("positions", positions);
		model.addAttribute("content", content);


		return ControllerConstants.Tiles.Content.contentPagesDetails;


	}

	@PreAuthorize("hasRole('CONTENT')")
	@RequestMapping(value = "/admin/content/boxes/details.html", method = RequestMethod.GET)
	public String getContentDetails(@RequestParam("id") String id, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		model.addAttribute("boxes", true);
		setMenu(model, request);
		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);
		ContentItem content = contentService.getById(UUID.fromString(id));


		List<String> positions = new ArrayList<String>();
		positions.add("LEFT");
		positions.add("RIGHT");

		model.addAttribute("positions", positions);

		if (content == null) {
			LOGGER.error("ContentItem entity null for id " + id);
			return "redirect:/admin/content/boxes/listContent.html";
		}

		if (!content.getMerchantStore().getUuid().equals(store.getUuid())) {
			LOGGER.error("ContentItem id " + id + " does not belong to merchant " + store.getUuid());
			return "redirect:/admin/content/boxes/listContent.html";
		}

		if (!content.getContentType().name().equals(ContentTypeEnum.BOX.name())) {
			LOGGER.error("This controller does not handle content type " + content.getContentType().name());
			return "redirect:/admin/content/boxes/listContent.html";
		}


		model.addAttribute("content", content);


		return ControllerConstants.Tiles.Content.contentPagesDetails;


	}


	@PreAuthorize("hasRole('CONTENT')")
	@RequestMapping(value = "/admin/content/boxes/save.html", method = RequestMethod.POST)
	public String saveContent(@Valid @ModelAttribute ContentItem content, BindingResult result, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		model.addAttribute("boxes", true);
		setMenu(model, request);

		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);

		List<String> positions = new ArrayList<String>();
		positions.add("LEFT");
		positions.add("RIGHT");

		model.addAttribute("positions", positions);

		if (result.hasErrors()) {
			return ControllerConstants.Tiles.Content.contentPagesDetails;
		}


		content.setContentType(ContentTypeEnum.BOX);
		content.setMerchantStore(store);
		contentService.saveOrUpdate(content);


		model.addAttribute("content", content);
		model.addAttribute("success", "success");
		return ControllerConstants.Tiles.Content.contentPagesDetails;


	}


	private void setMenu(Model model, HttpServletRequest request) throws Exception {

		//display menu
		Map<String, String> activeMenus = new HashMap<String, String>();
		activeMenus.put("content", "content");
		activeMenus.put("content-boxes", "content-boxes");

		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>) request.getAttribute("MENUMAP");

		Menu currentMenu = menus.get("content");
		model.addAttribute("currentMenu", currentMenu);
		model.addAttribute("activeMenus", activeMenus);
		//

	}


}
