package com.coretex.shop.admin.controller.products;

import com.coretex.core.business.services.catalog.product.manufacturer.ManufacturerService;
import com.coretex.core.business.services.reference.language.LanguageService;
import com.coretex.core.business.utils.CoreConfiguration;
import com.coretex.core.business.utils.ajax.AjaxPageableResponse;
import com.coretex.core.business.utils.ajax.AjaxResponse;
import com.coretex.items.core.LocaleItem;
import com.coretex.items.commerce_core_model.ManufacturerItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.shop.admin.controller.ControllerConstants;
import com.coretex.shop.admin.controller.customers.CustomerController;
import com.coretex.shop.admin.model.catalog.Manufacturer;
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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

@Controller
public class ManufacturerController {

	@Resource
	private LanguageService languageService;

	@Resource
	private ManufacturerService manufacturerService;

	@Resource
	LabelUtils messages;

	@Resource
	private CoreConfiguration configuration;

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);

	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value = "/admin/catalogue/manufacturer/list.html", method = RequestMethod.GET)
	public String getManufacturers(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		this.setMenu(model, request);

		return ControllerConstants.Tiles.Product.manufacturerList;
	}


	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value = "/admin/catalogue/manufacturer/create.html", method = RequestMethod.GET)
	public String createManufacturer(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		return displayManufacturer(null, model, request, response);
	}

	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value = "/admin/catalogue/manufacturer/edit.html", method = RequestMethod.GET)
	public String editManufacturer(@RequestParam("id") String manufacturerId, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		return displayManufacturer(UUID.fromString(manufacturerId), model, request, response);
	}

	private String displayManufacturer(UUID manufacturerId, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		//display menu
		setMenu(model, request);

		//List<LocaleItem> languages = languageService.getLanguages();
		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);
		List<LocaleItem> languages = store.getLanguages();


		Manufacturer manufacturer = new Manufacturer();


		if (manufacturerId != null) {    //edit mode

			ManufacturerItem dbManufacturer;
			dbManufacturer = manufacturerService.getByUUID(manufacturerId);

			if (dbManufacturer == null) {
				return ControllerConstants.Tiles.Product.manufacturerList;
			}

			if (!dbManufacturer.getMerchantStore().getUuid().equals(store.getUuid())) {
				return ControllerConstants.Tiles.Product.manufacturerList;
			}


			manufacturer.setManufacturer(dbManufacturer);

			manufacturer.setCode(dbManufacturer.getCode());
			manufacturer.setOrder(dbManufacturer.getOrder());

		} else {    // Create mode

			ManufacturerItem manufacturerTmp = new ManufacturerItem();
			manufacturer.setManufacturer(manufacturerTmp);

		}

		model.addAttribute("languages", languages);
		model.addAttribute("manufacturer", manufacturer);

		return ControllerConstants.Tiles.Product.manufacturerDetails;
	}

	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value = "/admin/catalogue/manufacturer/save.html", method = RequestMethod.POST)
	public String saveManufacturer(@Valid @ModelAttribute("manufacturer") Manufacturer manufacturer, BindingResult result, Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {

		this.setMenu(model, request);
		//save or edit a manufacturer

		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);
		List<LocaleItem> languages = languageService.getLanguages();

		//validate image
		if (manufacturer.getImage() != null && !manufacturer.getImage().isEmpty()) {

			try {

				String maxHeight = configuration.getProperty("PRODUCT_IMAGE_MAX_HEIGHT_SIZE");
				String maxWidth = configuration.getProperty("PRODUCT_IMAGE_MAX_WIDTH_SIZE");
				String maxSize = configuration.getProperty("PRODUCT_IMAGE_MAX_SIZE");

				BufferedImage image = ImageIO.read(manufacturer.getImage().getInputStream());

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
					if (manufacturer.getImage().getSize() > maxImageSize) {
						ObjectError error = new ObjectError("image", messages.getMessage("message.image.size", locale) + " {" + maxSize + "}");
						result.addError(error);
					}
				}

			} catch (Exception e) {
				LOGGER.error("Cannot validate manufacturer image", e);
			}

		}

		if (result.hasErrors()) {
			model.addAttribute("languages", languages);
			return ControllerConstants.Tiles.Product.manufacturerDetails;
		}

		ManufacturerItem newManufacturer = manufacturer.getManufacturer();

		if (manufacturer.getManufacturer().getUuid() != null) {

			newManufacturer = manufacturerService.getByUUID(manufacturer.getManufacturer().getUuid());

			if (!newManufacturer.getMerchantStore().getUuid().equals(store.getUuid())) {
				return ControllerConstants.Tiles.Product.manufacturerList;
			}

		}

