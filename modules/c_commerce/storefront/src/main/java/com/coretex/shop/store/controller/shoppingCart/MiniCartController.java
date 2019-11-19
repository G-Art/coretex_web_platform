
package com.coretex.shop.store.controller.shoppingCart;

import com.coretex.items.commerce_core_model.CustomerItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.shop.constants.Constants;
import com.coretex.shop.model.shoppingcart.ShoppingCartData;
import com.coretex.shop.store.controller.AbstractController;
import com.coretex.shop.store.controller.shoppingCart.facade.ShoppingCartFacade;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.UUID;


@Controller
@RequestMapping("/shop/cart")
public class MiniCartController extends AbstractController {

	private static final Logger LOG = LoggerFactory.getLogger(MiniCartController.class);

	@Resource
	private ShoppingCartFacade shoppingCartFacade;


	@RequestMapping(value = {"/displayMiniCartByCode"}, method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/json")
	public @ResponseBody
	ShoppingCartData displayMiniCart(final String shoppingCartCode, HttpServletRequest request, Model model) {

		LanguageItem language = (LanguageItem) request.getAttribute(Constants.LANGUAGE);

		try {
			MerchantStoreItem merchantStore = (MerchantStoreItem) request.getAttribute(Constants.MERCHANT_STORE);
			CustomerItem customer = getSessionAttribute(Constants.CUSTOMER, request);
			ShoppingCartData cart = shoppingCartFacade.getShoppingCartData(customer, merchantStore, shoppingCartCode, language);
			if (cart != null) {
				request.getSession().setAttribute(Constants.SHOPPING_CART, cart.getCode());
			}
			if (cart == null) {
				request.getSession().removeAttribute(Constants.SHOPPING_CART);//make sure there is no cart here
				cart = new ShoppingCartData();//create an empty cart
			}
			return cart;


		} catch (Exception e) {
			LOG.error("Error while getting the shopping cart", e);
		}

		return null;

	}


	@RequestMapping(value = {"/removeMiniShoppingCartItem"}, method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/json")
	public @ResponseBody
	ShoppingCartData removeShoppingCartItem(String lineItemId, final String shoppingCartCode, HttpServletRequest request, Model model) throws Exception {
		LanguageItem language = (LanguageItem) request.getAttribute(Constants.LANGUAGE);
		MerchantStoreItem merchantStore = (MerchantStoreItem) request.getAttribute(Constants.MERCHANT_STORE);
		ShoppingCartData cart = shoppingCartFacade.getShoppingCartData(null, merchantStore, shoppingCartCode, language);
		if (cart == null) {
			return null;
		}
		ShoppingCartData shoppingCartData = shoppingCartFacade.removeCartItem(UUID.fromString(lineItemId), cart.getCode(), merchantStore, language);

		if (shoppingCartData == null) {
			return null;
		}

		if (CollectionUtils.isEmpty(shoppingCartData.getShoppingCartItems())) {
			shoppingCartFacade.deleteShoppingCart(shoppingCartData.getUuid(), merchantStore);
			request.getSession().removeAttribute(Constants.SHOPPING_CART);
			return null;
		}


		request.getSession().setAttribute(Constants.SHOPPING_CART, cart.getCode());

		LOG.debug("removed item" + lineItemId + "from cart");
		return shoppingCartData;
	}


}
