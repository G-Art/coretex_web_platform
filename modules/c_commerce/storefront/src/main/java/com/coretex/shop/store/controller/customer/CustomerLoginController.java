package com.coretex.shop.store.controller.customer;

import com.coretex.core.business.exception.ConversionException;
import com.coretex.core.business.services.catalog.product.PricingService;
import com.coretex.core.business.services.reference.language.LanguageService;
import com.coretex.core.business.services.shoppingcart.ShoppingCartCalculationService;
import com.coretex.core.business.services.shoppingcart.ShoppingCartService;
import com.coretex.core.business.utils.ajax.AjaxResponse;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.ShoppingCartItem;
import com.coretex.items.core.LocaleItem;
import com.coretex.items.cx_core.CustomerItem;
import com.coretex.shop.constants.Constants;
import com.coretex.shop.model.customer.SecuredCustomer;
import com.coretex.shop.model.shoppingcart.ShoppingCartData;
import com.coretex.shop.populator.shoppingCart.ShoppingCartDataPopulator;
import com.coretex.shop.store.controller.AbstractController;
import com.coretex.shop.store.controller.customer.facade.CustomerFacade;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Custom Spring Security authentication
 *
 * @author Carl Samson
 */
@Controller
@RequestMapping("/shop/customer")
public class CustomerLoginController extends AbstractController {

	@Resource
	private LanguageService languageService;

	@Resource
	private CustomerFacade customerFacade;

	@Resource
	private ShoppingCartService shoppingCartService;

	@Resource
	private ShoppingCartCalculationService shoppingCartCalculationService;

	@Resource
	private PricingService pricingService;

	private ShoppingCartDataPopulator shoppingCartDataPopulator;


	private static final Logger LOG = LoggerFactory.getLogger(CustomerLoginController.class);

	@PostConstruct
	private void init(){
		 shoppingCartDataPopulator = new ShoppingCartDataPopulator();
		shoppingCartDataPopulator.setShoppingCartCalculationService(shoppingCartCalculationService);
		shoppingCartDataPopulator.setLanguageService(languageService);
		shoppingCartDataPopulator.setPricingService(pricingService);
	}


	private AjaxResponse logon(String userName, String password, String storeCode, HttpServletRequest request, HttpServletResponse response) throws Exception {

		AjaxResponse jsonObject = new AjaxResponse();


		try {

			LOG.debug("Authenticating user " + userName);

			//user goes to shop filter first so store and language are set
			MerchantStoreItem store = (MerchantStoreItem) request.getAttribute(Constants.MERCHANT_STORE);
			LocaleItem language = (LocaleItem) request.getAttribute("LANGUAGE");

			//check if username is from the appropriate store
			CustomerItem customerModel = customerFacade.getCustomerByUserName(userName, store);
			if (customerModel == null) {
				jsonObject.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
				return jsonObject;
			}

			if (!customerModel.getStore().getCode().equals(storeCode)) {
				jsonObject.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
				return jsonObject;
			}

			customerFacade.authenticate(customerModel, userName, password);
			//set customer in the http session
			super.setSessionAttribute(Constants.CUSTOMER, customerModel, request);
			jsonObject.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
			jsonObject.addEntry(Constants.RESPONSE_KEY_USERNAME, customerModel.getEmail());


			LOG.info("Fetching and merging Shopping Cart data");
			String sessionShoppingCartCode = (String) request.getSession().getAttribute(Constants.SHOPPING_CART);
			if (!StringUtils.isBlank(sessionShoppingCartCode)) {
				ShoppingCartItem shoppingCart = customerFacade.mergeCart(customerModel, sessionShoppingCartCode, store, language);
				if (shoppingCart != null) {
					ShoppingCartData shoppingCartData = this.populateShoppingCartData(shoppingCart, store, language);
					if (shoppingCartData != null) {
						jsonObject.addEntry(Constants.SHOPPING_CART, shoppingCartData.getCode());
						request.getSession().setAttribute(Constants.SHOPPING_CART, shoppingCartData.getCode());

						//set cart in the cookie
						Cookie c = new Cookie(Constants.COOKIE_NAME_CART, shoppingCartData.getCode());
						c.setMaxAge(60 * 24 * 3600);
						c.setPath(Constants.SLASH);
						response.addCookie(c);

					} else {
						//DELETE COOKIE
						Cookie c = new Cookie(Constants.COOKIE_NAME_CART, "");
						c.setMaxAge(0);
						c.setPath(Constants.SLASH);
						response.addCookie(c);
					}
				}


			} else {

				ShoppingCartItem cartModel = shoppingCartService.getByCustomer(customerModel);
				if (cartModel != null) {
					jsonObject.addEntry(Constants.SHOPPING_CART, cartModel.getShoppingCartCode());
					request.getSession().setAttribute(Constants.SHOPPING_CART, cartModel.getShoppingCartCode());

					Cookie c = new Cookie(Constants.COOKIE_NAME_CART, cartModel.getShoppingCartCode());
					c.setMaxAge(60 * 24 * 3600);
					c.setPath(Constants.SLASH);
					response.addCookie(c);

				}


			}

			StringBuilder cookieValue = new StringBuilder();
			cookieValue.append(store.getCode()).append("_").append(customerModel.getEmail());

			//set username in the cookie
			Cookie c = new Cookie(Constants.COOKIE_NAME_USER, cookieValue.toString());
			c.setMaxAge(60 * 24 * 3600);
			c.setPath(Constants.SLASH);
			response.addCookie(c);


		} catch (AuthenticationException ex) {
			jsonObject.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		} catch (Exception e) {
			jsonObject.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
		}

		return jsonObject;


	}

	//http://localhost:8080/sm-shop/shop/customer/authenticate.html?userName=shopizer&password=password&storeCode=DEFAULT
	@RequestMapping(value = "/authenticate.html", method = RequestMethod.GET)
	public @ResponseBody
	String basicLogon(@RequestParam String userName, @RequestParam String password, @RequestParam String storeCode, HttpServletRequest request, HttpServletResponse response) throws Exception {

		AjaxResponse jsonObject = this.logon(userName, password, storeCode, request, response);
		return jsonObject.toJSONString();

	}

	/**
	 * CustomerItem login entry point
	 *
	 * @param securedCustomer
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/logon.html", method = RequestMethod.POST)
	public @ResponseBody
	String jsonLogon(@ModelAttribute SecuredCustomer securedCustomer, HttpServletRequest request, HttpServletResponse response) throws Exception {

		AjaxResponse jsonObject = this.logon(securedCustomer.getUserName(), securedCustomer.getPassword(), securedCustomer.getStoreCode(), request, response);
		return jsonObject.toJSONString();


	}


	private ShoppingCartData populateShoppingCartData(final ShoppingCartItem cartModel, final MerchantStoreItem store, final LocaleItem language) {
		try {
			return shoppingCartDataPopulator.populate(cartModel, store, language);
		} catch (ConversionException ce) {
			LOG.error("Error in converting shopping cart to shopping cart data", ce);

		}
		return null;
	}

}
