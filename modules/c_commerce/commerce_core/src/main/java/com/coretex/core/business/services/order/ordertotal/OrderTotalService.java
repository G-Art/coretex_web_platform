package com.coretex.core.business.services.order.ordertotal;

import com.coretex.items.cx_core.CustomerItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.core.model.order.OrderSummary;
import com.coretex.core.model.order.OrderTotalVariation;
import com.coretex.items.core.LocaleItem;
import com.coretex.items.core.LocaleItem;

/**
 * Additional dynamic order total calculation
 * from the rules engine and other modules
 *
 * @author carlsamson
 */
public interface OrderTotalService {

	OrderTotalVariation findOrderTotalVariation(final OrderSummary summary, final CustomerItem customer, final MerchantStoreItem store, final LocaleItem language) throws Exception;

}
