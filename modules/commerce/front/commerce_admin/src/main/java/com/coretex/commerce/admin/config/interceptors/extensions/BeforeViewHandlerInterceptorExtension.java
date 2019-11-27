package com.coretex.commerce.admin.config.interceptors.extensions;

import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface BeforeViewHandlerInterceptorExtension {
	void beforeView(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView);
}
