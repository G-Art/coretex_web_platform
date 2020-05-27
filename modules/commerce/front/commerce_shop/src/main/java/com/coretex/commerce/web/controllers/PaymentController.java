package com.coretex.commerce.web.controllers;

import com.coretex.commerce.facades.PaymentFacade;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/v1/payment")
public class PaymentController {

	@Resource
	private PaymentFacade paymentFacade;

//	@GetMapping(path = "/type/{code}")
//	private Mono<PaymentTypeData> getPaymentByCode(@PathVariable(value = "code") String code) {
//		return paymentFacade.
//	}
}
