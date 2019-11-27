package com.coretex.commerce.admin.config.interceptors.extensions.impl;

import com.coretex.commerce.admin.config.interceptors.extensions.BeforeViewHandlerInterceptorExtension;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class PageInfoInterceptorExtension implements BeforeViewHandlerInterceptorExtension {

	@Override
	public void beforeView(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
		modelAndView.addObject("requestPath", request.getRequestURI());
		modelAndView.addObject("applicationBaseUrl", getApplicationBaseUrl(request));
	}

	private String getApplicationBaseUrl(final HttpServletRequest request){
		StringBuffer url = request.getRequestURL();
		String uri = request.getRequestURI();
		return url.substring(0, url.length() - uri.length());
	}
}
