package com.coretex.newpost.api.impl;

import com.coretex.newpost.api.NewPostApiIOService;
import com.coretex.newpost.api.data.RequestApiData;
import com.coretex.newpost.api.data.ResponseApiData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringEscapeUtils;
import org.javalite.http.Http;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.function.Function;

@Service
public class NewPostApiJsonIOService<T extends RequestApiData<?>> implements NewPostApiIOService<T> {

	private Logger LOG = LoggerFactory.getLogger(NewPostApiJsonIOService.class);

	@Override
	public <R> ResponseApiData<R> submit(T request, String url, TypeReference<ResponseApiData<R>> typeReference) {
		try {
			var value = createObjectMapper()
					.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
					.writerWithDefaultPrettyPrinter()
					.writeValueAsString(request);
			var post = Http.post(url, value)
					.header("Accept", "application/json")
					.header("Content-Type", "application/json");


			if(LOG.isDebugEnabled()){
				LOG.debug(String.format("NewPost JSON API Request: " +
						"\n url: [%s] " +
						"\n payload : [%s] " +
						"\n type: [%s]", url, value, typeReference.getType().getTypeName()));
			}

			return parseResponse(typeReference).apply(post.text());

		} catch (JsonProcessingException e) {
			LOG.error("Can't convert New Post Request Api Date to JSON", e);
		}
		return null;
	}

	private <R> Function<String, R> parseResponse(TypeReference<R> typeReference) {
		return data -> {
			var objectMapper = createObjectMapper()
					.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			try {
				if(LOG.isDebugEnabled()){
					LOG.debug(String.format("NewPost JSON API Response: " +
							"\n data : [%s] " +
							"\n type: [%s]", StringEscapeUtils.unescapeJava(data), typeReference.getType().getTypeName()));
				}
				return objectMapper.readValue(data, typeReference);
			} catch (IOException e) {
				LOG.error("Can't parse data: [" + data + "]", e);
			}
			return null;
		};
	}

	protected ObjectMapper createObjectMapper(){
		return new ObjectMapper();
	}
}
