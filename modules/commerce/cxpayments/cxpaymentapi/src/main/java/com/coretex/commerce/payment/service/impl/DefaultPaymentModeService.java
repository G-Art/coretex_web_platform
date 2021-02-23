package com.coretex.commerce.payment.service.impl;

import com.coretex.commerce.core.services.AbstractGenericItemService;
import com.coretex.commerce.payment.dao.PaymentModeDao;
import com.coretex.commerce.payment.service.PaymentModeService;
import com.coretex.items.cxpaymentapi.PaymentModeItem;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class DefaultPaymentModeService extends AbstractGenericItemService<PaymentModeItem> implements PaymentModeService {

	public DefaultPaymentModeService(PaymentModeDao repository) {
		super(repository);
	}

	@Override
	public PaymentModeItem getByCode(String code) {
		return getRepository().findByCode(code);
	}

	@Override
	public Flux<PaymentModeItem> getPaymentsForDeliveryType(String code) {
		return getRepository().findByDeliveryType(code);
	}

	@Override
	public PaymentModeDao getRepository() {
		return (PaymentModeDao) super.getRepository();
	}
}
