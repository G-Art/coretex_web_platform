package com.coretex.core.business.repositories.reference.currency;

import com.coretex.core.activeorm.dao.Dao;

import com.coretex.items.cx_core.CurrencyItem;

public interface CurrencyDao extends Dao<CurrencyItem> {


	CurrencyItem getByCode(String code);
}
