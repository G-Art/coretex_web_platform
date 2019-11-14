package com.coretex.core.business.repositories.shoppingcart;

import com.coretex.core.activeorm.dao.Dao;
import com.coretex.core.activeorm.dao.DefaultGenericDao;
import com.coretex.items.commerce_core_model.ShoppingCartEntryItem;
import org.springframework.stereotype.Component;

@Component
public class ShoppingCartItemDaoImpl extends DefaultGenericDao<ShoppingCartEntryItem> implements ShoppingCartItemDao{

	public ShoppingCartItemDaoImpl() {
		super(ShoppingCartEntryItem.ITEM_TYPE);
	}
}
