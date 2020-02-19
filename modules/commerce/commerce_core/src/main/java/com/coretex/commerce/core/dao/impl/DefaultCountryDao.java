package com.coretex.commerce.core.dao.impl;

import com.coretex.commerce.core.dao.CountryDao;
import com.coretex.core.activeorm.dao.DefaultGenericDao;
import com.coretex.items.core.CountryItem;
import org.springframework.stereotype.Component;

@Component
public class DefaultCountryDao extends DefaultGenericDao<CountryItem> implements CountryDao {
	public DefaultCountryDao() {
		super(CountryItem.ITEM_TYPE);
	}
}
