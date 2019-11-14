package com.coretex.newpost.api;

import com.coretex.newpost.api.data.RequestApiData;

public interface NewPostDataApiService {

	default <D> RequestApiData<D> createRequestApiData(String apiKey, String modelName, String calledMethod, D methodProperties) {
		return new RequestApiData<D>(apiKey, modelName, calledMethod, methodProperties);
	}

}
