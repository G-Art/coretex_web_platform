package com.coretex.commerce.facades;

import com.coretex.commerce.data.PaymentTypeData;

import java.util.stream.Stream;

public interface PaymentFacade {
	Stream<PaymentTypeData> getPaymentModesForDeliveryType(String code);
}
