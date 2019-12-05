package com.coretex.shop.store.controller.customer;

import com.coretex.core.business.exception.ConversionException;

import com.coretex.core.business.services.catalog.product.PricingService;
import com.coretex.core.business.services.reference.country.CountryService;
import com.coretex.core.business.services.reference.language.LanguageService;
import com.coretex.core.business.services.reference.zone.ZoneService;
import com.coretex.core.business.services.shoppingcart.ShoppingCartCalculationService;
import com.coretex.core.business.services.system.EmailService;
import com.coretex.core.business.utils.CoreConfiguration;
import com.coretex.items.commerce_core_model.CustomerItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.core.CountryItem;
import com.coretex.items.core.LocaleItem;
import com.coretex.items.commerce_core_model.ZoneItem;
import com.coretex.items.commerce_core_model.ShoppingCartItem;
import com.coretex.shop.constants.ApplicationConstants;
import com.coretex.shop.constants.Constants;
import com.coretex.shop.model.customer.AnonymousCustomer;
import com.coretex.shop.model.customer.CustomerEntity;
import com.coretex.shop.model.customer.SecuredShopPersistableCustomer;
import com.coretex.shop.model.shoppingcart.ShoppingCartData;
import com.coretex.shop.populator.shoppingCart.ShoppingCartDataPopulator;
import com.coretex.shop.store.controller.AbstractController;
import com.coretex.shop.store.controller.ControllerConstants;
import com.coretex.shop.store.controller.customer.facade.CustomerFacade;
import com.coretex.shop.utils.CaptchaRequestUtils;
import com.coretex.shop.utils.EmailTemplatesUtils;
import com.coretex.shop.utils.ImageFilePath;
import com.coretex.shop.utils.LabelUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

//import com.coretex.core.business.customer.CustomerRegistrationException;

/**
 * Registration of a new customer
 *
 * @author Carl Samson
 */

