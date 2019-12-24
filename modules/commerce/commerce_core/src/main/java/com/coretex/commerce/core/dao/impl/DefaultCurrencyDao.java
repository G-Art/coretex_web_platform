package com.coretex.commerce.core.dao.impl;

import com.coretex.commerce.core.dao.CurrencyDao;
import com.coretex.core.activeorm.dao.DefaultGenericDao;
import com.coretex.items.cx_core.CurrencyItem;
import org.springframework.stereotype.Component;

@Component
public class DefaultCurrencyDao extends DefaultGenericDao<CurrencyItem> implements CurrencyDao {

	public DefaultCurrencyDao() {
		super(CurrencyItem.ITEM_TYPE);
	}

	@Override
	public CurrencyItem findByCode(String code) {
		return null;
	}
}
