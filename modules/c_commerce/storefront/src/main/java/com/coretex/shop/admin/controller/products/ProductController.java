package com.coretex.shop.admin.controller.products;

import com.coretex.core.business.services.catalog.category.CategoryService;
import com.coretex.core.business.services.catalog.product.ProductService;
import com.coretex.core.business.services.catalog.product.image.ProductImageService;
import com.coretex.core.business.services.catalog.product.manufacturer.ManufacturerService;
import com.coretex.core.business.utils.CoreConfiguration;
import com.coretex.core.business.utils.ProductPriceUtils;
import com.coretex.core.business.utils.ajax.AjaxPageableResponse;
import com.coretex.core.business.utils.ajax.AjaxResponse;
import com.coretex.core.data.web.Menu;
import com.coretex.items.commerce_core_model.CategoryItem;
import com.coretex.items.core.LocaleItem;
import com.coretex.items.cx_core.ManufacturerItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.ProductAttributeItem;
import com.coretex.items.commerce_core_model.ProductAvailabilityItem;
import com.coretex.items.commerce_core_model.ProductImageItem;
import com.coretex.items.commerce_core_model.ProductItem;
import com.coretex.items.commerce_core_model.ProductPriceItem;
import com.coretex.shop.admin.controller.AbstractController;
import com.coretex.shop.admin.forms.ProductForm;
import com.coretex.shop.admin.mapppers.ProductFormMapper;
import com.coretex.shop.constants.Constants;
import com.coretex.shop.utils.DateUtil;
import com.coretex.shop.utils.LabelUtils;
import com.google.common.collect.Sets;
import org.apache.commons.collections4.CollectionUtils;
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
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Controller
public class ProductController extends AbstractController {


	private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

	@Resource
	private ProductFormMapper productFormMapper;

	@Resource
	private ProductService productService;

	@Resource
	private ManufacturerService manufacturerService;

	@Resource
	private ProductImageService productImageService;

	@Resource
	private ProductPriceUtils priceUtil;

	@Resource
	private LabelUtils messages;

	@Resource
	private CoreConfiguration configuration;

