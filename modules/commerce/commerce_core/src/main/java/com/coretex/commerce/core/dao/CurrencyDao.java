package com.coretex.commerce.core.dao;

import com.coretex.core.activeorm.dao.Dao;
import com.coretex.items.cx_core.CurrencyItem;

public interface CurrencyDao extends Dao<CurrencyItem> {
	CurrencyItem findByCode(String code);
}
