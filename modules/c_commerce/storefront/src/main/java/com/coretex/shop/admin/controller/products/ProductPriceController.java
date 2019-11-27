package com.coretex.shop.admin.controller.products;

import com.coretex.core.business.services.catalog.product.ProductService;
import com.coretex.core.business.services.catalog.product.price.ProductPriceService;
import com.coretex.core.business.utils.ProductPriceUtils;
import com.coretex.core.business.utils.ajax.AjaxPageableResponse;
import com.coretex.core.business.utils.ajax.AjaxResponse;
import com.coretex.enums.commerce_core_model.ProductPriceTypeEnum;
import com.coretex.items.core.LocaleItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.ProductAvailabilityItem;
import com.coretex.items.commerce_core_model.ProductItem;
import com.coretex.items.commerce_core_model.ProductPriceItem;
import com.coretex.shop.admin.controller.ControllerConstants;
import com.coretex.core.data.web.Menu;
import com.coretex.shop.constants.Constants;
import com.coretex.shop.utils.DateUtil;
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
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Controller
public class ProductPriceController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductPriceController.class);

	@Resource
	private ProductService productService;

	@Resource
	private ProductPriceService productPriceService;

	@Resource
	private ProductPriceUtils priceUtil;

	@Resource
	LabelUtils messages;

	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value = "/admin/products/prices.html", method = RequestMethod.GET)
	public String getProductPrices(@RequestParam("id") String productId, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		setMenu(model, request);

		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);

		//get the product and validate it belongs to the current merchant
		ProductItem product = productService.getByUUID(UUID.fromString(productId));

		if (product == null) {
			return "redirect:/admin/products/products.html";
		}

		if (!product.getMerchantStore().getUuid().equals(store.getUuid())) {
			return "redirect:/admin/products/products.html";
		}

		ProductAvailabilityItem productAvailability = null;
		for (ProductAvailabilityItem availability : product.getAvailabilities()) {
			if (availability.getRegion().equals(com.coretex.core.business.constants.Constants.ALL_REGIONS)) {
				productAvailability = availability;
			}
		}

		model.addAttribute("product", product);
		model.addAttribute("availability", productAvailability);

		return ControllerConstants.Tiles.Product.productPrices;

	}

	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value = "/admin/products/prices/paging.html", method = RequestMethod.POST)
	public @ResponseBody
	ResponseEntity<String> pagePrices(HttpServletRequest request, HttpServletResponse response) {

		String sProductId = request.getParameter("productId");
		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);

		LocaleItem language = (LocaleItem) request.getAttribute("LANGUAGE");


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

			ProductAvailabilityItem defaultAvailability = null;

			Set<ProductAvailabilityItem> availabilities = product.getAvailabilities();

			//get default availability
			for (ProductAvailabilityItem availability : availabilities) {
				if (availability.getRegion().equals(com.coretex.core.business.constants.Constants.ALL_REGIONS)) {
					defaultAvailability = availability;
					break;
				}
			}

			if (defaultAvailability == null) {
				resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
				resp.setErrorString("ProductItem id is not valid");
				String returnString = resp.toJSONString();
				return new ResponseEntity<String>(returnString, HttpStatus.OK);
			}

			Set<ProductPriceItem> prices = defaultAvailability.getPrices();


			for (ProductPriceItem price : prices) {
				Map entry = new HashMap();
				entry.put("priceId", price.getUuid());


				String priceName = price.getName();

				entry.put("name", priceName);
				entry.put("price", priceUtil.getAdminFormatedAmountWithCurrency(store, price.getProductPriceAmount()));
				entry.put("specialPrice", priceUtil.getAdminFormatedAmountWithCurrency(store, price.getProductPriceSpecialAmount()));

				String discount = "";
				if (priceUtil.hasDiscount(price)) {
					discount = priceUtil.getAdminFormatedAmountWithCurrency(store, price.getProductPriceAmount());
				}
				entry.put("special", discount);

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
	@RequestMapping(value = "/admin/products/price/edit.html", method = RequestMethod.GET)
	public String editProductPrice(@RequestParam("id") String productPriceId, @RequestParam("productId") String productId, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);
		ProductItem product = productService.getByUUID(UUID.fromString(productId));

		if (product == null) {
			return "redirect:/admin/products/products.html";
		}

		if (!product.getMerchantStore().getUuid().equals(store.getUuid())) {
			return "redirect:/admin/products/products.html";
		}


		setMenu(model, request);
		return displayProductPrice(product, UUID.fromString(productPriceId), model, request, response);

	}

	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value = "/admin/products/price/create.html", method = RequestMethod.GET)
	public String displayCreateProductPrice(@RequestParam("productId") String productId, @RequestParam("availabilityId") String avilabilityId, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);
		ProductItem product = productService.getByUUID(UUID.fromString(productId));
		if (product == null) {
			return "redirect:/admin/products/products.html";
		}

		if (!product.getMerchantStore().getUuid().equals(store.getUuid())) {
			return "redirect:/admin/products/products.html";
		}

		setMenu(model, request);
		return displayProductPrice(product, null, model, request, response);


	}

	private String displayProductPrice(ProductItem product, UUID productPriceId, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {


		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);

		com.coretex.shop.admin.model.catalog.ProductPrice pprice = new com.coretex.shop.admin.model.catalog.ProductPrice();

		ProductPriceItem productPrice = null;
		ProductAvailabilityItem productAvailability = null;

		if (productPriceId != null) {

			Set<ProductAvailabilityItem> availabilities = product.getAvailabilities();

			//get default availability
			for (ProductAvailabilityItem availability : availabilities) {
				if (availability.getRegion().equals(com.coretex.core.business.constants.Constants.ALL_REGIONS)) {//TODO to be updated when multiple regions is implemented
					productAvailability = availability;
					Set<ProductPriceItem> prices = availability.getPrices();
					for (ProductPriceItem price : prices) {
						if (price.getUuid().equals(productPriceId)) {
							productPrice = price;
							if (price.getProductPriceSpecialStartDate() != null) {
								pprice.setProductPriceSpecialStartDate(DateUtil.formatDate(price.getProductPriceSpecialStartDate()));
							}
							if (price.getProductPriceSpecialEndDate() != null) {
								pprice.setProductPriceSpecialEndDate(DateUtil.formatDate(price.getProductPriceSpecialEndDate()));
							}
							pprice.setPriceText(priceUtil.getAdminFormatedAmount(price.getProductPriceAmount()));
							if (price.getProductPriceSpecialAmount() != null) {
								pprice.setSpecialPriceText(priceUtil.getAdminFormatedAmount(price.getProductPriceSpecialAmount()));
							}
							break;
						}
					}
				}
			}

		}

		if (productPrice == null) {
			productPrice = new ProductPriceItem();
			productPrice.setProductPriceType(ProductPriceTypeEnum.ONE_TIME);
		}


		if (productAvailability == null) {
			Set<ProductAvailabilityItem> availabilities = product.getAvailabilities();
			for (ProductAvailabilityItem availability : availabilities) {
				if (availability.getRegion().equals(com.coretex.core.business.constants.Constants.ALL_REGIONS)) {//TODO to be updated when multiple regions is implemented
					productAvailability = availability;
					break;
				}
			}
		}
		pprice.setProductAvailability(productAvailability);
		pprice.setPrice(productPrice);
		pprice.setProduct(product);


		model.addAttribute("product", product);
		//model.addAttribute("descriptions",descriptions);
		model.addAttribute("price", pprice);
		//model.addAttribute("availability",productAvailability);

		return ControllerConstants.Tiles.Product.productPrice;
	}


	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value = "/admin/products/price/save.html", method = RequestMethod.POST)
	public String saveProductPrice(@Valid @ModelAttribute("price") com.coretex.shop.admin.model.catalog.ProductPrice price, BindingResult result, Model model, HttpServletRequest request, Locale locale) throws Exception {

		//dates after save

		setMenu(model, request);

		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);

		ProductItem product = price.getProduct();
		ProductItem dbProduct = productService.getByUUID(product.getUuid());
		if (!store.getUuid().equals(dbProduct.getMerchantStore().getUuid())) {
			return "redirect:/admin/products/products.html";
		}

		model.addAttribute("product", dbProduct);

		//validate price
		BigDecimal submitedPrice = null;
		try {
			submitedPrice = priceUtil.getAmount(price.getPriceText());
		} catch (Exception e) {
			ObjectError error = new ObjectError("productPrice", messages.getMessage("NotEmpty.product.productPrice", locale));
			result.addError(error);
		}

		//validate discount price
		BigDecimal submitedDiscountPrice = null;

		if (!StringUtils.isBlank(price.getSpecialPriceText())) {
			try {
				submitedDiscountPrice = priceUtil.getAmount(price.getSpecialPriceText());
			} catch (Exception e) {
				ObjectError error = new ObjectError("productSpecialPrice", messages.getMessage("NotEmpty.product.productPrice", locale));
				result.addError(error);
			}
		}

		//validate start date
		if (!StringUtils.isBlank(price.getProductPriceSpecialStartDate())) {
			try {
				Date startDate = DateUtil.getDate(price.getProductPriceSpecialStartDate());
				price.getPrice().setProductPriceSpecialStartDate(startDate);
			} catch (Exception e) {
				ObjectError error = new ObjectError("productPriceSpecialStartDate", messages.getMessage("message.invalid.date", locale));
				result.addError(error);
			}
		}

		if (!StringUtils.isBlank(price.getProductPriceSpecialEndDate())) {
			try {
				Date endDate = DateUtil.getDate(price.getProductPriceSpecialEndDate());
				price.getPrice().setProductPriceSpecialEndDate(endDate);
			} catch (Exception e) {
				ObjectError error = new ObjectError("productPriceSpecialEndDate", messages.getMessage("message.invalid.date", locale));
				result.addError(error);
			}
		}


		if (result.hasErrors()) {
			return ControllerConstants.Tiles.Product.productPrice;
		}


		price.getPrice().setProductPriceAmount(submitedPrice);
		if (!StringUtils.isBlank(price.getSpecialPriceText())) {
			price.getPrice().setProductPriceSpecialAmount(submitedDiscountPrice);
		}

		ProductAvailabilityItem productAvailability = null;

		Set<ProductAvailabilityItem> availabilities = dbProduct.getAvailabilities();
		for (ProductAvailabilityItem availability : availabilities) {

			if (availability.getUuid().equals(price.getProductAvailability().getUuid())) {
				productAvailability = availability;
				break;
			}


		}

		price.getPrice().setProductAvailability(productAvailability);

		productPriceService.save(price.getPrice());
		model.addAttribute("success", "success");

		return ControllerConstants.Tiles.Product.productPrice;

	}

	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value = "/admin/products/price/remove.html", method = RequestMethod.POST)
	public @ResponseBody
	ResponseEntity<String> deleteProductPrice(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		String sPriceid = request.getParameter("priceId");


		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);
		AjaxResponse resp = new AjaxResponse();



		try {

			UUID priceId = UUID.fromString(sPriceid);
			ProductPriceItem price = productPriceService.getByUUID(priceId);


			if (price == null || !price.getProductAvailability().getProduct().getMerchantStore().getUuid().equals(store.getUuid())) {

				resp.setStatusMessage(messages.getMessage("message.unauthorized", locale));
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
				String returnString = resp.toJSONString();
				return new ResponseEntity<String>(returnString, HttpStatus.OK);
			}

			productPriceService.delete(price);


			resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);


		} catch (Exception e) {
			LOGGER.error("Error while deleting product price", e);
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
