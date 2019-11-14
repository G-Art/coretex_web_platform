package com.coretex.shop.application;

import javax.servlet.http.HttpServletRequest;

import com.coretex.items.commerce_core_model.MerchantStoreItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.shop.utils.LanguageUtils;

@Component
public class LanguageArgumentResolver implements HandlerMethodArgumentResolver {

	@Autowired
	private LanguageUtils languageUtils;
	@Autowired
	private MerchantStoreArgumentResolver merchantStoreArgumentResolver;

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterType().equals(LanguageItem.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
								  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);

		MerchantStoreItem merchantStore = getMerchantStore(parameter, mavContainer, webRequest,
				binderFactory);
		return languageUtils.getRESTLanguage(request, merchantStore);
	}

	private MerchantStoreItem getMerchantStore(MethodParameter parameter,
											   ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
											   WebDataBinderFactory binderFactory) throws Exception {
		return (MerchantStoreItem) merchantStoreArgumentResolver.resolveArgument(parameter, mavContainer, webRequest, binderFactory);
	}
}
