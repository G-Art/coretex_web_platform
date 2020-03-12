package com.coretex.commerce.admin.config.interceptors.extensions.impl;

import com.coretex.commerce.admin.config.interceptors.extensions.BeforeViewHandlerInterceptorExtension;
import com.coretex.commerce.facades.LocaleFacade;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.stream.Collectors;

@Component
public class PageInfoInterceptorExtension implements BeforeViewHandlerInterceptorExtension {

	@Resource
	private LocaleFacade localeFacade;

	@Override
	public void beforeView(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
		modelAndView.addObject("requestPath", request.getRequestURI());
		modelAndView.addObject("applicationBaseUrl", getApplicationBaseUrl(request));

		modelAndView.addObject("locales", localeFacade.getAll().collect(Collectors.toList()));
	}

	private String getApplicationBaseUrl(final HttpServletRequest request){
		StringBuffer url = request.getRequestURL();
		String uri = request.getRequestURI();
		return url.substring(0, url.length() - uri.length());
	}
}
