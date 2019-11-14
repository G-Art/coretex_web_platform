package com.coretex.newpost.api.actions.impl;

import com.coretex.items.newpost.NewPostDeliveryServiceItem;
import com.coretex.newpost.api.NewPostApiRequestService;
import com.coretex.newpost.api.NewPostDataApiService;
import com.coretex.newpost.api.actions.NewPostActionApiService;
import com.coretex.newpost.api.actions.data.properties.DocumentDeliveryDateProperties;
import com.coretex.newpost.api.actions.data.properties.DocumentListProperties;
import com.coretex.newpost.api.actions.data.properties.DocumentPriceProperties;
import com.coretex.newpost.api.actions.data.values.DocumentDeliveryDateValue;
import com.coretex.newpost.api.actions.data.values.DocumentListItemValue;
import com.coretex.newpost.api.actions.data.values.DocumentPriceValue;
import com.coretex.newpost.api.data.ResponseApiData;
import com.coretex.newpost.api.enchiridion.data.properties.TimeIntervalsProperties;
import com.coretex.newpost.api.enchiridion.data.values.TimeIntervalValue;
import com.coretex.newpost.api.enchiridion.impl.DefaultNewPostEnchiridionsApiService;
import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DefaultNewPostActionApiService extends NewPostActionApiService {

	private Logger LOG = LoggerFactory.getLogger(DefaultNewPostActionApiService.class);

	private final String INTERNET_DOCUMENT_MODEL_NAME = "InternetDocument";

	@Override
	public ResponseApiData<DocumentListItemValue> getDocumentList(NewPostDeliveryServiceItem deliveryServiceItem, DocumentListProperties documentListProperties) {
		var requestApiData = createRequestApiData(deliveryServiceItem.getApiKey(),
				INTERNET_DOCUMENT_MODEL_NAME, "getDocumentList", documentListProperties);
		return request(deliveryServiceItem,
				requestApiData,
				new TypeReference<ResponseApiData<DocumentListItemValue>>() {
				});
	}

	@Override
	public ResponseApiData<DocumentDeliveryDateValue> getDocumentDeliveryDate(NewPostDeliveryServiceItem deliveryServiceItem, DocumentDeliveryDateProperties documentDeliveryDateProperties) {
		var requestApiData = createRequestApiData(deliveryServiceItem.getApiKey(),
				INTERNET_DOCUMENT_MODEL_NAME, "getDocumentDeliveryDate", documentDeliveryDateProperties);
		return request(deliveryServiceItem,
				requestApiData,
				new TypeReference<ResponseApiData<DocumentDeliveryDateValue>>() {
				});
	}

	@Override
	public ResponseApiData<DocumentPriceValue> getDocumentPrice(NewPostDeliveryServiceItem deliveryServiceItem, DocumentPriceProperties documentPriceProperties) {
		var requestApiData = createRequestApiData(deliveryServiceItem.getApiKey(),
				INTERNET_DOCUMENT_MODEL_NAME, "getDocumentPrice", documentPriceProperties);
		return request(deliveryServiceItem,
				requestApiData,
				new TypeReference<ResponseApiData<DocumentPriceValue>>() {
				});
	}


}
