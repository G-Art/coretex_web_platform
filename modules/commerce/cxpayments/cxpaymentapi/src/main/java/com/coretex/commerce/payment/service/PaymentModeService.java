package com.coretex.commerce.payment.service;

import com.coretex.commerce.core.services.GenericItemService;
import com.coretex.items.cxpaymentapi.PaymentModeItem;
import reactor.core.publisher.Flux;

public interface PaymentModeService extends GenericItemService<PaymentModeItem> {

	PaymentModeItem getByCode(String code);

	Flux<PaymentModeItem> getPaymentsForDeliveryType(String code);


}
