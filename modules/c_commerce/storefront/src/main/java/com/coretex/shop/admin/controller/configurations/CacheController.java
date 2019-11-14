package com.coretex.shop.admin.controller.configurations;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.coretex.items.commerce_core_model.MerchantStoreItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.coretex.core.business.utils.CacheUtils;
import com.coretex.core.business.utils.ajax.AjaxResponse;
import com.coretex.shop.admin.controller.ControllerConstants;
import com.coretex.core.data.web.Menu;
import com.coretex.shop.constants.Constants;


@Controller
public class CacheController {

	private static final Logger LOGGER = LoggerFactory.getLogger(CacheController.class);

	@Resource
	private CacheUtils cache;


	@PreAuthorize("hasRole('AUTH')")
	@RequestMapping(value = "/admin/cache/cacheManagement.html", method = RequestMethod.GET)
	public String displayAccounts(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		this.setMenu(model, request);

		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);

		//get cache keys
		List<String> cacheKeysList = cache.getCacheKeys(store);

		model.addAttribute("keys", cacheKeysList);

		return ControllerConstants.Tiles.Configuration.cache;

	}


	@PreAuthorize("hasRole('AUTH')")
	@RequestMapping(value = "/admin/cache/clear.html", method = RequestMethod.POST)
	public @ResponseBody
	ResponseEntity<String> clearCache(HttpServletRequest request, HttpServletResponse response) {
		String cacheKey = request.getParameter("cacheKey");

		AjaxResponse resp = new AjaxResponse();

		try {

			MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);

			StringBuilder key = new StringBuilder();
			key.append(store.getUuid()).append("_").append(cacheKey);

			if (cacheKey != null) {
				cache.removeFromCache(key.toString());
			} else {
				cache.removeAllFromCache(store);
			}

			resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);

		} catch (Exception e) {
			LOGGER.error("Error while updateing groups", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}

		String returnString = resp.toJSONString();
		return new ResponseEntity<String>(returnString, HttpStatus.OK);
	}


	private void setMenu(Model model, HttpServletRequest request) throws Exception {

		//display menu
		Map<String, String> activeMenus = new HashMap<String, String>();
		activeMenus.put("cache", "cache");


		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>) request.getAttribute("MENUMAP");

		Menu currentMenu = menus.get("cache");
		model.addAttribute("currentMenu", currentMenu);
		model.addAttribute("activeMenus", activeMenus);
		//

	}


}
