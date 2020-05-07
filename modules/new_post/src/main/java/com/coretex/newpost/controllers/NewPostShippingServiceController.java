package com.coretex.newpost.controllers;

import com.coretex.commerce.delivery.api.service.DeliveryServiceService;
import com.coretex.items.newpost.NewPostDeliveryServiceItem;
import com.coretex.newpost.api.NewPostApiService;
import com.coretex.newpost.api.address.data.properties.SettlementsProperties;
import com.coretex.newpost.api.address.data.properties.WarehousesProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.UUID;

@RestController()
@RequestMapping("/newpost")
public class NewPostShippingServiceController {

	@Resource
	private DeliveryServiceService deliveryService;
	@Resource
	private NewPostApiService newPostApiService;

	private static final Logger LOGGER = LoggerFactory.getLogger(NewPostShippingServiceController.class);

	@GetMapping(value = "/{uid}/settlements")
	public Object getNewPostCities(@PathVariable("uid") UUID uid, @RequestParam("q") String query) {
		var deliveryServiceItem = deliveryService.getByUUID(uid);
		SettlementsProperties settlements = new SettlementsProperties();
		settlements.setFindByString(query);
		settlements.setWarehouse("1");
		var result = newPostApiService.getNewPostAddressApiService().getSettlements((NewPostDeliveryServiceItem) deliveryServiceItem, settlements);
		return result.getData();
	}

	@GetMapping(value = "/{uid}/warehouses")
	public Object getNewPostOfficeBySettlement(@PathVariable("uid") UUID uid, @RequestParam("ref") String settlement) {
		var deliveryServiceItem = deliveryService.getByUUID(uid);
		var options = new WarehousesProperties();
		options.setSettlementRef(settlement);
		var result = newPostApiService.getNewPostAddressApiService().getWarehouses((NewPostDeliveryServiceItem) deliveryServiceItem, options);

		return result.getData();
	}


}
