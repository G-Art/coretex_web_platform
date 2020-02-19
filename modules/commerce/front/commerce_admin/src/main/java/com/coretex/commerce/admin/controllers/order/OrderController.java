package com.coretex.commerce.admin.controllers.order;

import com.coretex.commerce.admin.controllers.PageableDataTableAbstractController;
import com.coretex.commerce.data.minimal.MinimalOrderData;
import com.coretex.commerce.facades.OrderFacade;
import com.coretex.commerce.facades.PageableDataTableFacade;
import com.coretex.items.cx_core.OrderItem;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.util.UUID;

@Controller
@RequestMapping("/order")
public class OrderController extends PageableDataTableAbstractController<MinimalOrderData> {

	@Resource
	private OrderFacade orderFacade;

	@RequestMapping(path = "", method = RequestMethod.GET)
	public String getOrders() {
		return "order/orders";
	}

	@RequestMapping(path = "/{uuid}", method = RequestMethod.GET)
	public String getUserData(@PathVariable("uuid") UUID uuid, Model model) {
		var order = orderFacade.getOrderByUUID(uuid);
		model.addAttribute("order", order);
		return "order/order";
	}

	@Override
	protected PageableDataTableFacade<OrderItem, MinimalOrderData> getPageableFacade() {
		return orderFacade;
	}
}
