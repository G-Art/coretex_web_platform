package com.coretex.newpost.populators;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class NewPostDeliveryServiceDataPopulator
//		extends DeliveryServiceDataPopulator<NewPostDeliveryServiceItem, NewPostDeliveryServiceData>
{

	@Resource
	private NewPostDeliveryTypeDataPopulator newPostDeliveryTypeDataPopulator;

//	@Override
//	protected NewPostDeliveryServiceData createTarget() {
//		return new NewPostDeliveryServiceData();
//	}

//	@Override
//	public NewPostDeliveryServiceData populate(NewPostDeliveryServiceItem deliveryServiceItem, NewPostDeliveryServiceData deliveryServiceData, MerchantStoreItem store, LocaleItem language) throws ConversionException {
//		super.populate(deliveryServiceItem, deliveryServiceData, store, language);
//		var deliveryTypeDataList = deliveryServiceItem.getDeliveryTypes()
//				.stream()
//				.map(newPostDeliveryTypeItem -> newPostDeliveryTypeDataPopulator.populate((NewPostDeliveryTypeItem) newPostDeliveryTypeItem, store, language))
//				.collect(Collectors.toList());
//		deliveryServiceData.setApiKey(deliveryServiceItem.getApiKey());
//		deliveryServiceData.setEndpoint(deliveryServiceItem.getEndpoint());
//		deliveryServiceData.setDeliveryTypes(deliveryTypeDataList);
//		deliveryServiceData.setActive(deliveryServiceItem.getActive());
//
//		return deliveryServiceData;
//	}
}
