package com.coretex.commerce.admin.controllers.order;

import com.coretex.commerce.admin.controllers.PageableDataTableAbstractController;
import com.coretex.commerce.admin.facades.OrderFacade;
import com.coretex.commerce.admin.facades.PageableDataTableFacade;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
@RequestMapping("/order")
public class OrderController extends PageableDataTableAbstractController {

	@Resource
	private OrderFacade orderFacade;

	@Override
	protected PageableDataTableFacade getPageableFacade() {
		return orderFacade;
	}
}