//		for(ManufacturerImage image : manufacturer.getImages()) {
//			if(image.isDefaultImage()) {
//				manufacturer.setProductImage(image);
//			}
//		}

		newManufacturer.setOrder(manufacturer.getOrder());
		newManufacturer.setMerchantStore(store);
		newManufacturer.setCode(manufacturer.getCode());


//		if(manufacturer.getManufacturerImage()!=null && manufacturer.getManufacturerImage().getUuid() == null) {
//			newManufacturer.setProductImage(null);
//		}


//		if (manufacturer.getImage() != null && !manufacturer.getImage().isEmpty()) {
//
//			String imageName = manufacturer.getImage().getOriginalFilename();
//
//			ManufacturerImage manufacturerImage = new ManufacturerImage();
//			manufacturerImage.setDefaultImage(true);
//			manufacturerImage.setImage(manufacturer.getImage().getInputStream());
//			manufacturerImage.setManufacturerImage(imageName);
//
//			List<ManufacturerImageDescription> imagesDescriptions = new ArrayList<ManufacturerImageDescription>();
//
//			for(LocaleItem l : languages) {
//
//				ManufacturerImageDescription imageDescription = new ManufacturerImageDescription();
//				imageDescription.setName(imageName);
//				imageDescription.setLanguage(l);
//				imageDescription.setManufacturerImage(productImage);
//				imagesDescriptions.add(imageDescription);
//
//			}
//
//			manufacturerImage.setDescriptions(imagesDescriptions);
//			manufacturerImage.setProduct(newManufacturer);
//
//			newManufacturer.getImages().add(manufacturerImage);
//
//			manufacturerService.saveOrUpdate(newManufacturer);
//
//			//manufacturer displayed
//			manufacturer.setProductImage(manufacturerImage);


//		} else {

		manufacturerService.saveOrUpdate(newManufacturer);
