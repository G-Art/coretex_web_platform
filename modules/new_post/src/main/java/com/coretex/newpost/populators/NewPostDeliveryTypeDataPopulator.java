package com.coretex.newpost.populators;

import com.coretex.items.core.LocaleItem;
import com.coretex.items.cx_core.StoreItem;
import com.coretex.items.newpost.NewPostDeliveryTypeItem;
import com.coretex.newpost.data.NewPostDeliveryTypeData;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

@Component
public class NewPostDeliveryTypeDataPopulator  {

	protected NewPostDeliveryTypeData createTarget() {
		return new NewPostDeliveryTypeData();
	}

	public NewPostDeliveryTypeData populate(NewPostDeliveryTypeItem newPostDeliveryTypeItem, NewPostDeliveryTypeData newPostDeliveryTypeData, StoreItem store, LocaleItem language) {
//		newPostDeliveryTypeData.setUuid(newPostDeliveryTypeItem.getUuid().toString());
		newPostDeliveryTypeData.setName(newPostDeliveryTypeItem.allName().entrySet().stream().collect(Collectors.toMap(e -> e.getKey().toString(), Map.Entry::getValue)));
		newPostDeliveryTypeData.setActive(newPostDeliveryTypeItem.getActive());
		newPostDeliveryTypeData.setPayOnDelivery(newPostDeliveryTypeItem.getPayOnDelivery());
		newPostDeliveryTypeData.setCode(newPostDeliveryTypeItem.getCode());
		return newPostDeliveryTypeData;
	}
}
