package com.coretex.core.populators;

import com.coretex.core.business.exception.ConversionException;
import com.coretex.items.commerce_core_model.DeliveryServiceItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.core.data.shipping.DeliveryServiceData;

import java.util.Map;
import java.util.stream.Collectors;

public abstract class DeliveryServiceDataPopulator<SOURCE extends DeliveryServiceItem, TARGET extends DeliveryServiceData> extends
		AbstractDataPopulator<SOURCE, TARGET> {

	@Override
	public TARGET populate(SOURCE deliveryServiceItem,
						   TARGET deliveryServiceData,
										MerchantStoreItem store,
										LanguageItem language) throws ConversionException {
		deliveryServiceData.setUuid(deliveryServiceItem.getUuid().toString());
		deliveryServiceData.setCode(deliveryServiceItem.getCode());
		deliveryServiceData.setImage(deliveryServiceItem.getImage());
		deliveryServiceData.setName(deliveryServiceItem.allName().entrySet().stream().collect(Collectors.toMap(e -> e.getKey().toString(), Map.Entry::getValue)));
		deliveryServiceData.setActive(deliveryServiceData.getActive());

		return deliveryServiceData;
	}


}
