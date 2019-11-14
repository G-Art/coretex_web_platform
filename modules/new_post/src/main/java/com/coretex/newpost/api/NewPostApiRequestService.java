package com.coretex.newpost.api;

import com.coretex.enums.newpost.DataFormatEnum;
import com.coretex.items.newpost.NewPostDeliveryServiceItem;
import com.coretex.newpost.api.data.RequestApiData;
import com.coretex.newpost.api.data.ResponseApiData;
import com.coretex.newpost.api.impl.NewPostApiJsonIOService;
import com.coretex.newpost.api.impl.NewPostApiXmlIOService;
import com.fasterxml.jackson.core.type.TypeReference;

import javax.annotation.Resource;

public abstract class NewPostApiRequestService {

	@Resource
	private NewPostApiJsonIOService newPostApiJsonIOService;

	@Resource
	private NewPostApiXmlIOService newPostApiXmlIOService;

	protected  <R> ResponseApiData<R> request(NewPostDeliveryServiceItem deliveryServiceItem, RequestApiData<?> requestApiData, TypeReference<ResponseApiData<R>> responseFunction) {

		ResponseApiData<R> result;
		String url = deliveryServiceItem.getEndpoint() + deliveryServiceItem.getDataFormat().toString() + "/" + requestApiData.getModelName() + "/" + requestApiData.getCalledMethod();

		if (deliveryServiceItem.getDataFormat() == DataFormatEnum.JSON) {
			result = newPostApiJsonIOService.submit(requestApiData, url, responseFunction);
		} else {
			result = newPostApiXmlIOService.submit(requestApiData, url, responseFunction);
		}
		return result;
	}
}
