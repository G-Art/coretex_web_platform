package com.coretex.shop.admin.controller.products;

import com.coretex.core.business.services.catalog.product.ProductService;
import com.coretex.core.business.services.catalog.product.review.ProductReviewService;
import com.coretex.core.business.utils.ajax.AjaxPageableResponse;
import com.coretex.core.business.utils.ajax.AjaxResponse;
import com.coretex.items.commerce_core_model.ProductItem;
import com.coretex.items.commerce_core_model.ProductReviewItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.shop.admin.controller.ControllerConstants;
import com.coretex.core.data.web.Menu;
import com.coretex.shop.constants.Constants;
import com.coretex.shop.utils.LabelUtils;
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
import java.util.*;

@Controller
public class ProductReviewController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductReviewController.class);

	@Resource
	private ProductService productService;

	@Resource
	private ProductReviewService productReviewService;

	@Resource
	LabelUtils messages;


	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value = "/admin/products/reviews.html", method = RequestMethod.GET)
	public String displayProductReviews(@RequestParam("id") String productId, Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {

		setMenu(model, request);

		LanguageItem language = (LanguageItem) request.getAttribute("LANGUAGE");


		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);
		ProductItem product = productService.getById(UUID.fromString(productId));

		if (product == null) {
			return "redirect:/admin/products/products.html";
		}

		if (!product.getMerchantStore().getUuid().equals(store.getUuid())) {
			return "redirect:/admin/products/products.html";
		}


		model.addAttribute("product", product);

		return ControllerConstants.Tiles.Product.productReviews;

	}


	@SuppressWarnings({"rawtypes", "unchecked"})
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value = "/admin/products/reviews/paging.html", method = RequestMethod.POST)
	public @ResponseBody
	ResponseEntity<String> pageProductReviews(HttpServletRequest request, HttpServletResponse response) {

		String sProductId = request.getParameter("productId");
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

			product = productService.getById(productId);


			if (product == null) {
				resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
				resp.setErrorString("ProductItem id is not valid");
				String returnString = resp.toJSONString();
				return new ResponseEntity<>(returnString, HttpStatus.OK);
			}

			if (!product.getMerchantStore().getUuid().equals(store.getUuid())) {
				resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
				resp.setErrorString("ProductItem id is not valid");
				String returnString = resp.toJSONString();
				return new ResponseEntity<String>(returnString, HttpStatus.OK);
			}


			LanguageItem language = (LanguageItem) request.getAttribute("LANGUAGE");


			List<ProductReviewItem> reviews = productReviewService.getByProduct(product);


			for (ProductReviewItem review : reviews) {
				Map entry = new HashMap();
				entry.put("reviewId", review.getUuid());
				entry.put("rating", review.getReviewRating().intValue());
				String reviewDesc = review.getDescription();
				//for(ProductReviewDescription description : descriptions){
				//	if(description.getLanguage().getCode().equals(language.getCode())) {
				//		reviewDesc = description.getDescription();
				//	}
				//}
				entry.put("description", reviewDesc);
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
	@RequestMapping(value = "/admin/products/reviews/remove.html", method = RequestMethod.POST)
	public @ResponseBody
	ResponseEntity<String> deleteProductReview(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		String sReviewid = request.getParameter("reviewId");


		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);

		AjaxResponse resp = new AjaxResponse();



		try {

			UUID reviewId = UUID.fromString(sReviewid);


			ProductReviewItem review = productReviewService.getById(reviewId);


			if (review == null || !review.getProduct().getMerchantStore().getUuid().equals(store.getUuid())) {

				resp.setStatusMessage(messages.getMessage("message.unauthorized", locale));
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
				String returnString = resp.toJSONString();
				return new ResponseEntity<>(returnString, HttpStatus.OK);
			}


			productReviewService.delete(review);


			resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);


		} catch (Exception e) {
			LOGGER.error("Error while deleting category", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}

		String returnString = resp.toJSONString();
		return new ResponseEntity<>(returnString, HttpStatus.OK);
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
