package com.coretex.newpost.populators;

import com.coretex.core.business.exception.ConversionException;
import com.coretex.core.populators.DeliveryServiceDataPopulator;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.newpost.NewPostDeliveryServiceItem;
import com.coretex.items.newpost.NewPostDeliveryTypeItem;
import com.coretex.newpost.data.NewPostDeliveryServiceData;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.stream.Collectors;

@Component
public class NewPostDeliveryServiceDataPopulator extends DeliveryServiceDataPopulator<NewPostDeliveryServiceItem, NewPostDeliveryServiceData> {

	@Resource
	private NewPostDeliveryTypeDataPopulator newPostDeliveryTypeDataPopulator;

	@Override
	protected NewPostDeliveryServiceData createTarget() {
		return new NewPostDeliveryServiceData();
	}

	@Override
	public NewPostDeliveryServiceData populate(NewPostDeliveryServiceItem deliveryServiceItem, NewPostDeliveryServiceData deliveryServiceData, MerchantStoreItem store, LanguageItem language) throws ConversionException {
		super.populate(deliveryServiceItem, deliveryServiceData, store, language);
		var deliveryTypeDataList = deliveryServiceItem.getDeliveryTypes()
				.stream()
				.map(newPostDeliveryTypeItem -> newPostDeliveryTypeDataPopulator.populate((NewPostDeliveryTypeItem) newPostDeliveryTypeItem, store, language))
				.collect(Collectors.toList());
		deliveryServiceData.setApiKey(deliveryServiceItem.getApiKey());
		deliveryServiceData.setEndpoint(deliveryServiceItem.getEndpoint());
		deliveryServiceData.setDeliveryTypes(deliveryTypeDataList);
		deliveryServiceData.setActive(deliveryServiceItem.getActive());

		return deliveryServiceData;
	}
}
