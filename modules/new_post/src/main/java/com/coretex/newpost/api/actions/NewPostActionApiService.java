package com.coretex.newpost.api.actions;

import com.coretex.items.newpost.NewPostDeliveryServiceItem;
import com.coretex.newpost.api.NewPostApiRequestService;
import com.coretex.newpost.api.NewPostDataApiService;
import com.coretex.newpost.api.actions.data.properties.DocumentDeliveryDateProperties;
import com.coretex.newpost.api.actions.data.properties.DocumentListProperties;
import com.coretex.newpost.api.actions.data.properties.DocumentPriceProperties;
import com.coretex.newpost.api.actions.data.values.DocumentDeliveryDateValue;
import com.coretex.newpost.api.actions.data.values.DocumentListItemValue;
import com.coretex.newpost.api.actions.data.values.DocumentPriceValue;
import com.coretex.newpost.api.data.ResponseApiData;

public abstract class NewPostActionApiService extends NewPostApiRequestService implements NewPostDataApiService {
	public abstract ResponseApiData<DocumentListItemValue> getDocumentList(NewPostDeliveryServiceItem deliveryServiceItem, DocumentListProperties documentListProperties);

	public abstract ResponseApiData<DocumentDeliveryDateValue> getDocumentDeliveryDate(NewPostDeliveryServiceItem deliveryServiceItem, DocumentDeliveryDateProperties documentDeliveryDateProperties);

	public abstract ResponseApiData<DocumentPriceValue> getDocumentPrice(NewPostDeliveryServiceItem deliveryServiceItem, DocumentPriceProperties documentPriceProperties);
}
