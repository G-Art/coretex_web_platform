package com.coretex.web.controllers;

import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;

public abstract class AbstractController {

	@ModelAttribute("requestPath")
	public String getRequestPath(final HttpServletRequest request){
		return request.getRequestURI();
	}

	@ModelAttribute("applicationBaseUrl")
	public String getApplicationBaseUrl(final HttpServletRequest request){
		StringBuffer url = request.getRequestURL();
		String uri = request.getRequestURI();
		return url.substring(0, url.length() - uri.length());
	}
}
