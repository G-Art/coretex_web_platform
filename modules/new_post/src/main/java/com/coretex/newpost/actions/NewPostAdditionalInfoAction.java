package com.coretex.newpost.actions;

import com.coretex.commerce.delivery.api.actions.AdditionalInfoAction;
import com.coretex.items.newpost.NewPostDeliveryTypeItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class NewPostAdditionalInfoAction implements AdditionalInfoAction<NewPostDeliveryTypeItem> {

	private final Logger LOG = LoggerFactory.getLogger(this.getClass());

	@Override
	public String deliveryType() {
		return NewPostDeliveryTypeItem.ITEM_TYPE;
	}

	@Override
	public Map<String, Object> execute(NewPostDeliveryTypeItem object) {
		var map = new HashMap<String, Object>();
		map.put(NewPostDeliveryTypeItem.PAY_ON_DELIVERY, object.getPayOnDelivery());
		map.put(NewPostDeliveryTypeItem.SEND_FROM_WAREHOUSE, object.getSendFromWarehouse());
		map.put(NewPostDeliveryTypeItem.SEND_TO_WAREHOUSE, object.getSendToWarehouse());
		if (LOG.isDebugEnabled()) {
			LOG.debug(String.format("Additional info NewPost  info:[%s]", map));
		}
		return map;
	}
}
