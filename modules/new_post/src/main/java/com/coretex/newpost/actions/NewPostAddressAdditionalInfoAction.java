package com.coretex.newpost.actions;

import com.coretex.commerce.delivery.api.actions.AddressAdditionalInfoAction;
import com.coretex.core.activeorm.services.ItemService;
import com.coretex.items.cx_core.AddressItem;
import com.coretex.items.newpost.NewPostDeliveryTypeItem;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

@Component
public class NewPostAddressAdditionalInfoAction implements AddressAdditionalInfoAction<AddressItem> {
	private final Logger LOG = LoggerFactory.getLogger(this.getClass());

	@Resource
	private ItemService itemService;

	@Override
	public String deliveryType() {
		return NewPostDeliveryTypeItem.ITEM_TYPE;
	}

	@Override
	public Map<String, Object> execute(AddressItem addressItem) {

		Map<String, Object> result = Maps.newHashMap();

		result.put(NewPostAddDeliveryInfoAction.FieldExtractor.BRANCH.getInfoField(), addressItem.getNewPostWarehouse());
		result.put(NewPostAddDeliveryInfoAction.FieldExtractor.BRANCH_REF.getInfoField(), addressItem.getNewPostWarehouseRef());
		result.put(NewPostAddDeliveryInfoAction.FieldExtractor.CITY_REF.getInfoField(), addressItem.getNewPostCityRef());

		return result;

	}

}
