package com.coretex.commerce.admin.postprocessors;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class EncodingPostProcessor implements BeanPostProcessor {

	private Charset defaultCharset = StandardCharsets.UTF_8;

	public Object postProcessBeforeInitialization(Object bean, String name)
			throws BeansException {
		if (bean instanceof RequestMappingHandlerAdapter) {
			List<HttpMessageConverter<?>> convs = ((RequestMappingHandlerAdapter) bean).getMessageConverters();
			for (HttpMessageConverter<?> conv : convs) {
				if (conv instanceof StringHttpMessageConverter) {
					((StringHttpMessageConverter) conv).setDefaultCharset(defaultCharset);
				}
			}
		}
		return bean;
	}

	public Object postProcessAfterInitialization(Object bean, String name)
			throws BeansException {
		return bean;
	}

	public void setDefaultCharset(Charset defaultCharset) {
		this.defaultCharset = defaultCharset;
	}
}