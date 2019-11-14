package com.coretex.core.business.services.shipping;

import com.coretex.core.business.services.common.generic.SalesManagerEntityService;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.ShippingOriginItem;

/**
 * ShippingOriginItem object if different from MerchantStoreItem address.
 * Can be managed through this service.
 *
 * @author carlsamson
 */
public interface ShippingOriginService extends SalesManagerEntityService<ShippingOriginItem> {

	ShippingOriginItem getByStore(MerchantStoreItem store);


}