//		}

		model.addAttribute("manufacturer", manufacturer);
		model.addAttribute("languages", languages);
		model.addAttribute("success", "success");

		return ControllerConstants.Tiles.Product.manufacturerDetails;

	}


	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value = "/admin/catalogue/manufacturer/paging.html", method = RequestMethod.POST)
	public @ResponseBody
	ResponseEntity<String> pageManufacturers(HttpServletRequest request, HttpServletResponse response) {

		AjaxResponse resp = new AjaxResponse();
		try {

			LocaleItem language = (LocaleItem) request.getAttribute("LANGUAGE");
			MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);

			List<ManufacturerItem> manufacturers = null;
			manufacturers = manufacturerService.listByStore(store, language);


			for (ManufacturerItem manufacturer : manufacturers) {

				@SuppressWarnings("rawtypes")
				Map entry = new HashMap();
				entry.put("id", manufacturer.getUuid());

				entry.put("name", manufacturer.getName());
				entry.put("code", manufacturer.getCode());
				entry.put("order", manufacturer.getOrder());
				resp.addDataEntry(entry);

			}

			resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);

		} catch (Exception e) {
			LOGGER.error("Error while paging Manufacturers", e);
			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}

		resp.setStatus(AjaxPageableResponse.RESPONSE_STATUS_SUCCESS);

		String returnString = resp.toJSONString();

		return new ResponseEntity<String>(returnString, HttpStatus.OK);

	}

	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value = "/admin/catalogue/manufacturer/remove.html", method = RequestMethod.POST)
	public @ResponseBody
	ResponseEntity<String> deleteManufacturer(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		UUID sid = UUID.fromString(request.getParameter("id"));


		AjaxResponse resp = new AjaxResponse();
		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);


		try {
			ManufacturerItem delManufacturer = manufacturerService.getByUUID(sid);
			if (delManufacturer == null || !delManufacturer.getMerchantStore().getUuid().equals(store.getUuid())) {
				resp.setStatusMessage(messages.getMessage("message.unauthorized", locale));
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
				String returnString = resp.toJSONString();
				return new ResponseEntity<String>(returnString, HttpStatus.OK);
			}

			int count = manufacturerService.getCountManufAttachedProducts(delManufacturer).intValue();
			//IF already attached to products it can't be deleted
			if (count > 0) {
				resp.setStatusMessage(messages.getMessage("message.product.association", locale));
				resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
				String returnString = resp.toJSONString();
				return new ResponseEntity<String>(returnString, HttpStatus.OK);
			}

			manufacturerService.delete(delManufacturer);

			resp.setStatusMessage(messages.getMessage("message.success", locale));
			resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);

		} catch (Exception e) {

			resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
			LOGGER.error("Cannot delete manufacturer.", e);
		}

		String returnString = resp.toJSONString();
		return new ResponseEntity<String>(returnString, HttpStatus.OK);

	}


	@PreAuthorize("hasRole('PRODUCTS')")
	@RequestMapping(value = "/admin/manufacturer/checkCode.html", method = RequestMethod.POST)
	public @ResponseBody
	ResponseEntity<String> checkCode(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		String code = request.getParameter("code");
		String id = request.getParameter("id");



		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.ADMIN_STORE);


		AjaxResponse resp = new AjaxResponse();

		if (StringUtils.isBlank(code)) {
			resp.setStatus(AjaxResponse.CODE_ALREADY_EXIST);
			String returnString = resp.toJSONString();
			return new ResponseEntity<String>(returnString, HttpStatus.OK);
		}


		try {

			ManufacturerItem manufacturer = manufacturerService.getByCode(store, code);

			if (manufacturer != null && StringUtils.isBlank(id)) {
				resp.setStatus(AjaxResponse.CODE_ALREADY_EXIST);
				String returnString = resp.toJSONString();
				return new ResponseEntity<>(returnString, HttpStatus.OK);
			}


			if (manufacturer != null && !StringUtils.isBlank(id)) {
				try {
					UUID lid = UUID.fromString(id);

					if (manufacturer.getCode().equals(code) && manufacturer.getUuid().equals(lid)) {
						resp.setStatus(AjaxResponse.CODE_ALREADY_EXIST);
						String returnString = resp.toJSONString();
						return new ResponseEntity<String>(returnString, HttpStatus.OK);
					}
				} catch (Exception e) {
					resp.setStatus(AjaxResponse.CODE_ALREADY_EXIST);
					String returnString = resp.toJSONString();
					return new ResponseEntity<String>(returnString, HttpStatus.OK);
				}

			}


			resp.setStatus(AjaxResponse.RESPONSE_OPERATION_COMPLETED);

		} catch (Exception e) {
			LOGGER.error("Error while getting category", e);
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
		activeMenus.put("manufacturer-list", "manufacturer-list");

		@SuppressWarnings("unchecked")
		Map<String, Menu> menus = (Map<String, Menu>) request.getAttribute("MENUMAP");

		Menu currentMenu = menus.get("catalogue");
		model.addAttribute("currentMenu", currentMenu);
		model.addAttribute("activeMenus", activeMenus);
	}

}
