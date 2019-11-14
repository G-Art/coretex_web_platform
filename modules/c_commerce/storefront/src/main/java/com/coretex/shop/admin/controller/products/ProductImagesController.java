package com.coretex.shop.admin.controller.products;

import com.coretex.core.business.services.catalog.product.ProductService;
import com.coretex.core.business.services.catalog.product.image.ImageDataHolder;
import com.coretex.core.business.services.catalog.product.image.ProductImageService;
import com.coretex.core.business.utils.ajax.AjaxPageableResponse;
import com.coretex.core.business.utils.ajax.AjaxResponse;
import com.coretex.items.commerce_core_model.ProductItem;
import com.coretex.items.commerce_core_model.ProductImageItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.shop.admin.controller.ControllerConstants;
import com.coretex.shop.admin.model.content.ProductImages;
import com.coretex.core.data.web.Menu;
import com.coretex.shop.constants.Constants;
import com.coretex.shop.utils.ImageFilePath;
import com.coretex.shop.utils.LabelUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.*;

@Controller
public class ProductImagesController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductImagesController.class);


	@Resource
	private ProductService productService;


	@Resource
	private ProductImageService productImageService;

	@Resource
	private LabelUtils messages;

	@Resource
	@Qualifier("img")
	private ImageFilePath imageUtils;


	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value = "/admin/products/images/list.html", method = RequestMethod.GET)
	public String displayProductImages(@RequestParam("id") String productId, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {


		setMenu(model, request);
		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);

		ProductItem product = productService.getById(UUID.fromString(productId));

		if (product == null) {
			return "redirect:/admin/products/products.html";
		}

		if (!product.getMerchantStore().getUuid().equals(store.getUuid())) {
			return "redirect:/admin/products/products.html";
		}

		model.addAttribute("product", product);
		return ControllerConstants.Tiles.Product.productImages;

	}

	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value = "/admin/products/images/url/list.html", method = RequestMethod.GET)
	public String displayProductImagesUrl(@RequestParam("id") String productId, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {


		setMenu(model, request);
		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);

		ProductItem product = productService.getById(UUID.fromString(productId));

		if (product == null) {
			return "redirect:/admin/products/products.html";
		}

		if (!product.getMerchantStore().getUuid().equals(store.getUuid())) {
			return "redirect:/admin/products/products.html";
		}

		Map<String, String> mediaTypes = new HashMap<String, String>();
		mediaTypes.put("0", "IMAGE");
		mediaTypes.put("1", "VIDEO");

		ProductImageItem productImage = new ProductImageItem();

		model.addAttribute("productImage", productImage);
		model.addAttribute("product", product);
		model.addAttribute("mediaTypes", mediaTypes);
		return ControllerConstants.Tiles.Product.productImagesUrl;

	}


	@SuppressWarnings({"rawtypes", "unchecked"})
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value = "/admin/products/images/page.html", method = RequestMethod.POST)
	public @ResponseBody
	ResponseEntity<String> pageProductImages(HttpServletRequest request, HttpServletResponse response) {

		String sProductId = request.getParameter("productId");


		AjaxResponse resp = new AjaxResponse();
		UUID productId;
		ProductItem product = null;

		try {
			productId = UUID.fromString(sProductId);
		} catch (Exception e) {
			resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorString("ProductItem id is not valid");
			String returnString = resp.toJSONString();
			return new ResponseEntity<>(returnString, HttpStatus.OK);
		}


		try {


			product = productService.getById(productId);

			MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);

			if (!product.getMerchantStore().getUuid().equals(store.getUuid())) {
				resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
				resp.setErrorString("Merchant id is not valid");
				String returnString = resp.toJSONString();
				return new ResponseEntity<>(returnString, HttpStatus.OK);
			}

			Set<ProductImageItem> images = product.getImages();

			if (images != null) {

				for (ProductImageItem image : images) {

					String imagePath = imageUtils.buildProductImageUtils(store, product, image.getProductImage());

					Map entry = new HashMap();
					//entry.put("picture", new StringBuilder().append(request.getContextPath()).append(imagePath).toString());
					entry.put("picture", imagePath);
					entry.put("name", image.getProductImage());
					entry.put("id", image.getUuid().toString());
					entry.put("defaultImageCheckmark", image.getDefaultImage() ? "/resources/img/admin/checkmark_checked.png" : "/resources/img/admin/checkmark_unchecked.png");

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


	@SuppressWarnings({"rawtypes", "unchecked"})
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value = "/admin/products/images/url/page.html", method = RequestMethod.POST)
	public @ResponseBody
	ResponseEntity<String> pageProductImagesUrl(HttpServletRequest request, HttpServletResponse response) {

		String sProductId = request.getParameter("productId");


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

			MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);

			if (!product.getMerchantStore().getUuid().equals(store.getUuid())) {
				resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
				resp.setErrorString("Merchant id is not valid");
				String returnString = resp.toJSONString();
				return new ResponseEntity<>(returnString, HttpStatus.OK);
			}

			Set<ProductImageItem> images = product.getImages();

			if (images != null) {

				for (ProductImageItem image : images) {

					if (!StringUtils.isBlank(image.getProductImageUrl())) {

						Map entry = new HashMap();
						entry.put("image", image.getProductImageUrl());
						entry.put("url", image.getProductImageUrl());
						entry.put("default", image.getDefaultImage());
						entry.put("id", image.getUuid());

						resp.addDataEntry(entry);

					}
				}

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
	@RequestMapping(value = "/admin/products/images/save.html", method = RequestMethod.POST)
	public String saveProductImages(@ModelAttribute(value = "productImages") @Valid final ProductImages productImages, final BindingResult bindingResult, final Model model, final HttpServletRequest request, Locale locale) throws Exception {


		this.setMenu(model, request);


		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);

		ProductItem product = productService.getById(productImages.getProductId());
		model.addAttribute("product", product);
		if (product == null) {
			FieldError error = new FieldError("productImages", "image", messages.getMessage("message.error", locale));
			bindingResult.addError(error);
			return ControllerConstants.Tiles.Product.productImages;
		}

		if (product.getMerchantStore().getUuid().equals(store.getUuid())) {
			FieldError error = new FieldError("productImages", "image", messages.getMessage("message.error", locale));
			bindingResult.addError(error);
		}

		if (bindingResult.hasErrors()) {
			LOGGER.info("Found {} Validation errors", bindingResult.getErrorCount());
			return ControllerConstants.Tiles.Product.productImages;

		}

		final List<ImageDataHolder<ProductImageItem>> contentImagesList = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(productImages.getFile())) {
			LOGGER.info("Saving {} content images for merchant {}", productImages.getFile().size(), store.getUuid());
			for (final MultipartFile multipartFile : productImages.getFile()) {
				if (!multipartFile.isEmpty()) {
					ProductImageItem productImage = new ProductImageItem();

					productImage.setProductImage(multipartFile.getOriginalFilename());
					productImage.setProduct(product);
					productImage.setDefaultImage(false);//default image is uploaded in the product details

					var imageDataHolder = new ImageDataHolder<>(productImage, multipartFile.getInputStream());

					contentImagesList.add(imageDataHolder);
				}
			}

			if (CollectionUtils.isNotEmpty(contentImagesList)) {
				productImageService.addProductImages(product, contentImagesList);
			}

		}


		//reload
		product = productService.getById(productImages.getProductId());
		model.addAttribute("product", product);
		model.addAttribute("success", "success");

		return ControllerConstants.Tiles.Product.productImages;
	}


	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value = "/admin/products/images/url/save.html", method = RequestMethod.POST)
	public String saveProductImagesUrl(@ModelAttribute(value = "productImage") @Valid final ProductImageItem productImage, final BindingResult bindingResult, final Model model, final HttpServletRequest request, Locale locale) throws Exception {


		this.setMenu(model, request);

		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);

		Map<String, String> mediaTypes = new HashMap<String, String>();
		mediaTypes.put("0", "IMAGE");
		mediaTypes.put("1", "VIDEO");

		model.addAttribute("productImage", productImage);
		model.addAttribute("mediaTypes", mediaTypes);

		ProductItem product = productService.getById(productImage.getUuid());
		model.addAttribute("product", product);
		if (product == null) {
			FieldError error = new FieldError("productImages", "image", messages.getMessage("message.error", locale));
			bindingResult.addError(error);
			return ControllerConstants.Tiles.Product.productImagesUrl;
		}

		if (!product.getMerchantStore().getUuid().equals(store.getUuid())) {
			FieldError error = new FieldError("productImages", "image", messages.getMessage("message.error", locale));
			bindingResult.addError(error);
		}

		model.addAttribute("product", product);

		if (bindingResult.hasErrors()) {
			LOGGER.info("Found {} Validation errors", bindingResult.getErrorCount());
			return ControllerConstants.Tiles.Product.productImagesUrl;
		}

		productImage.setProduct(product);
		productImage.setUuid(null);

		productImageService.saveOrUpdate(productImage);
		model.addAttribute("product", product);
		model.addAttribute("success", "success");

		return ControllerConstants.Tiles.Product.productImagesUrl;
	}


	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value = "/admin/products/images/remove.html", method = RequestMethod.POST)
	public @ResponseBody
	ResponseEntity<String> deleteImage(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		String sImageId = request.getParameter("id");


		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);
		AjaxResponse resp = new AjaxResponse();



		try {


			UUID imageId = UUID.fromString(sImageId);


			ProductImageItem productImage = productImageService.getById(imageId);
			if (productImage == null) {
				resp.setStatusMessage(messages.getMessage("message.unauthorized", locale));
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
				String returnString = resp.toJSONString();
				return new ResponseEntity<>(returnString, HttpStatus.OK);
			}

			if (!productImage.getProduct().getMerchantStore().getUuid().equals(store.getUuid())) {
				resp.setStatusMessage(messages.getMessage("message.unauthorized", locale));
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
				String returnString = resp.toJSONString();
				return new ResponseEntity<>(returnString, HttpStatus.OK);
			}

			productImageService.removeProductImage(productImage);

			resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);


		} catch (Exception e) {
			LOGGER.error("Error while deleting product price", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}

		String returnString = resp.toJSONString();
		return new ResponseEntity<String>(returnString, HttpStatus.OK);
	}


	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value = "/admin/products/images/defaultImage.html", method = RequestMethod.POST)
	public @ResponseBody
	ResponseEntity<String> setDefaultImage(final HttpServletRequest request,
										   final HttpServletResponse response,
										   final Locale locale) {
		final String sImageId = request.getParameter("id");
		final MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);
		final AjaxResponse resp = new AjaxResponse();


		try {
			final UUID imageId = UUID.fromString(sImageId);
			final ProductImageItem productImage = productImageService.getById(imageId);

			if (productImage == null) {
				resp.setStatusMessage(messages.getMessage("message.unauthorized", locale));
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
				String returnString = resp.toJSONString();
				return new ResponseEntity<>(returnString, HttpStatus.OK);
			}

			if (!productImage.getProduct().getMerchantStore().getUuid().equals(store.getUuid())) {
				resp.setStatusMessage(messages.getMessage("message.unauthorized", locale));
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
				String returnString = resp.toJSONString();
				return new ResponseEntity<>(returnString, HttpStatus.OK);
			}

			productImage.setDefaultImage(true);
			productImageService.saveOrUpdate(productImage);

			final Set<ProductImageItem> images = productService.getById(productImage.getProduct().getUuid()).getImages();
			for (final ProductImageItem image : images) {
				if (image.getUuid() != productImage.getUuid()) {
					image.setDefaultImage(false);
					productImageService.saveOrUpdate(image);
				}
			}

			resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);
		} catch (final Exception e) {
			LOGGER.error("Error while set default image", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}

		String returnString = resp.toJSONString();
		return new ResponseEntity<>(returnString, HttpStatus.OK);
	}


	private void setMenu(Model model, HttpServletRequest request) {

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
