package com.coretex.shop.admin.controller.products;

import com.coretex.core.business.services.catalog.category.CategoryService;
import com.coretex.core.business.services.catalog.product.ProductService;
import com.coretex.core.business.utils.ajax.AjaxPageableResponse;
import com.coretex.core.business.utils.ajax.AjaxResponse;
import com.coretex.core.model.catalog.product.ProductCriteria;
import com.coretex.core.model.catalog.product.ProductList;
import com.coretex.items.commerce_core_model.CategoryItem;
import com.coretex.items.core.LocaleItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.ProductItem;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

@Controller
public class ProductsController {

	@Resource
	CategoryService categoryService;

	@Resource
	ProductService productService;

	@Resource
	LabelUtils messages;

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductsController.class);

	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value = "/admin/products/products.html", method = RequestMethod.GET)
	public String displayProducts(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {


		setMenu(model, request);

		LocaleItem language = (LocaleItem) request.getAttribute("LANGUAGE");
		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);

		List<CategoryItem> categories = categoryService.listByStore(store, language);

		model.addAttribute("categories", categories);

		return "admin-products";

	}


	@SuppressWarnings({"rawtypes", "unchecked"})
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value = "/admin/products/paging.html", method = RequestMethod.POST)
	public @ResponseBody
	ResponseEntity<String> pageProducts(HttpServletRequest request, HttpServletResponse response) {

		//TODO what if ROOT

		String categoryId = request.getParameter("categoryId");
		String sku = request.getParameter("sku");
		String available = request.getParameter("available");
		String searchTerm = request.getParameter("searchTerm");
		String name = request.getParameter("name");

		AjaxPageableResponse resp = new AjaxPageableResponse();

		try {


			int startRow = Integer.parseInt(request.getParameter("_startRow"));
			int endRow = Integer.parseInt(request.getParameter("_endRow"));

			LocaleItem language = (LocaleItem) request.getAttribute("LANGUAGE");
			MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);

			ProductCriteria criteria = new ProductCriteria();

			criteria.setStartIndex(startRow);
			criteria.setMaxCount(endRow);


			if (!StringUtils.isBlank(categoryId) && !categoryId.equals("-1")) {

				//get other filters
				UUID lcategoryId;
				try {
					lcategoryId = UUID.fromString(categoryId);
				} catch (Exception e) {
					LOGGER.error("ProductItem page cannot parse categoryId " + categoryId);
					resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
					String returnString = resp.toJSONString();
					return new ResponseEntity<String>(returnString, HttpStatus.BAD_REQUEST);
				}


				if (lcategoryId != null) {

					CategoryItem category = categoryService.getByUUID(lcategoryId);

					if (category == null || !category.getMerchantStore().getUuid().equals(store.getUuid())) {
						resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
						String returnString = resp.toJSONString();
						return new ResponseEntity<String>(returnString, HttpStatus.BAD_REQUEST);
					}

					//get all sub categories
					StringBuilder lineage = new StringBuilder();
					lineage.append(category.getLineage()).append(category.getUuid()).append("/");

					List<CategoryItem> categories = categoryService.getListByLineage(store, lineage.toString());

					List<UUID> categoryIds = new ArrayList<>();

					for (CategoryItem cat : categories) {
						categoryIds.add(cat.getUuid());
					}
					categoryIds.add(category.getUuid());
					criteria.setCategoryIds(categoryIds);

				}


			}

			if (!StringUtils.isBlank(sku)) {
				criteria.setCode(sku);
			}

			if (!StringUtils.isBlank(name)) {
				criteria.setProductName(name);
			}

			if (!StringUtils.isBlank(available)) {
				if (available.equals("true")) {
					criteria.setAvailable(new Boolean(true));
				} else {
					criteria.setAvailable(new Boolean(false));
				}
			}

			ProductList productList = productService.listByStore(store, language, criteria);
			resp.setEndRow(productList.getTotalCount());
			resp.setStartRow(startRow);
			List<ProductItem> plist = productList.getProducts();

			if (plist != null) {

				for (ProductItem product : plist) {

					Map entry = new HashMap();
					entry.put("productId", product.getUuid().toString());

					entry.put("name", product.getName());
					entry.put("sku", product.getSku());
					entry.put("available", product.getAvailable());
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
	@RequestMapping(value = "/admin/products/remove.html", method = RequestMethod.POST)
	public @ResponseBody
	ResponseEntity<String> deleteProduct(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		String sid = request.getParameter("productId");

		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);

		AjaxResponse resp = new AjaxResponse();


		try {

			UUID id = UUID.fromString(sid);

			ProductItem product = productService.getByUUID(id);

			if (product == null || product.getMerchantStore().getUuid() != store.getUuid()) {

				resp.setStatusMessage(messages.getMessage("message.unauthorized", locale));
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);

			} else {

				productService.delete(product);
				resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);

			}


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
		activeMenus.put("catalogue-products", "catalogue-products");

		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>) request.getAttribute("MENUMAP");

		Menu currentMenu = menus.get("catalogue");
		model.addAttribute("currentMenu", currentMenu);
		model.addAttribute("activeMenus", activeMenus);
		//

	}

}
