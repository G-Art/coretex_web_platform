package com.coretex.core.activeorm.dao;

import com.coretex.items.core.LocaleItem;
import org.springframework.stereotype.Component;

@Component
public class LocaleDao extends DefaultGenericDao<LocaleItem> implements Dao<LocaleItem> {
	public LocaleDao() {
		super(LocaleItem.ITEM_TYPE);
	}
}
