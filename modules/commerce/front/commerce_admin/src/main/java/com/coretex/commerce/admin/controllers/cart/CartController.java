package com.coretex.commerce.admin.controllers.cart;

import com.coretex.commerce.admin.controllers.PageableDataTableAbstractController;
import com.coretex.commerce.data.CartData;
import com.coretex.commerce.facades.CartFacade;
import com.coretex.commerce.facades.PageableDataTableFacade;
import com.coretex.items.cx_core.CartItem;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.util.UUID;
@Controller
@RequestMapping("/cart")
public class CartController extends PageableDataTableAbstractController<CartData> {

	@Resource
	private CartFacade cartFacade;

	@RequestMapping(path = "", method = RequestMethod.GET)
	public String getCarts() {
		return "order/carts";
	}

	@RequestMapping(path = "/{uuid}", method = RequestMethod.GET)
	public String getUserData(@PathVariable("uuid") UUID uuid, Model model) {
		var order = cartFacade.getByUUID(uuid);
		model.addAttribute("order", order);
		return "order/cart";
	}

	@RequestMapping(path = {"/remove/{uuid}"}, method = {RequestMethod.GET, RequestMethod.DELETE})
	public String removeStore(@PathVariable(value = "uuid") UUID uuid,
	                          RedirectAttributes redirectAttributes) {
		cartFacade.delete(uuid);
		addInfoFlashMessage(redirectAttributes, "Cart removed");

		return redirect("/cart");
	}

	@Override
	protected PageableDataTableFacade<CartItem, CartData> getPageableFacade() {
		return cartFacade;
	}
}
