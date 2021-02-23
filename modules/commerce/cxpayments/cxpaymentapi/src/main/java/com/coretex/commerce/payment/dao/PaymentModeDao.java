package com.coretex.commerce.payment.dao;

import com.coretex.core.activeorm.dao.Dao;
import com.coretex.items.cxpaymentapi.PaymentModeItem;
import reactor.core.publisher.Flux;

public interface PaymentModeDao extends Dao<PaymentModeItem> {
	PaymentModeItem findByCode(String code);

	Flux<PaymentModeItem> findByDeliveryType(String code);
}
