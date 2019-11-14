package com.coretex.shop.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class CorsFilter extends HandlerInterceptorAdapter {

	public CorsFilter() {

	}

	/**
	 * Allows public web services to work from remote hosts
	 */
	public boolean preHandle(
			HttpServletRequest request,
			HttpServletResponse response,
			Object handler) throws Exception {

		HttpServletResponse httpResponse = response;

		String origin = "*";
		if (!StringUtils.isBlank(request.getHeader("origin"))) {
			origin = request.getHeader("origin");
		}

		httpResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
		httpResponse.setHeader("Access-Control-Allow-Headers", "X-Auth-Token, ContentItem-Type, Authorization");
		httpResponse.setHeader("Access-Control-Allow-Origin", origin);

		return true;

	}
}
