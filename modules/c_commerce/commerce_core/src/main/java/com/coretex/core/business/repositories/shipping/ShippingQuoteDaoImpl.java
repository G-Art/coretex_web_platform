package com.coretex.core.business.repositories.shipping;

import com.coretex.core.activeorm.dao.DefaultGenericDao;
import com.coretex.items.commerce_core_model.QuoteItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class ShippingQuoteDaoImpl extends DefaultGenericDao<QuoteItem> implements ShippingQuoteDao {

	public ShippingQuoteDaoImpl() {
		super(QuoteItem.ITEM_TYPE);
	}


	@Override
	public List<QuoteItem> findByOrder(UUID order) {
		return null;
	}
}
