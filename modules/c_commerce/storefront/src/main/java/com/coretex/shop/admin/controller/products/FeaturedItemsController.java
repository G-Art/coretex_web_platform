package com.coretex.shop.admin.controller.products;

import com.coretex.core.business.services.catalog.category.CategoryService;
import com.coretex.core.business.services.catalog.product.ProductService;
import com.coretex.core.business.services.catalog.product.relationship.ProductRelationshipService;
import com.coretex.core.business.utils.ajax.AjaxPageableResponse;
import com.coretex.core.business.utils.ajax.AjaxResponse;
import com.coretex.items.commerce_core_model.CategoryItem;
import com.coretex.items.commerce_core_model.ProductItem;
import com.coretex.items.commerce_core_model.ProductRelationshipItem;
import com.coretex.core.model.catalog.product.relationship.ProductRelationshipType;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.core.LocaleItem;
import com.coretex.core.data.web.Menu;
import com.coretex.shop.constants.Constants;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Controller
public class FeaturedItemsController {

	private static final Logger LOGGER = LoggerFactory.getLogger(FeaturedItemsController.class);

	@Resource
	CategoryService categoryService;

	@Resource
	ProductService productService;

	@Resource
	ProductRelationshipService productRelationshipService;

	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value = "/admin/catalogue/featured/list.html", method = RequestMethod.GET)
	public String displayFeaturedItems(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {


		setMenu(model, request);

		LocaleItem language = (LocaleItem) request.getAttribute("LANGUAGE");
		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);

		List<CategoryItem> categories = categoryService.listByStore(store, language);

		model.addAttribute("categories", categories);
		return "admin-catalogue-featured";

	}


	@SuppressWarnings({"rawtypes", "unchecked"})
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value = "/admin/catalogue/featured/paging.html", method = RequestMethod.POST)
	public @ResponseBody
	ResponseEntity<String> pageProducts(HttpServletRequest request, HttpServletResponse response) {


		AjaxResponse resp = new AjaxResponse();

		try {


			LocaleItem language = (LocaleItem) request.getAttribute("LANGUAGE");
			MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);


			List<ProductRelationshipItem> relationships = productRelationshipService.getByType(store, ProductRelationshipType.FEATURED_ITEM, language);

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

		return new ResponseEntity<>(returnString, HttpStatus.OK);


	}

	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value = "/admin/catalogue/featured/addItem.html", method = RequestMethod.POST)
	public @ResponseBody
	ResponseEntity<String> addItem(HttpServletRequest request, HttpServletResponse response) {

		String productId = request.getParameter("productId");
		AjaxResponse resp = new AjaxResponse();




		try {


			UUID lProductId = UUID.fromString(productId);

			MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);

			ProductItem product = productService.getByUUID(lProductId);

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
			relationship.setCode(ProductRelationshipType.FEATURED_ITEM.name());
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
		return new ResponseEntity<>(returnString, HttpStatus.OK);

	}

	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value = "/admin/catalogue/featured/removeItem.html&removeEntity=FEATURED", method = RequestMethod.POST)
	public @ResponseBody
	ResponseEntity<String> removeItem(HttpServletRequest request, HttpServletResponse response) {

		String productId = request.getParameter("productId");
		AjaxResponse resp = new AjaxResponse();



		try {


			UUID lproductId = UUID.fromString(productId);

			MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);

			ProductItem product = productService.getByUUID(lproductId);

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
			List<ProductRelationshipItem> relationships = productRelationshipService.getByType(store, ProductRelationshipType.FEATURED_ITEM);

			for (ProductRelationshipItem r : relationships) {
				if (r.getRelatedProduct().getUuid().equals(lproductId)) {
					relationship = r;
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
		return new ResponseEntity<>(returnString, HttpStatus.OK);

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