	@Resource
	private CategoryService categoryService;

	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value = "/admin/products/editProduct.html", method = RequestMethod.GET)
	public String displayProductEdit(@RequestParam("id") String productId, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return displayProduct(Optional.ofNullable(UUID.fromString(productId)), model, request, response);

	}

	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value = "/admin/products/viewEditProduct.html", method = RequestMethod.GET)
	public String displayProductViewEdit(@RequestParam("sku") String sku, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		ProductItem dbProduct = productService.getByCode(sku);

		UUID productId = null;//non existent
		if (dbProduct != null) {
			productId = dbProduct.getUuid();
		}

		return displayProduct(Optional.ofNullable(productId), model, request, response);
	}

	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value = "/admin/products/createProduct.html", method = RequestMethod.GET)
	public String displayProductCreate(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return displayProduct(Optional.empty(), model, request, response);

	}


	private String displayProduct(Optional<UUID> productId, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		//display menu
		setMenu(model, request);


		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);
		LocaleItem language = (LocaleItem) request.getAttribute("LANGUAGE");


		List<ManufacturerItem> manufacturers = manufacturerService.listByStore(store, language);

		var product = new ProductForm();
		var dbProduct = new ProductItem();
		if (productId.isPresent()) {
			dbProduct = productService.getByUUID(productId.get());
		}
		productFormMapper.updateFromProductItem(dbProduct, product);


		model.addAttribute("product", product);
		model.addAttribute("productItem", dbProduct);
		model.addAttribute("manufacturers", manufacturers);
		return "admin-products-edit";
	}


	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value = "/admin/products/save.html", method = RequestMethod.POST)
	public String saveProduct(@Valid @ModelAttribute("product") ProductForm product, BindingResult result, Model model, HttpServletRequest request, Locale locale) throws Exception {


		LocaleItem language = (LocaleItem) request.getAttribute("LANGUAGE");

		//display menu
		setMenu(model, request);

		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);

		List<ManufacturerItem> manufacturers = manufacturerService.listByStore(store, language);


		model.addAttribute("manufacturers", manufacturers);

		var date = new Date();
		if (!StringUtils.isBlank(product.getDateAvailable())) {
			try {
				date = DateUtil.getDate(product.getDateAvailable());
				product.getAvailability().setProductDateAvailable(date);
				product.setDateAvailable(DateUtil.formatDate(date));
			} catch (Exception e) {
				ObjectError error = new ObjectError("dateAvailable", messages.getMessage("message.invalid.date", locale));
				result.addError(error);
			}
		}

		//validate image
		if (product.getImage() != null && !product.getImage().isEmpty()) {

			try {

				String maxHeight = configuration.getProperty("PRODUCT_IMAGE_MAX_HEIGHT_SIZE");
				String maxWidth = configuration.getProperty("PRODUCT_IMAGE_MAX_WIDTH_SIZE");
				String maxSize = configuration.getProperty("PRODUCT_IMAGE_MAX_SIZE");

				BufferedImage image = ImageIO.read(product.getImage().getInputStream());

				if (!StringUtils.isBlank(maxHeight)) {

					int maxImageHeight = Integer.parseInt(maxHeight);
					if (image.getHeight() > maxImageHeight) {
						ObjectError error = new ObjectError("image", messages.getMessage("message.image.height", locale) + " {" + maxHeight + "}");
						result.addError(error);
					}

				}

				if (!StringUtils.isBlank(maxWidth)) {

					int maxImageWidth = Integer.parseInt(maxWidth);
					if (image.getWidth() > maxImageWidth) {
						ObjectError error = new ObjectError("image", messages.getMessage("message.image.width", locale) + " {" + maxWidth + "}");
						result.addError(error);
					}

				}

				if (!StringUtils.isBlank(maxSize)) {

					int maxImageSize = Integer.parseInt(maxSize);
					if (product.getImage().getSize() > maxImageSize) {
						ObjectError error = new ObjectError("image", messages.getMessage("message.image.size", locale) + " {" + maxSize + "}");
						result.addError(error);
					}

				}

			} catch (Exception e) {
				LOGGER.error("Cannot validate product image", e);
			}

		}

		if (result.hasErrors()) {
			return "admin-products-edit";
		}

		ProductItem newProduct = new ProductItem();

		if (product.getUuid() != null) {

			//get actual product
			newProduct = productService.getByUUID(product.getUuid());
			if (newProduct != null && !newProduct.getMerchantStore().getUuid().equals(store.getUuid())) {
				return "redirect:/admin/products/products.html";
			}

		}

		productFormMapper.updateToProductItem(product, newProduct);

		productService.save(newProduct);



		model.addAttribute("productItem", newProduct);
		model.addAttribute("success", "success");

		return "admin-products-edit";
	}


	/**
	 * Creates a duplicate product with the same inner object graph
	 * Will ignore SKU, reviews and images
	 *
	 * @param id
	 * @param result
	 * @param model
	 * @param request
	 * @param locale
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value = "/admin/products/product/duplicate.html", method = RequestMethod.POST)
	public String duplicateProduct(@ModelAttribute("productId") String id, BindingResult result, Model model, HttpServletRequest request, Locale locale) throws Exception {


		LocaleItem language = (LocaleItem) request.getAttribute("LANGUAGE");

		//display menu
		setMenu(model, request);

		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);

		List<ManufacturerItem> manufacturers = manufacturerService.listByStore(store, language);

		model.addAttribute("manufacturers", manufacturers);

		ProductItem dbProduct = productService.getByUUID(UUID.fromString(id));
		ProductItem newProduct = new ProductItem();

		if (dbProduct == null || dbProduct.getMerchantStore().getUuid().equals(store.getUuid())) {
			return "redirect:/admin/products/products.html";
		}

		//Make a copy of the product
		com.coretex.shop.admin.model.catalog.Product product = new com.coretex.shop.admin.model.catalog.Product();

		Set<ProductAvailabilityItem> availabilities = new HashSet<>();
		//availability - price
		for (ProductAvailabilityItem pAvailability : dbProduct.getAvailabilities()) {

			ProductAvailabilityItem availability = new ProductAvailabilityItem();
			availability.setProductDateAvailable(pAvailability.getProductDateAvailable());
			availability.setProductIsAlwaysFreeShipping(pAvailability.getProductIsAlwaysFreeShipping());
			availability.setProductQuantity(pAvailability.getProductQuantity());
			availability.setProductQuantityOrderMax(pAvailability.getProductQuantityOrderMax());
			availability.setProductQuantityOrderMin(pAvailability.getProductQuantityOrderMin());
			availability.setProductStatus(pAvailability.getProductStatus());
			availability.setRegion(pAvailability.getRegion());
			availability.setRegionVariant(pAvailability.getRegionVariant());


			Set<ProductPriceItem> prices = pAvailability.getPrices();
			for (ProductPriceItem pPrice : prices) {

				ProductPriceItem price = new ProductPriceItem();
				price.setDefaultPrice(pPrice.getDefaultPrice());
				price.setProductPriceAmount(pPrice.getProductPriceAmount());
				price.setProductAvailability(availability);
				price.setProductPriceSpecialAmount(pPrice.getProductPriceSpecialAmount());
				price.setProductPriceSpecialEndDate(pPrice.getProductPriceSpecialEndDate());
				price.setProductPriceSpecialStartDate(pPrice.getProductPriceSpecialStartDate());

				if (price.getDefaultPrice()) {
					product.setPrice(price);
					product.setProductPrice(priceUtil.getAdminFormatedAmount(price.getProductPriceAmount()));
				}

				availability.getPrices().add(price);
			}


			if (availability.getRegion().equals(com.coretex.core.business.constants.Constants.ALL_REGIONS)) {
				product.setAvailability(availability);
			}

			availabilities.add(availability);
		}

		newProduct.setAvailabilities(availabilities);


		//attributes
		Set<ProductAttributeItem> attributes = new HashSet<ProductAttributeItem>();
		for (ProductAttributeItem pAttribute : dbProduct.getAttributes()) {

			ProductAttributeItem attribute = new ProductAttributeItem();
			attribute.setAttributeDefault(pAttribute.getAttributeDefault());
			attribute.setAttributeDiscounted(pAttribute.getAttributeDiscounted());
			attribute.setAttributeDisplayOnly(pAttribute.getAttributeDisplayOnly());
			attribute.setAttributeRequired(pAttribute.getAttributeRequired());
			attribute.setProductAttributePrice(pAttribute.getProductAttributePrice());
			attribute.setProductAttributeIsFree(pAttribute.getProductAttributeIsFree());
			attribute.setProductAttributeWeight(pAttribute.getProductAttributeWeight());
			attribute.setProductOptionSortOrder(pAttribute.getProductOptionSortOrder());
			attributes.add(attribute);

		}
		newProduct.setAttributes(attributes);

		newProduct.setAvailable(dbProduct.getAvailable());


		//copy
		// newProduct.setCategories(dbProduct.getCategories());
		newProduct.setDateAvailable(dbProduct.getDateAvailable());
		newProduct.setManufacturer(dbProduct.getManufacturer());
		newProduct.setMerchantStore(store);
		newProduct.setProductHeight(dbProduct.getProductHeight());
		newProduct.setProductIsFree(dbProduct.getProductIsFree());
		newProduct.setProductLength(dbProduct.getProductLength());
		newProduct.setProductOrdered(dbProduct.getProductOrdered());
		newProduct.setProductWeight(dbProduct.getProductWeight());
		newProduct.setProductWidth(dbProduct.getProductWidth());
		newProduct.setSortOrder(dbProduct.getSortOrder());
		newProduct.setSku(UUID.randomUUID().toString().replace("-", ""));
		newProduct.setProductVirtual(dbProduct.getProductVirtual());
		newProduct.setProductShippable(dbProduct.getProductShippable());

		productService.update(newProduct);

		Set<CategoryItem> categories = dbProduct.getCategories();
		for (CategoryItem category : categories) {
			CategoryItem categoryCopy = categoryService.getByUUID(category.getUuid());
			newProduct.getCategories().add(categoryCopy);
			productService.update(newProduct);
		}

		product.setProduct(newProduct);
		model.addAttribute("product", product);
		model.addAttribute("success", "success");

		return "redirect:/admin/products/editProduct.html?id=" + newProduct.getUuid();
	}


	/**
	 * Removes a product image based on the productimage id
	 *
	 * @param request
	 * @param response
	 * @param locale
	 * @return
	 */
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value = "/admin/products/product/removeImage.html")
	public @ResponseBody
	ResponseEntity<String> removeImage(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		String iid = request.getParameter("imageId");

		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);

		AjaxResponse resp = new AjaxResponse();


		try {

			UUID id = UUID.fromString(iid);
			ProductImageItem productImage = productImageService.getByUUID(id);
			if (productImage == null || !productImage.getProduct().getMerchantStore().getUuid().equals(store.getUuid())) {

				resp.setStatusMessage(messages.getMessage("message.unauthorized", locale));
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);

			} else {

				productImageService.removeProductImage(productImage);
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


	/**
	 * List all categories and let the merchant associate the product to a category
	 *
	 * @param productId
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value = "/admin/products/displayProductToCategories.html", method = RequestMethod.GET)
	public String displayAddProductToCategories(@RequestParam("id") String productId, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {


		setMenu(model, request);
		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);
		LocaleItem language = (LocaleItem) request.getAttribute("LANGUAGE");


		//get the product and validate it belongs to the current merchant
		ProductItem product = productService.getByUUID(UUID.fromString(productId));

		if (product == null) {
			return "redirect:/admin/products/products.html";
		}

		if (!product.getMerchantStore().getUuid().equals(store.getUuid())) {
			return "redirect:/admin/products/products.html";
		}


		//get parent categories
		List<CategoryItem> categories = categoryService.listByStore(store, language);
		var productCategories = product.getCategories();
		if(CollectionUtils.isNotEmpty(productCategories)){
			model.addAttribute("categoryId", productCategories.iterator().next().getUuid());
		}

		model.addAttribute("product", product);
		model.addAttribute("categories", categories);
		return "catalogue-product-categories";

	}

	/**
	 * List all categories associated to a ProductItem
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value = "/admin/product-categories/paging.html", method = RequestMethod.POST)
	public @ResponseBody
	ResponseEntity<String> pageProductCategories(HttpServletRequest request, HttpServletResponse response) {

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
			return new ResponseEntity<>(returnString, HttpStatus.OK);
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


			LocaleItem language = (LocaleItem) request.getAttribute("LANGUAGE");


			Set<CategoryItem> categories = product.getCategories();


			for (CategoryItem category : categories) {
				Map entry = new HashMap();
				entry.put("categoryId", category.getUuid());

				String categoryName = category.getName();

				entry.put("name", categoryName);
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
	@RequestMapping(value = "/admin/product-categories/remove.html", method = RequestMethod.POST)
	public @ResponseBody
	ResponseEntity<String> deleteProductFromCategory(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		String sCategoryid = request.getParameter("categoryId");
		String sProductId = request.getParameter("productId");

		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);

		AjaxResponse resp = new AjaxResponse();


		try {


			CategoryItem category = categoryService.getByUUID(UUID.fromString(sCategoryid));
			ProductItem product = productService.getByUUID(UUID.fromString(sProductId));

			if (category == null || category.getMerchantStore().getUuid() != store.getUuid()) {

				resp.setStatusMessage(messages.getMessage("message.unauthorized", locale));
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
				String returnString = resp.toJSONString();
				return new ResponseEntity<String>(returnString, HttpStatus.OK);
			}

			if (product == null || product.getMerchantStore().getUuid() != store.getUuid()) {

				resp.setStatusMessage(messages.getMessage("message.unauthorized", locale));
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
				String returnString = resp.toJSONString();
				return new ResponseEntity<String>(returnString, HttpStatus.OK);
			}

			product.getCategories().remove(category);
			productService.update(product);

			resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);


		} catch (Exception e) {
			LOGGER.error("Error while deleting category", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}

		String returnString = resp.toJSONString();

		return new ResponseEntity<String>(returnString, HttpStatus.OK);
	}


	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value = "/admin/products/addProductToCategories.html", method = RequestMethod.POST)
	public String addProductToCategory(@RequestParam("productId") String productId, @RequestParam("uuid") String categoryId, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		setMenu(model, request);
		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);
		LocaleItem language = (LocaleItem) request.getAttribute("LANGUAGE");


		//get the product and validate it belongs to the current merchant
		ProductItem product = productService.getByUUID(UUID.fromString(productId));

		if (product == null) {
			return "redirect:/admin/products/products.html";
		}

		if (!product.getMerchantStore().getUuid().equals(store.getUuid())) {
			return "redirect:/admin/products/products.html";
		}


		//get parent categories
		List<CategoryItem> categories = categoryService.listByStore(store, language);

		CategoryItem category = categoryService.getByUUID(UUID.fromString(categoryId));

		if (category == null) {
			return "redirect:/admin/products/products.html";
		}

		if (!category.getMerchantStore().getUuid().equals(store.getUuid())) {
			return "redirect:/admin/products/products.html";
		}

		product.setCategories(Sets.newHashSet(category));

		productService.update(product);

		model.addAttribute("categoryId", category.getUuid());
		model.addAttribute("product", product);
		model.addAttribute("categories", categories);

		return "catalogue-product-categories";

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