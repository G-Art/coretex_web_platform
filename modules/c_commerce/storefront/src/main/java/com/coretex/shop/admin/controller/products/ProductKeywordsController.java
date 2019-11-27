package com.coretex.shop.admin.controller.products;

import com.coretex.core.business.services.catalog.product.ProductService;
import com.coretex.core.business.utils.ajax.AjaxPageableResponse;
import com.coretex.core.business.utils.ajax.AjaxResponse;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.ProductItem;
import com.coretex.shop.admin.controller.ControllerConstants;
import com.coretex.shop.admin.model.catalog.Keyword;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

@Controller
public class ProductKeywordsController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductKeywordsController.class);

	@Resource
	private ProductService productService;

	@Resource
	LabelUtils messages;


	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value = {"/admin/products/product/keywords.html"}, method = RequestMethod.GET)
	public String displayKeywords(@RequestParam("id") String productId, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		this.setMenu(model, request);
		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);

		ProductItem product = productService.getByUUID(UUID.fromString(productId));

		if (product == null || !product.getMerchantStore().getUuid().equals(store.getUuid())) {
			return "redirect:/admin/products/products.html";
		}

		model.addAttribute("store", store);
		model.addAttribute("product", product);
		model.addAttribute("productKeyword", new Keyword());

		return ControllerConstants.Tiles.Product.productKeywords;

	}

	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value = "/admin/products/product/addKeyword.html", method = RequestMethod.POST)
	public String addKeyword(@Valid @ModelAttribute("productKeyword") Keyword keyword, final BindingResult bindingResult, final Model model, final HttpServletRequest request, Locale locale) throws Exception {
		this.setMenu(model, request);
		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);


		ProductItem product = productService.getByUUID(keyword.getProductId());

		model.addAttribute("store", store);
		model.addAttribute("product", product);
		model.addAttribute("productKeyword", new Keyword());

		if (product == null || !product.getMerchantStore().getUuid().equals(store.getUuid())) {
			return "redirect:/admin/products/products.html";
		}

		String keywords = product.getMetatagKeywords();
		List<String> keyWordsList = null;
		if (!StringUtils.isBlank(keywords)) {
			String[] splits = keywords.split(",");
			keyWordsList = new ArrayList(Arrays.asList(splits));
		}

		if (keyWordsList == null) {
			keyWordsList = new ArrayList<String>();
		}
		keyWordsList.add(keyword.getKeyword());

		StringBuilder kwString = new StringBuilder();
		for (String s : keyWordsList) {
			kwString.append(s).append(",");
		}
		productService.update(product);
		model.addAttribute("success", "success");


		return ControllerConstants.Tiles.Product.productKeywords;
	}

	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value = "/admin/products/product/removeKeyword.html", method = RequestMethod.POST)
	public @ResponseBody
	ResponseEntity<String> removeKeyword(@RequestParam("id") String productId, HttpServletRequest request, HttpServletResponse response, Locale locale) {


		String code = request.getParameter("code");
		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);

		AjaxResponse resp = new AjaxResponse();



		try {

			//parse code i,lang (0,en)
			String[] ids = code.split(",");

			String languageCode = ids[1];

			int index = Integer.parseInt(ids[0]);

			ProductItem product = productService.getByUUID(UUID.fromString(productId));


			if (product == null) {
				resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
				resp.setErrorString("ProductItem id is not valid");
				String returnString = resp.toJSONString();
				return new ResponseEntity<String>(returnString, HttpStatus.OK);
			}

			if (!product.getMerchantStore().getUuid().equals(store.getUuid())) {
				resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
				resp.setErrorString("ProductItem id is not valid");
				String returnString = resp.toJSONString();
				return new ResponseEntity<String>(returnString, HttpStatus.OK);
			}


			List<String> keyWordsList = new ArrayList<>();


			String keywords = product.getMetatagKeywords();
			if (!StringUtils.isBlank(keywords)) {
				String[] splitKeywords = keywords.split(",");
				for (int i = 0; i < splitKeywords.length; i++) {

					if (i != index) {
						keyWordsList.add(splitKeywords[i]);
					}


				}
			}


			StringBuilder kwString = new StringBuilder();
			for (String s : keyWordsList) {
				kwString.append(s).append(",");
			}


			product.setMetatagKeywords(kwString.toString());
			productService.update(product);
			resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);

		} catch (Exception e) {
			LOGGER.error("Error while deleting product", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}

		String returnString = resp.toJSONString();
		return new ResponseEntity<String>(returnString, HttpStatus.OK);
	}


	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value = "/admin/products/product/keywords/paging.html", method = RequestMethod.POST)
	public @ResponseBody
	ResponseEntity<String> pageKeywords(HttpServletRequest request, HttpServletResponse response) {

		String sProductId = request.getParameter("id");
		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);

		AjaxResponse resp = new AjaxResponse();


		UUID productId;
		ProductItem product = null;

		try {
			productId = UUID.fromString(sProductId);
		} catch (Exception e) {
			resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorString("ProductItem id is not valid");
			String returnString = resp.toJSONString();
			return new ResponseEntity<String>(returnString, HttpStatus.OK);
		}


		try {

			product = productService.getByUUID(productId);


			if (product == null) {
				resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
				resp.setErrorString("ProductItem id is not valid");
				String returnString = resp.toJSONString();
				return new ResponseEntity<String>(returnString, HttpStatus.OK);
			}

			if (!product.getMerchantStore().getUuid().equals(store.getUuid())) {
				resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
				resp.setErrorString("ProductItem id is not valid");
				String returnString = resp.toJSONString();
				return new ResponseEntity<String>(returnString, HttpStatus.OK);
			}


			String keywords = product.getMetatagKeywords();
			if (!StringUtils.isBlank(keywords)) {

				String[] splitKeywords = keywords.split(",");
				for (int i = 0; i < splitKeywords.length; i++) {
					Map entry = new HashMap();
					String keyword = splitKeywords[i];
					StringBuilder code = new StringBuilder();
					code.append(i);

					entry.put("code", code.toString());
					entry.put("keyword", keyword);
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


	private void setMenu(Model model, HttpServletRequest request) throws Exception {

		//display menu
		Map<String, String> activeMenus = new HashMap<String, String>();
		activeMenus.put("catalogue", "catalogue");
		activeMenus.put("catalogue-products", "catalogue-products");

		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>) request.getAttribute("MENUMAP");

		Menu currentMenu = menus.get("catalogue");
		model.addAttribute("currentMenu", currentMenu);
		model.addAttribute("activeMenus", activeMenus);
		//

	}

}
