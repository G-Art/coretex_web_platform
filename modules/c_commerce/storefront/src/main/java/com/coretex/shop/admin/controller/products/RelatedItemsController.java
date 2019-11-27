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
import com.coretex.shop.admin.controller.ControllerConstants;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Controller
public class RelatedItemsController {

	private static final Logger LOGGER = LoggerFactory.getLogger(RelatedItemsController.class);

	@Resource
	CategoryService categoryService;

	@Resource
	ProductService productService;

	@Resource
	ProductRelationshipService productRelationshipService;

	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value = "/admin/catalogue/related/list.html", method = RequestMethod.GET)
	public String displayRelatedItems(@RequestParam("id") String productId, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {


		setMenu(model, request);

		LocaleItem language = (LocaleItem) request.getAttribute("LANGUAGE");
		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);

		//get the product and validate it belongs to the current merchant
		ProductItem product = productService.getByUUID(UUID.fromString(productId));

		if (product == null) {
			return "redirect:/admin/products/products.html";
		}

		if (!product.getMerchantStore().getUuid().equals(store.getUuid())) {
			return "redirect:/admin/products/products.html";
		}


		List<CategoryItem> categories = categoryService.listByStore(store, language);

		model.addAttribute("categories", categories);
		model.addAttribute("product", product);
		return ControllerConstants.Tiles.Product.relatedItems;

	}


	@SuppressWarnings({"rawtypes", "unchecked"})
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value = "/admin/catalogue/related/paging.html", method = RequestMethod.POST)
	public @ResponseBody
	ResponseEntity<String> pageRelatedItems(HttpServletRequest request, HttpServletResponse response) {

		String sProductId = request.getParameter("productId");
		AjaxResponse resp = new AjaxResponse();


		try {


			UUID productId = UUID.fromString(sProductId);
			ProductItem product = productService.getByUUID(productId);

			LocaleItem language = (LocaleItem) request.getAttribute("LANGUAGE");
			MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);


			if (product == null || !product.getMerchantStore().getUuid().equals(store.getUuid())) {
				resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
				resp.setErrorString("ProductItem id is not valid");
				String returnString = resp.toJSONString();
				return new ResponseEntity<String>(returnString, HttpStatus.OK);
			}


			List<ProductRelationshipItem> relationships = productRelationshipService.getByType(store, product, ProductRelationshipType.RELATED_ITEM, language);

			for (ProductRelationshipItem relationship : relationships) {

				ProductItem relatedProduct = relationship.getRelatedProduct();
				Map entry = new HashMap();
				entry.put("relationshipId", relationship.getUuid());
				entry.put("productId", relatedProduct.getUuid());


				entry.put("name", relatedProduct.getName());
				entry.put("sku", relatedProduct.getSku());
				entry.put("available", relatedProduct.getAvailable());
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
	@RequestMapping(value = "/admin/catalogue/related/addItem.html", method = RequestMethod.POST)
	public @ResponseBody
	ResponseEntity<String> addItem(HttpServletRequest request, HttpServletResponse response) {

		String productId = request.getParameter("productId");
		String baseProductId = request.getParameter("baseProductId");
		AjaxResponse resp = new AjaxResponse();


		try {


			UUID lProductId = UUID.fromString(productId);
			UUID lBaseProductId = UUID.fromString(baseProductId);

			MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);

			ProductItem product = productService.getByUUID(lProductId);

			if (product == null) {
				resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
				String returnString = resp.toJSONString();
				return new ResponseEntity<String>(returnString, HttpStatus.OK);
			}

			if (!product.getMerchantStore().getUuid().equals(store.getUuid())) {
				resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
				String returnString = resp.toJSONString();
				return new ResponseEntity<String>(returnString, HttpStatus.OK);
			}

			ProductItem baseProduct = productService.getByUUID(lBaseProductId);

			if (baseProduct == null) {
				resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
				String returnString = resp.toJSONString();
				return new ResponseEntity<String>(returnString, HttpStatus.OK);
			}

			if (!baseProduct.getMerchantStore().getUuid().equals(store.getUuid())) {
				resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
				String returnString = resp.toJSONString();
				return new ResponseEntity<String>(returnString, HttpStatus.OK);
			}


			ProductRelationshipItem relationship = new ProductRelationshipItem();
			relationship.setActive(true);
			relationship.setProduct(baseProduct);
			relationship.setCode(ProductRelationshipType.RELATED_ITEM.name());
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
	@RequestMapping(value = "/admin/catalogue/related/removeItem.html", method = RequestMethod.POST)
	public @ResponseBody
	ResponseEntity<String> removeItem(HttpServletRequest request, HttpServletResponse response) {

		String productId = request.getParameter("productId");
		String baseProductId = request.getParameter("baseProductId");
		AjaxResponse resp = new AjaxResponse();


		try {


			UUID lproductId = UUID.fromString(productId);
			UUID lBaseProductId = UUID.fromString(baseProductId);

			MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);

			ProductItem product = productService.getByUUID(lproductId);

			if (product == null) {
				resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
				String returnString = resp.toJSONString();
				return new ResponseEntity<String>(returnString, HttpStatus.OK);
			}

			if (!product.getMerchantStore().getUuid().equals(store.getUuid())) {
				resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
				String returnString = resp.toJSONString();
				return new ResponseEntity<String>(returnString, HttpStatus.OK);
			}

			ProductItem baseProduct = productService.getByUUID(lBaseProductId);

			if (baseProduct == null) {
				resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
				String returnString = resp.toJSONString();
				return new ResponseEntity<String>(returnString, HttpStatus.OK);
			}

			if (!baseProduct.getMerchantStore().getUuid().equals(store.getUuid())) {
				resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
				String returnString = resp.toJSONString();
				return new ResponseEntity<String>(returnString, HttpStatus.OK);
			}

			ProductRelationshipItem relationship = null;
			List<ProductRelationshipItem> relationships = productRelationshipService.getByType(store, baseProduct, ProductRelationshipType.RELATED_ITEM);

			for (ProductRelationshipItem r : relationships) {
				if (r.getRelatedProduct().getUuid().equals(lproductId)) {
					relationship = r;
					break;
				}
			}

			if (relationship == null) {
				resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
				String returnString = resp.toJSONString();
				return new ResponseEntity<String>(returnString, HttpStatus.OK);
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
		activeMenus.put("catalogue-products", "catalogue-products");

		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>) request.getAttribute("MENUMAP");

		Menu currentMenu = menus.get("catalogue");
		model.addAttribute("currentMenu", currentMenu);
		model.addAttribute("activeMenus", activeMenus);
		//

	}

}
