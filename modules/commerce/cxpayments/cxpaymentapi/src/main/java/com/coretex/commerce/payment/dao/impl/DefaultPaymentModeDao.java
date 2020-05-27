package com.coretex.commerce.payment.dao.impl;

import com.coretex.commerce.payment.dao.PaymentModeDao;
import com.coretex.core.activeorm.dao.DefaultGenericDao;
import com.coretex.items.cx_commercedelivery_api.DeliveryTypeItem;
import com.coretex.items.cxpaymentapi.PaymentModeItem;
import com.coretex.relations.cxpaymentapi.PaymentModeDeliveryTypeRelation;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Stream;

@Component
public class DefaultPaymentModeDao extends DefaultGenericDao<PaymentModeItem> implements PaymentModeDao {
	public DefaultPaymentModeDao() {
		super(PaymentModeItem.ITEM_TYPE);
	}

	@Override
	public PaymentModeItem findByCode(String code) {
		return findSingle(Map.of(PaymentModeItem.CODE, code), false);
	}

	@Override
	public Stream<PaymentModeItem> findByDeliveryType(String code) {
		return findReactive(" SELECT * FROM " + PaymentModeItem.ITEM_TYPE + " as pm " +
				"JOIN " + PaymentModeDeliveryTypeRelation.ITEM_TYPE + " as pmdt ON pmdt.source = pm.uuid " +
				"JOIN " + DeliveryTypeItem.ITEM_TYPE + " as dt ON pmdt.target = dt.uuid " +
				"WHERE dt." + DeliveryTypeItem.CODE +" = :code", Map.of("code", code) );
	}
}
