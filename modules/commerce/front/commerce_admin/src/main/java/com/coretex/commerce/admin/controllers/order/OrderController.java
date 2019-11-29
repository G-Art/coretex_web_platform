package com.coretex.commerce.admin.controllers.order;

import com.coretex.commerce.admin.controllers.AbstractController;
import com.coretex.commerce.admin.facades.OrderFacade;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/order")
public class OrderController extends AbstractController {

	@Resource
	private OrderFacade orderFacade;

	@RequestMapping(path = "/paginated", method = RequestMethod.GET)
	@ResponseBody
	public String getPageableOrderList(@RequestParam(value = "draw", required = false) String draw,
									   @RequestParam(value = "start", required = false) Long start,
									   @RequestParam(value = "length", required = false) Long length,
									   HttpServletRequest request){
		var tableResult = orderFacade.tableResult(draw, start / length, length);
		return tableResult.getJson();
	}
}
