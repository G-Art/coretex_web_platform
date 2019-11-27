package com.coretex.commerce.admin.controllers.order;

import com.coretex.commerce.admin.controllers.AbstractController;
import com.coretex.commerce.admin.controllers.DataTableResults;
import com.coretex.commerce.admin.data.MinimalOrderData;
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
	public String getPageableOrderList(@RequestParam("draw") String draw, @RequestParam("start") Long start, @RequestParam("length") Long length,  HttpServletRequest request){
		var tableResult = orderFacade.tableResult(draw, start / length, length);
		return tableResult.getJson();
	}
}
