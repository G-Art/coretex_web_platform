package com.coretex.commerce.facades.impl;

import com.coretex.commerce.data.PaymentTypeData;
import com.coretex.commerce.facades.PaymentFacade;
import com.coretex.commerce.mapper.PaymentTypeDataMapper;
import com.coretex.commerce.payment.service.PaymentModeService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.stream.Stream;

@Component
public class DefaultPaymentFacade implements PaymentFacade {

	@Resource
	private PaymentModeService paymentModeService;

	@Resource
	private PaymentTypeDataMapper paymentTypeDataMapper;

	@Override
	public Stream<PaymentTypeData> getPaymentModesForDeliveryType(String code){
		return paymentModeService.getPaymentsForDeliveryType(code)
				.map(paymentTypeDataMapper::fromItem);
	}
}
