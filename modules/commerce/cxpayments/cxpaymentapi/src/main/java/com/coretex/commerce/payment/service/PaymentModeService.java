package com.coretex.commerce.payment.service;

import com.coretex.commerce.core.services.GenericItemService;
import com.coretex.items.cxpaymentapi.PaymentModeItem;

import java.util.stream.Stream;

public interface PaymentModeService extends GenericItemService<PaymentModeItem> {

	PaymentModeItem getByCode(String code);

	Stream<PaymentModeItem> getPaymentsForDeliveryType(String code);


}
