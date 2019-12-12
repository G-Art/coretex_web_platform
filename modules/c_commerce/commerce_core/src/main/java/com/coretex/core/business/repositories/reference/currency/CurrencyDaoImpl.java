package com.coretex.core.business.repositories.reference.currency;

import com.coretex.core.activeorm.dao.DefaultGenericDao;
import com.coretex.items.cx_core.CurrencyItem;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CurrencyDaoImpl extends DefaultGenericDao<CurrencyItem> implements CurrencyDao {
	public CurrencyDaoImpl() {
		super(CurrencyItem.ITEM_TYPE);
	}

	@Override
	public CurrencyItem getByCode(String code) {
		return findSingle(Map.of(CurrencyItem.CODE, code), true);
	}
}
