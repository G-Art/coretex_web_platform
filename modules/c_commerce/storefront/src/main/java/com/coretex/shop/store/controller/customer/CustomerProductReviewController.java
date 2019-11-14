package com.coretex.shop.store.controller.customer;

import com.coretex.core.business.services.catalog.product.PricingService;
import com.coretex.core.business.services.catalog.product.ProductService;
import com.coretex.core.business.services.catalog.product.review.ProductReviewService;
import com.coretex.core.business.services.customer.CustomerService;
import com.coretex.core.business.services.reference.language.LanguageService;
import com.coretex.items.commerce_core_model.ProductItem;
import com.coretex.items.commerce_core_model.ProductReviewItem;
import com.coretex.items.commerce_core_model.CustomerItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.shop.constants.Constants;
import com.coretex.shop.model.catalog.product.PersistableProductReview;
import com.coretex.shop.model.catalog.product.ReadableProduct;
import com.coretex.shop.model.catalog.product.ReadableProductReview;
import com.coretex.shop.populator.catalog.PersistableProductReviewPopulator;
import com.coretex.shop.populator.catalog.ReadableProductPopulator;
import com.coretex.shop.populator.catalog.ReadableProductReviewPopulator;
import com.coretex.shop.store.controller.AbstractController;
import com.coretex.shop.store.controller.ControllerConstants;
import com.coretex.shop.store.controller.customer.facade.CustomerFacade;
import com.coretex.shop.utils.DateUtil;
import com.coretex.shop.utils.ImageFilePath;
import com.coretex.shop.utils.LabelUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * Entry point for logged in customers
 *
 * @author Carl Samson
 */
@Controller
@RequestMapping(Constants.SHOP_URI + "/customer")
public class CustomerProductReviewController extends AbstractController {

	@Resource
	private ProductService productService;

	@Resource
	private LanguageService languageService;

	@Resource
	private PricingService pricingService;

	@Resource
	private ProductReviewService productReviewService;

	@Resource
	private CustomerService customerService;

	@Resource
	private CustomerFacade customerFacade;

	@Resource
	private LabelUtils messages;

	@Resource
	@Qualifier("img")
	private ImageFilePath imageUtils;

	@PreAuthorize("hasRole('AUTH_CUSTOMER')")
	@RequestMapping(value = "/review.html", method = RequestMethod.GET)
	public String displayProductReview(@RequestParam String productId, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {


		MerchantStoreItem store = getSessionAttribute(Constants.MERCHANT_STORE, request);
		LanguageItem language = super.getLanguage(request);


		//get product
		ProductItem product = productService.getById(UUID.fromString(productId));
		if (product == null) {
			return "redirect:" + Constants.SHOP_URI;
		}

		if (!product.getMerchantStore().getUuid().equals(store.getUuid())) {
			return "redirect:" + Constants.SHOP_URI;
		}


		//create readable product
		ReadableProduct readableProduct = new ReadableProduct();
		ReadableProductPopulator readableProductPopulator = new ReadableProductPopulator();
		readableProductPopulator.setPricingService(pricingService);
		readableProductPopulator.setimageUtils(imageUtils);
		readableProductPopulator.populate(product, readableProduct, store, language);
		model.addAttribute("product", readableProduct);


		CustomerItem customer = customerFacade.getCustomerByUserName(request.getRemoteUser(), store);

		List<ProductReviewItem> reviews = productReviewService.getByProduct(product, language);
		for (ProductReviewItem r : reviews) {
			if (r.getCustomer().getUuid().equals(customer.getUuid())) {

				ReadableProductReviewPopulator reviewPopulator = new ReadableProductReviewPopulator();
				ReadableProductReview rev = new ReadableProductReview();
				reviewPopulator.populate(r, rev, store, language);

				model.addAttribute("customerReview", rev);
				break;
			}
		}


		ProductReviewItem review = new ProductReviewItem();
		review.setCustomer(customer);
		review.setProduct(product);

		ReadableProductReview productReview = new ReadableProductReview();
		ReadableProductReviewPopulator reviewPopulator = new ReadableProductReviewPopulator();
		reviewPopulator.populate(review, productReview, store, language);

		model.addAttribute("review", productReview);
		model.addAttribute("reviews", reviews);


		/** template **/
		StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Customer.review).append(".").append(store.getStoreTemplate());

		return template.toString();

	}


	@PreAuthorize("hasRole('AUTH_CUSTOMER')")
	@RequestMapping(value = "/review/submit.html", method = RequestMethod.POST)
	public String submitProductReview(@ModelAttribute("review") PersistableProductReview review, BindingResult bindingResult, Model model, HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception {


		MerchantStoreItem store = getSessionAttribute(Constants.MERCHANT_STORE, request);
		LanguageItem language = getLanguage(request);

		CustomerItem customer = customerFacade.getCustomerByUserName(request.getRemoteUser(), store);

		if (customer == null) {
			return "redirect:" + Constants.SHOP_URI;
		}


		ProductItem product = productService.getById(review.getProductId());
		if (product == null) {
			return "redirect:" + Constants.SHOP_URI;
		}

		if (StringUtils.isBlank(review.getDescription())) {
			FieldError error = new FieldError("description", "description", messages.getMessage("NotEmpty.review.description", locale));
			bindingResult.addError(error);
		}

		if (review.getRating() == null) {
			FieldError error = new FieldError("rating", "rating", messages.getMessage("NotEmpty.review.rating", locale, "ProductItem rating is required"));
			bindingResult.addError(error);
		}


		ReadableProduct readableProduct = new ReadableProduct();
		ReadableProductPopulator readableProductPopulator = new ReadableProductPopulator();
		readableProductPopulator.setPricingService(pricingService);
		readableProductPopulator.setimageUtils(imageUtils);
		readableProductPopulator.populate(product, readableProduct, store, language);
		model.addAttribute("product", readableProduct);


		/** template **/
		StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Customer.review).append(".").append(store.getStoreTemplate());

		if (bindingResult.hasErrors()) {

			return template.toString();

		}


		//check if customer has already evaluated the product
		List<ProductReviewItem> reviews = productReviewService.getByProduct(product);

		for (ProductReviewItem r : reviews) {
			if (r.getCustomer().getUuid().equals(customer.getUuid())) {
				ReadableProductReviewPopulator reviewPopulator = new ReadableProductReviewPopulator();
				ReadableProductReview rev = new ReadableProductReview();
				reviewPopulator.populate(r, rev, store, language);

				model.addAttribute("customerReview", rev);
				return template.toString();
			}
		}


		PersistableProductReviewPopulator populator = new PersistableProductReviewPopulator();
		populator.setCustomerService(customerService);
		populator.setLanguageService(languageService);
		populator.setProductService(productService);

		review.setDate(DateUtil.formatDate(new Date()));
		review.setCustomerId(customer.getUuid());

		ProductReviewItem productReview = populator.populate(review, store, language);
		productReviewService.create(productReview);

		model.addAttribute("review", review);
		model.addAttribute("success", "success");

		ReadableProductReviewPopulator reviewPopulator = new ReadableProductReviewPopulator();
		ReadableProductReview rev = new ReadableProductReview();
		reviewPopulator.populate(productReview, rev, store, language);

		model.addAttribute("customerReview", rev);

		return template.toString();

	}


}
