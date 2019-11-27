package com.coretex.core.business.services.order.ordertotal;

import com.coretex.core.model.order.OrderSummary;
import com.coretex.core.model.order.OrderTotalVariation;
import com.coretex.core.model.order.RebatesOrderTotalVariation;
import com.coretex.items.commerce_core_model.CustomerItem;
import com.coretex.items.core.LocaleItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import org.springframework.stereotype.Service;

@Service("OrderTotalService")
public class OrderTotalServiceImpl implements OrderTotalService {


	@Override
	public OrderTotalVariation findOrderTotalVariation(OrderSummary summary, CustomerItem customer, MerchantStoreItem store, LocaleItem language) {

		return new RebatesOrderTotalVariation();
	}

}
