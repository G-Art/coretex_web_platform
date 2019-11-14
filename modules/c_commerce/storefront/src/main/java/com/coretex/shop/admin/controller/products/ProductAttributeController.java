package com.coretex.shop.admin.controller.products;

import com.coretex.core.business.services.catalog.product.ProductService;
import com.coretex.core.business.services.catalog.product.attribute.ProductAttributeService;
import com.coretex.core.business.services.catalog.product.attribute.ProductOptionService;
import com.coretex.core.business.services.catalog.product.attribute.ProductOptionValueService;
import com.coretex.core.business.utils.ProductPriceUtils;
import com.coretex.core.business.utils.ajax.AjaxPageableResponse;
import com.coretex.core.business.utils.ajax.AjaxResponse;
import com.coretex.items.commerce_core_model.ProductOptionItem;
import com.coretex.items.commerce_core_model.ProductOptionValueItem;
import com.coretex.items.commerce_core_model.ProductAttributeItem;
import com.coretex.items.commerce_core_model.ProductItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.core.data.web.Menu;
import com.coretex.shop.constants.Constants;
import com.coretex.shop.utils.LabelUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.*;

@Controller
public class ProductAttributeController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductAttributeController.class);

	private final static String TEXT_OPTION = "text";

	@Resource
	private ProductAttributeService productAttributeService;

	@Resource
	private ProductService productService;

	@Resource
	private ProductPriceUtils priceUtil;

	@Resource
	ProductOptionService productOptionService;

	@Resource
	ProductOptionValueService productOptionValueService;

	@Resource
	LabelUtils messages;


	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value = "/admin/products/attributes/list.html", method = RequestMethod.GET)
	public String displayProductAttributes(@RequestParam("id") String productId, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {


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
		return "admin-products-attributes";

	}


	@SuppressWarnings({"rawtypes", "unchecked"})
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value = "/admin/products/attributes/page.html", method = RequestMethod.POST)
	public @ResponseBody
	ResponseEntity<String> pageAttributes(HttpServletRequest request, HttpServletResponse response) {

		//String attribute = request.getParameter("attribute");
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


			LanguageItem language = (LanguageItem) request.getAttribute("LANGUAGE");
			MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);

			//List<ProductAttributeItem> attributes = productAttributeService.getByProductId(store, product, language);

			for (ProductAttributeItem attr : product.getAttributes()) {

				Map entry = new HashMap();
				entry.put("attributeId", attr.getUuid());


				entry.put("attribute", attr.getProductOption().getName());
				entry.put("display", attr.getAttributeDisplayOnly());
				entry.put("value", attr.getProductOptionValue().getName());
				entry.put("order", attr.getProductOptionSortOrder());
				entry.put("price", priceUtil.getAdminFormatedAmountWithCurrency(store, attr.getProductAttributePrice()));

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
	@RequestMapping(value = "/admin/products/attributes/editAttribute.html", method = RequestMethod.GET)
	public String displayAttributeEdit(@RequestParam("productId") String productId, @RequestParam("id") String id, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return displayAttribute(UUID.fromString(productId), UUID.fromString(id), model, request, response);

	}

	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value = "/admin/products/attribute/createAttribute.html", method = RequestMethod.GET)
	public String displayAttributeCreate(@RequestParam("productId") String productId, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return displayAttribute(UUID.fromString(productId), null, model, request, response);

	}

	private String displayAttribute(UUID productId, UUID id, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {


		//display menu
		setMenu(model, request);


		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);
		LanguageItem language = (LanguageItem) request.getAttribute("LANGUAGE");

		//get product
		ProductItem product = productService.getById(productId);
		if (!product.getMerchantStore().getUuid().equals(store.getUuid())) {
			return "redirect:/admin/products/products.html";
		}

		List<LanguageItem> languages = store.getLanguages();

		ProductAttributeItem attribute = null;

		//get Options
		List<ProductOptionItem> options = productOptionService.listByStore(store, language);
		//get OptionsValues
		List<ProductOptionValueItem> optionsValues = productOptionValueService.listByStoreNoReadOnly(store, language);

		if (id != null) {//edit mode

			attribute = productAttributeService.getById(id);
			attribute.setAttributePrice(priceUtil.getAdminFormatedAmount(attribute.getProductAttributePrice()));
			attribute.setAttributeAdditionalWeight(String.valueOf(attribute.getProductAttributeWeight().intValue()));
			attribute.setAttributeSortOrder(String.valueOf(attribute.getProductOptionSortOrder()));

		} else {

			attribute = new ProductAttributeItem();
			attribute.setProduct(product);
			ProductOptionValueItem value = new ProductOptionValueItem();
			attribute.setProductOptionValue(value);
		}

		model.addAttribute("optionsValues", optionsValues);
		model.addAttribute("options", options);
		model.addAttribute("attribute", attribute);
		model.addAttribute("product", product);
		return "admin-products-attribute-details";
	}

	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value = "/admin/attributes/attribute/save.html", method = RequestMethod.POST)
	public String saveAttribute(@Valid @ModelAttribute("attribute") ProductAttributeItem attribute, BindingResult result, Model model, HttpServletRequest request, Locale locale) throws Exception {


		//display menu
		setMenu(model, request);

		ProductItem product = productService.getById(attribute.getProduct().getUuid());

		model.addAttribute("product", product);


		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);
		LanguageItem language = (LanguageItem) request.getAttribute("LANGUAGE");


		//get Options
		List<ProductOptionItem> options = productOptionService.listByStore(store, language);
		//get OptionsValues
		List<ProductOptionValueItem> optionsValues = productOptionValueService.listByStoreNoReadOnly(store, language);

		model.addAttribute("optionsValues", optionsValues);
		model.addAttribute("options", options);

		ProductAttributeItem dbEntity = null;

		if (attribute.getUuid() != null) { //edit entry

			//get from DB
			dbEntity = productAttributeService.getById(attribute.getUuid());

			if (dbEntity == null) {
				return "redirect:/admin/products/attributes/list.html";
			}

			if (!dbEntity.getProductOption().getMerchantStore().getUuid().equals(store.getUuid())) {
				return "redirect:/admin/products/attributes/list.html";
			}
		}

		//validate price
		BigDecimal submitedPrice = null;
		try {
			submitedPrice = priceUtil.getAmount(attribute.getAttributePrice());
			attribute.setProductAttributePrice(submitedPrice);
		} catch (Exception e) {
			ObjectError error = new ObjectError("attributePrice", messages.getMessage("NotEmpty.product.productPrice", locale));
			result.addError(error);
		}

		//validate sort order
		try {
			Integer sortOrder = Integer.parseInt(attribute.getAttributeSortOrder());
			attribute.setProductOptionSortOrder(sortOrder);
		} catch (Exception e) {
			ObjectError error = new ObjectError("attributeSortOrder", messages.getMessage("message.number.invalid", locale));
			result.addError(error);
		}

		//validate weight
		try {
			Integer weight = Integer.parseInt(attribute.getAttributeAdditionalWeight());
			attribute.setProductAttributeWeight(new BigDecimal(weight));
		} catch (Exception e) {
			ObjectError error = new ObjectError("attributeAdditionalWeight", messages.getMessage("message.number.invalid", locale));
			result.addError(error);
		}

		if (attribute.getProductOption() == null) {
			ObjectError error = new ObjectError("productOption.id", messages.getMessage("message.productoption.required", locale));
			result.addError(error);
			return "admin-products-attribute-details";
		}


		//check type
		ProductOptionItem option = attribute.getProductOption();
		option = productOptionService.getById(option.getUuid());
		attribute.setProductOption(option);

		if (option.getProductOptionType().equals(TEXT_OPTION)) {

			if (dbEntity != null && dbEntity.getProductOption().getProductOptionType().equals(TEXT_OPTION)) {//bcz it is overwrited by hidden product option value list
				if (dbEntity.getProductOptionValue() != null) {
					ProductOptionValueItem optVal = dbEntity.getProductOptionValue();
					optVal.setProductOptionDisplayOnly(true);
					productOptionValueService.saveOrUpdate(optVal);
					attribute.setProductOptionValue(optVal);
				}
			} else {//create a new value

				//create new option value
				ProductOptionValueItem newValue = new ProductOptionValueItem();
				//code generation
				String code = RandomStringUtils.randomAlphanumeric(10).toUpperCase();
				newValue.setCode(code);
				newValue.setMerchantStore(store);
				newValue.setProductOptionValueSortOrder(attribute.getProductOptionValue().getProductOptionValueSortOrder());
				newValue.setProductOptionDisplayOnly(true);
				productOptionValueService.save(newValue);
				attribute.setProductOptionValue(newValue);
				attribute.setAttributeDisplayOnly(true);

			}

		}


		if (attribute.getProductOptionValue().getUuid() == null) {
			ObjectError error = new ObjectError("productOptionValue.id", messages.getMessage("message.productoptionvalue.required", locale));
			result.addError(error);
		}

		model.addAttribute("attribute", attribute);


		if (result.hasErrors()) {
			return "admin-products-attribute-details";
		}

		productAttributeService.saveOrUpdate(attribute);

		model.addAttribute("success", "success");
		return "admin-products-attribute-details";
	}

	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value = "/admin/attributes/attribute/remove.html", method = RequestMethod.POST)
	public @ResponseBody
	ResponseEntity<String> deleteProductPrice(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		String sAttributeid = request.getParameter("attributeId");


		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);
		AjaxResponse resp = new AjaxResponse();



		try {

			UUID attributeId = UUID.fromString(sAttributeid);
			ProductAttributeItem attribute = productAttributeService.getById(attributeId);


			if (attribute == null || !attribute.getProduct().getMerchantStore().getUuid().equals(store.getUuid())) {

				resp.setStatusMessage(messages.getMessage("message.unauthorized", locale));
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
				String returnString = resp.toJSONString();
				return new ResponseEntity<String>(returnString, HttpStatus.OK);
			}


			productAttributeService.delete(attribute);


			resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);


		} catch (Exception e) {
			LOGGER.error("Error while deleting product price", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorMessage(e);
		}

		String returnString = resp.toJSONString();
		return new ResponseEntity<String>(returnString, HttpStatus.OK);
	}


	@SuppressWarnings({"rawtypes", "unchecked"})
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value = "/admin/products/attributes/getAttributeType.html", method = RequestMethod.POST)
	public @ResponseBody
	ResponseEntity<String> checkAttributeType(HttpServletRequest request, HttpServletResponse response, Locale locale) {

		String sOptionId = request.getParameter("optionId");

		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);


		AjaxResponse resp = new AjaxResponse();



		UUID prodoptionId;
		ProductOptionItem productOption = null;

		try {
			prodoptionId = UUID.fromString(sOptionId);
		} catch (Exception e) {
			resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_FAIURE);
			resp.setErrorString("ProductItem Option id is not valid");
			String returnString = resp.toJSONString();
			return new ResponseEntity<String>(returnString, HttpStatus.OK);
		}


		try {


			productOption = productOptionService.getById(prodoptionId);

			if (productOption == null) {
				resp.setStatusMessage(messages.getMessage("message.unauthorized", locale));
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
				String returnString = resp.toJSONString();
				return new ResponseEntity<String>(returnString, HttpStatus.OK);
			}

			if (!productOption.getMerchantStore().getUuid().equals(store.getUuid())) {
				resp.setStatusMessage(messages.getMessage("message.unauthorized", locale));
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
				String returnString = resp.toJSONString();
				return new ResponseEntity<String>(returnString, HttpStatus.OK);
			}


			Map entry = new HashMap();


			entry.put("type", productOption.getProductOptionType());
			resp.addDataEntry(entry);
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
