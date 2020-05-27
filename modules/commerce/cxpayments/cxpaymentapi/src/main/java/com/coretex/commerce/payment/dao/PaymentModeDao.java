package com.coretex.commerce.payment.dao;

import com.coretex.core.activeorm.dao.Dao;
import com.coretex.items.cxpaymentapi.PaymentModeItem;

import java.util.stream.Stream;

public interface PaymentModeDao extends Dao<PaymentModeItem> {
	PaymentModeItem findByCode(String code);

	Stream<PaymentModeItem> findByDeliveryType(String code);
}