@SuppressWarnings("deprecation")
// http://stackoverflow.com/questions/17444258/how-to-use-new-passwordencoder-from-spring-security
@Controller
@RequestMapping("/shop/customer")
public class CustomerRegistrationController extends AbstractController {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerRegistrationController.class);


	@Resource
	private CoreConfiguration coreConfiguration;

	@Resource
	private LanguageService languageService;


	@Resource
	private CountryService countryService;


	@Resource
	private ZoneService zoneService;

	@Resource
	private PasswordEncoder passwordEncoder;

	@Resource
	EmailService emailService;

	@Resource
	private LabelUtils messages;

	@Resource
	private CustomerFacade customerFacade;

	@Resource
	private AuthenticationManager customerAuthenticationManager;

	@Resource
	private EmailTemplatesUtils emailTemplatesUtils;

	@Resource
	private CaptchaRequestUtils captchaRequestUtils;

	@Resource
	@Qualifier("img")
	private ImageFilePath imageUtils;

	@Resource
	private ShoppingCartCalculationService shoppingCartCalculationService;

	@Resource
	private PricingService pricingService;

	private ShoppingCartDataPopulator shoppingCartDataPopulator;

	@PostConstruct
	private void init(){
		shoppingCartDataPopulator = new ShoppingCartDataPopulator();
		shoppingCartDataPopulator.setShoppingCartCalculationService(shoppingCartCalculationService);
		shoppingCartDataPopulator.setLanguageService(languageService);
		shoppingCartDataPopulator.setPricingService(pricingService);
	}



	@RequestMapping(value = "/registration.html", method = RequestMethod.GET)
	public String displayRegistration(final Model model, final HttpServletRequest request, final HttpServletResponse response) throws Exception {

		MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.MERCHANT_STORE);

		model.addAttribute("recapatcha_public_key", coreConfiguration.getProperty(ApplicationConstants.RECAPTCHA_PUBLIC_KEY));

		SecuredShopPersistableCustomer customer = new SecuredShopPersistableCustomer();
		AnonymousCustomer anonymousCustomer = (AnonymousCustomer) request.getAttribute(Constants.ANONYMOUS_CUSTOMER);
		if (anonymousCustomer != null) {
			customer.setBilling(anonymousCustomer.getBilling());
		}

		model.addAttribute("customer", customer);

		/** template **/
		StringBuilder template = new StringBuilder().append(ControllerConstants.Tiles.Customer.register).append(".").append(store.getStoreTemplate());

		return template.toString();


	}

	@RequestMapping(value = "/register.html", method = RequestMethod.POST)
	public String registerCustomer(@Valid
								   @ModelAttribute("customer") SecuredShopPersistableCustomer customer, BindingResult bindingResult, Model model,
								   HttpServletRequest request, HttpServletResponse response, final Locale locale)
			throws Exception {
		MerchantStoreItem merchantStore = (MerchantStoreItem) request.getAttribute(Constants.MERCHANT_STORE);
		LocaleItem language = super.getLanguage(request);

		String userName = null;
		String password = null;

		model.addAttribute("recapatcha_public_key", coreConfiguration.getProperty(ApplicationConstants.RECAPTCHA_PUBLIC_KEY));

		if (!StringUtils.isBlank(request.getParameter("g-recaptcha-response"))) {
			boolean validateCaptcha = captchaRequestUtils.checkCaptcha(request.getParameter("g-recaptcha-response"));

			if (!validateCaptcha) {
				LOGGER.debug("Captcha response does not matched");
				FieldError error = new FieldError("captchaChallengeField", "captchaChallengeField", messages.getMessage("validaion.recaptcha.not.matched", locale));
				bindingResult.addError(error);
			}
		}


		if (StringUtils.isNotBlank(customer.getUserName())) {
			if (customerFacade.checkIfUserExists(customer.getUserName(), merchantStore)) {
				LOGGER.debug("CustomerItem with username {} already exists for this store ", customer.getUserName());
				FieldError error = new FieldError("userName", "userName", messages.getMessage("registration.username.already.exists", locale));
				bindingResult.addError(error);
			}
			userName = customer.getUserName();
		}


		if (StringUtils.isNotBlank(customer.getPassword()) && StringUtils.isNotBlank(customer.getCheckPassword())) {
			if (!customer.getPassword().equals(customer.getCheckPassword())) {
				FieldError error = new FieldError("password", "password", messages.getMessage("message.password.checkpassword.identical", locale));
				bindingResult.addError(error);

			}
			password = customer.getPassword();
		}

		if (bindingResult.hasErrors()) {
			LOGGER.debug("found {} validation error while validating in customer registration ",
					bindingResult.getErrorCount());
			StringBuilder template =
					new StringBuilder().append(ControllerConstants.Tiles.Customer.register).append(".").append(merchantStore.getStoreTemplate());
			return template.toString();

		}

		@SuppressWarnings("unused")
		CustomerEntity customerData = null;
		try {
			//set user clear password
			customer.setClearPassword(password);
			customerData = customerFacade.registerCustomer(customer, merchantStore, language);
		}
       /* catch ( CustomerRegistrationException cre )
        {
            LOGGER.error( "Error while registering customer.. ", cre);
        	ObjectError error = new ObjectError("registration",messages.getMessage("registration.failed", locale));
        	bindingResult.addError(error);
            StringBuilder template =
                            new StringBuilder().append( ControllerConstants.Tiles.CustomerItem.register ).append( "." ).append( merchantStore.getStoreTemplate() );
             return template.toString();
        }*/ catch (Exception e) {
			LOGGER.error("Error while registering customer.. ", e);
			ObjectError error = new ObjectError("registration", messages.getMessage("registration.failed", locale));
			bindingResult.addError(error);
			StringBuilder template =
					new StringBuilder().append(ControllerConstants.Tiles.Customer.register).append(".").append(merchantStore.getStoreTemplate());
			return template.toString();
		}


		try {

			/**
			 * Send registration email
			 */
			emailTemplatesUtils.sendRegistrationEmail(customer, merchantStore, locale, request.getContextPath());

		} catch (Exception e) {

			LOGGER.error("Cannot send email to customer ", e);

		}

		/**
		 * Login user
		 */

		try {

			//refresh customer
			CustomerItem c = customerFacade.getCustomerByUserName(customer.getUserName(), merchantStore);
			//authenticate
			customerFacade.authenticate(c, userName, password);
			super.setSessionAttribute(Constants.CUSTOMER, c, request);

			StringBuilder cookieValue = new StringBuilder();
			cookieValue.append(merchantStore.getCode()).append("_").append(c.getLogin());

			//set username in the cookie
			Cookie cookie = new Cookie(Constants.COOKIE_NAME_USER, cookieValue.toString());
			cookie.setMaxAge(60 * 24 * 3600);
			cookie.setPath(Constants.SLASH);
			response.addCookie(cookie);


			String sessionShoppingCartCode = (String) request.getSession().getAttribute(Constants.SHOPPING_CART);
			if (!StringUtils.isBlank(sessionShoppingCartCode)) {
				ShoppingCartItem shoppingCart = customerFacade.mergeCart(c, sessionShoppingCartCode, merchantStore, language);
				ShoppingCartData shoppingCartData = this.populateShoppingCartData(shoppingCart, merchantStore, language);
				if (shoppingCartData != null) {
					request.getSession().setAttribute(Constants.SHOPPING_CART, shoppingCartData.getCode());
				}

				//set username in the cookie
				Cookie c1 = new Cookie(Constants.COOKIE_NAME_CART, shoppingCartData.getCode());
				c1.setMaxAge(60 * 24 * 3600);
				c1.setPath(Constants.SLASH);
				response.addCookie(c1);

			}

			return "redirect:/shop/customer/dashboard.html";


		} catch (Exception e) {
			LOGGER.error("Cannot authenticate user ", e);
			ObjectError error = new ObjectError("registration", messages.getMessage("registration.failed", locale));
			bindingResult.addError(error);
		}


		StringBuilder template =
				new StringBuilder().append(ControllerConstants.Tiles.Customer.register).append(".").append(merchantStore.getStoreTemplate());
		return template.toString();

	}


	@ModelAttribute("countryList")
	public List<CountryItem> getCountries(final HttpServletRequest request) {

		LocaleItem language = (LocaleItem) request.getAttribute("LANGUAGE");
		if (language == null) {
			language = (LocaleItem) request.getAttribute("LANGUAGE");
		}

		if (language == null) {
			language = languageService.getByCode(Constants.DEFAULT_LANGUAGE);
		}

		return countryService.getCountries(language);
	}

	@ModelAttribute("zoneList")
	public List<ZoneItem> getZones(final HttpServletRequest request) {
		return zoneService.list();
	}


	private ShoppingCartData populateShoppingCartData(final ShoppingCartItem cartModel, final MerchantStoreItem store, final LocaleItem language) {

		try {
			return shoppingCartDataPopulator.populate(cartModel, store, language);
		} catch (ConversionException ce) {
			LOGGER.error("Error in converting shopping cart to shopping cart data", ce);

		}
		return null;
	}


}
