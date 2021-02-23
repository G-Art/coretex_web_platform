package com.coretex.commerce.facades.impl;

import com.coretex.commerce.data.PaymentTypeData;
import com.coretex.commerce.facades.PaymentFacade;
import com.coretex.commerce.mapper.PaymentTypeDataMapper;
import com.coretex.commerce.payment.service.PaymentModeService;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;

@Component
public class DefaultPaymentFacade implements PaymentFacade {

	@Resource
	private PaymentModeService paymentModeService;

	@Resource
	private PaymentTypeDataMapper paymentTypeDataMapper;

	@Override
	public Flux<PaymentTypeData> getPaymentModesForDeliveryType(String code){
		return paymentModeService.getPaymentsForDeliveryType(code)
				.map(paymentTypeDataMapper::fromItem);
	}
}
