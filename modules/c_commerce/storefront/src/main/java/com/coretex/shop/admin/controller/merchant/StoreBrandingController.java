package com.coretex.shop.admin.controller.merchant;


import com.coretex.core.business.services.merchant.MerchantStoreService;
import com.coretex.core.business.utils.ajax.AjaxResponse;
import com.coretex.core.data.web.Menu;
import com.coretex.core.model.content.InputContentFile;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.shop.constants.Constants;
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
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Controller
public class StoreBrandingController {

	private static final Logger LOGGER = LoggerFactory.getLogger(StoreBrandingController.class);


	@Resource
	MerchantStoreService merchantStoreService;


	@PreAuthorize("hasRole('STORE')")
	@RequestMapping(value = "/admin/store/storeBranding.html", method = RequestMethod.GET)
	public String displayStoreBranding(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		setMenu(model, request);

		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);


		model.addAttribute("store", store);


		return "admin-store-branding";
	}

	/**
	 * https://spring.io/guides/gs/uploading-files/
	 *
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasRole('STORE')")
	@RequestMapping(value = "/admin/store/saveBranding.html", method = RequestMethod.POST)
	public String saveStoreBranding(@RequestParam("file") MultipartFile file, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		setMenu(model, request);

		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);

		model.addAttribute("store", store);

		if (file != null) {

			String imageName = file.getOriginalFilename();
			InputStream inputStream = file.getInputStream();
			String mimeType = file.getContentType();

			InputContentFile cmsContentImage = new InputContentFile();
			cmsContentImage.setFileName(imageName);
			cmsContentImage.setMimeType(mimeType);
			cmsContentImage.setFile(inputStream);

			//Update store
			store.setStoreLogo(imageName);
			merchantStoreService.update(store);
			request.getSession().setAttribute(Constants.ADMIN_STORE, store);

		} else {
			model.addAttribute("error", "error");
			return "admin-store-branding";
		}

		model.addAttribute("success", "success");
		return "admin-store-branding";
	}

	@PreAuthorize("hasRole('STORE')")
	@RequestMapping(value = "/admin/store/saveTemplate.html", method = RequestMethod.POST)
	public String saveTemplate(@ModelAttribute(value = "store") final MerchantStoreItem store, BindingResult result, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		setMenu(model, request);

		MerchantStoreItem sessionstore = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);

		sessionstore.setStoreTemplate(store.getStoreTemplate());

		merchantStoreService.saveOrUpdate(sessionstore);

		request.setAttribute(Constants.ADMIN_STORE, sessionstore);

		model.addAttribute("success", "success");
		model.addAttribute("store", sessionstore);

		return "admin-store-branding";
	}

	@PreAuthorize("hasRole('STORE')")
	@RequestMapping(value = "/admin/store/removeImage.html", method = RequestMethod.POST)
	public @ResponseBody
	ResponseEntity<String> removeImage(HttpServletRequest request, HttpServletResponse response, Locale locale) {

		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);

		AjaxResponse resp = new AjaxResponse();

		try {

			store.setStoreLogo(null);
			merchantStoreService.update(store);


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
		activeMenus.put("store", "store");
		activeMenus.put("storeBranding", "storeBranding");


		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>) request.getAttribute("MENUMAP");

		Menu currentMenu = menus.get("store");
		model.addAttribute("currentMenu", currentMenu);
		model.addAttribute("activeMenus", activeMenus);
		//

	}

}
