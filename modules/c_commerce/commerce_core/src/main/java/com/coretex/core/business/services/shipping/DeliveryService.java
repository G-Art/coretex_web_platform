package com.coretex.core.business.services.shipping;

import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.model.shipping.ShippingProduct;
import com.coretex.core.model.shipping.ShippingQuote;
import com.coretex.items.commerce_core_model.DeliveryItem;
import com.coretex.items.commerce_core_model.DeliveryServiceItem;
import com.coretex.items.core.LocaleItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;

import java.util.List;
import java.util.UUID;

public interface DeliveryService {
	List<? extends DeliveryServiceItem> getAllDeliveryServices();

	List<? extends DeliveryServiceItem> getAvailableDeliveryServicesForStore(MerchantStoreItem merchantStoreItem);

	DeliveryServiceItem getByCode(String code);

	DeliveryServiceItem getTypeByCode(Class<? extends DeliveryServiceItem> type, String code);

	DeliveryServiceItem getByUUID(String uuid);

	ShippingQuote getShippingQuote(UUID shoppingCartId, MerchantStoreItem store, DeliveryItem delivery, List<ShippingProduct> products, LocaleItem language);

	}
