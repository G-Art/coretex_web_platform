package com.coretex.newpost.populators;

import com.coretex.core.populators.AbstractDataPopulator;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.newpost.NewPostDeliveryTypeItem;
import com.coretex.newpost.data.NewPostDeliveryTypeData;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

@Component
public class NewPostDeliveryTypeDataPopulator extends AbstractDataPopulator<NewPostDeliveryTypeItem, NewPostDeliveryTypeData> {

	@Override
	protected NewPostDeliveryTypeData createTarget() {
		return new NewPostDeliveryTypeData();
	}

	@Override
	public NewPostDeliveryTypeData populate(NewPostDeliveryTypeItem newPostDeliveryTypeItem, NewPostDeliveryTypeData newPostDeliveryTypeData, MerchantStoreItem store, LanguageItem language) {
		newPostDeliveryTypeData.setUuid(newPostDeliveryTypeItem.getUuid().toString());
		newPostDeliveryTypeData.setName(newPostDeliveryTypeItem.allName().entrySet().stream().collect(Collectors.toMap(e -> e.getKey().toString(), Map.Entry::getValue)));
		newPostDeliveryTypeData.setActive(newPostDeliveryTypeItem.getActive());
		newPostDeliveryTypeData.setPayOnDelivery(newPostDeliveryTypeItem.getPayOnDelivery());
		newPostDeliveryTypeData.setCode(newPostDeliveryTypeItem.getCode());
		return newPostDeliveryTypeData;
	}
}
