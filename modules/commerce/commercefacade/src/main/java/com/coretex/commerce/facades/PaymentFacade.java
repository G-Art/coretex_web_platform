package com.coretex.commerce.facades;

import com.coretex.commerce.data.PaymentTypeData;
import reactor.core.publisher.Flux;

public interface PaymentFacade {
	Flux<PaymentTypeData> getPaymentModesForDeliveryType(String code);
}
