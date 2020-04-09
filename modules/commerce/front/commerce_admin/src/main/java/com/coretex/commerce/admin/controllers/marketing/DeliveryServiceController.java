package com.coretex.commerce.admin.controllers.marketing;

import com.coretex.commerce.admin.controllers.PageableDataTableAbstractController;
import com.coretex.commerce.data.minimal.MinimalDeliveryServiceData;
import com.coretex.commerce.facades.DeliveryServiceFacade;
import com.coretex.commerce.facades.PageableDataTableFacade;
import com.coretex.items.core.GenericItem;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.util.UUID;

@Controller

@RequestMapping("/delivery")
public class DeliveryServiceController extends PageableDataTableAbstractController<MinimalDeliveryServiceData> {

	@Resource
	private DeliveryServiceFacade deliveryServiceFacade;

	@Override
	protected PageableDataTableFacade<? extends GenericItem, MinimalDeliveryServiceData> getPageableFacade() {
		return deliveryServiceFacade;
	}

	@RequestMapping(path = "", method = RequestMethod.GET)
	public String getDeliveryServices(Model model) {
		return "delivery/deliveryServices";
	}

	@RequestMapping(path = "/service/{uuid}", method = RequestMethod.GET)
	public String getDeliveryService(@PathVariable("uuid") UUID uuid, Model model) {
		var ds = deliveryServiceFacade.getByUUID(uuid);
		model.addAttribute("ds", ds);
		return "delivery/deliveryService";
	}

}
