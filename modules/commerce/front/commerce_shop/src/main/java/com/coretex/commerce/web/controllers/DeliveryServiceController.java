package com.coretex.commerce.web.controllers;

import com.coretex.commerce.data.DeliveryServiceData;
import com.coretex.commerce.data.DeliveryTypeData;
import com.coretex.commerce.data.PaymentTypeData;
import com.coretex.commerce.facades.DeliveryServiceFacade;
import com.coretex.commerce.facades.PaymentFacade;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;
import java.util.UUID;

@RestController
@RequestMapping("/v1/delivery")
public class DeliveryServiceController {

	@Resource
	private DeliveryServiceFacade deliveryServiceFacade;

	@Resource
	private PaymentFacade paymentFacade;

	@GetMapping(path = {"/service/cart/{uuid}"})
	private Flux<DeliveryServiceData> getDeliveryServiceForCart(@PathVariable("uuid") UUID uuid) {
		return Flux.fromStream(deliveryServiceFacade.getForCartUUID(uuid));
	}


	@GetMapping(path = {"/service/{uuid}/types"})
	private Flux<DeliveryTypeData> getDeliveryTypeForService(@PathVariable("uuid") UUID uuid) {
		return Flux.fromStream(deliveryServiceFacade.getDeliveryTypesForService(uuid));
	}

	@GetMapping(path = {"/type/{code}/payments"})
	private Flux<PaymentTypeData> getPaymentsForType(@PathVariable("code") String code) {
		return Flux.fromStream(paymentFacade.getPaymentModesForDeliveryType(code));
	}
}
