package com.coretex.commerce.web.controllers;

import com.coretex.commerce.data.DeliveryServiceData;
import com.coretex.commerce.data.DeliveryTypeData;
import com.coretex.commerce.facades.DeliveryServiceFacade;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;
import java.util.UUID;

@RestController
@RequestMapping("/v1/delivery/service")
public class DeliveryServiceController {

	@Resource
	private DeliveryServiceFacade deliveryServiceFacade;

	@GetMapping(path = {"/cart/{uuid}"})
	private Flux<DeliveryServiceData> getDeliveryServiceForCart(@PathVariable("uuid") UUID uuid) {
		return Flux.fromStream(deliveryServiceFacade.getForCartUUID(uuid));
	}


	@GetMapping(path = {"/{uuid}/types"})
	private Flux<DeliveryTypeData> getDeliveryTypeForService(@PathVariable("uuid") UUID uuid) {
		return Flux.fromStream(deliveryServiceFacade.getDeliveryTypesForService(uuid));
	}

}
