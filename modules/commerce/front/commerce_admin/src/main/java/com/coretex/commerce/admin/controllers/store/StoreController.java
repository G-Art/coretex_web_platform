package com.coretex.commerce.admin.controllers.store;

import com.coretex.commerce.admin.controllers.AbstractController;
import com.coretex.commerce.admin.data.GroupData;
import com.coretex.commerce.admin.data.MerchantStoreData;
import com.coretex.commerce.admin.data.UserData;
import com.coretex.commerce.admin.facades.GroupFacade;
import com.coretex.commerce.admin.facades.StoreFacade;
import com.coretex.commerce.admin.facades.UserFacade;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

@Controller
@RequestMapping("/store")
public class StoreController extends AbstractController {

	@Resource
	private StoreFacade storeFacade;


	@RequestMapping(path = "",method = RequestMethod.GET)
	public String getStores(Model model) {
		return "store/stores";
	}

	@RequestMapping(path = "/{uuid}", method = RequestMethod.GET)
	public String getStore(@PathVariable("uuid") UUID uuid, Model model) {
		return "store/store";
	}

	@RequestMapping(path = "/paginated", method = RequestMethod.GET)
	@ResponseBody
	public String getPageableOrderList(@RequestParam("draw") String draw, @RequestParam("start") Long start, @RequestParam("length") Long length){
		var tableResult = storeFacade.tableResult(draw, start / length, length);
		return tableResult.getJson();
	}

}
