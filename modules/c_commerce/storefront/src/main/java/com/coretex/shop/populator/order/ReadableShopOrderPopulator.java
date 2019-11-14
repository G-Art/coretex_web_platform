package com.coretex.shop.populator.order;

import com.coretex.core.business.exception.ConversionException;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.core.populators.AbstractDataPopulator;
import com.coretex.shop.model.customer.PersistableCustomer;
import com.coretex.shop.model.customer.ReadableCustomer;
import com.coretex.shop.model.customer.address.Address;
import com.coretex.shop.model.order.ReadableShopOrder;
import com.coretex.shop.model.order.ShopOrder;

public class ReadableShopOrderPopulator extends
		AbstractDataPopulator<ShopOrder, ReadableShopOrder> {

	@Override
	public ReadableShopOrder populate(ShopOrder source,
									  ReadableShopOrder target, MerchantStoreItem store, LanguageItem language)
			throws ConversionException {

		//not that much is required


		//customer

		try {

		} catch (Exception e) {
			throw new ConversionException(e);
		}


		return target;
	}

	@Override
	protected ReadableShopOrder createTarget() {
		return null;
	}

}
