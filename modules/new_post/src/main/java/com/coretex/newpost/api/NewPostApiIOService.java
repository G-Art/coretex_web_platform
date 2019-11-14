package com.coretex.newpost.api;

import com.coretex.newpost.api.data.RequestApiData;
import com.coretex.newpost.api.data.ResponseApiData;
import com.fasterxml.jackson.core.type.TypeReference;

public interface NewPostApiIOService<T extends RequestApiData<?>> {

	<R> ResponseApiData<R> submit(T request, String url, TypeReference<ResponseApiData<R>> typeReference);
}
