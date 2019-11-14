package com.coretex.core.business.repositories.shoppingcart;

import com.coretex.core.activeorm.dao.DefaultGenericDao;
import com.coretex.items.commerce_core_model.ShoppingCartEntryAttributeItem;
import org.springframework.stereotype.Component;

@Component
public class ShoppingCartAttributeDaoImpl extends DefaultGenericDao<ShoppingCartEntryAttributeItem> implements ShoppingCartAttributeDao {

	public ShoppingCartAttributeDaoImpl() {
		super(ShoppingCartEntryAttributeItem.ITEM_TYPE);
	}
}
