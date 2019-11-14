package com.coretex.web.controllers;

import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;

public abstract class AbstractController {

	@ModelAttribute("requestPath")
	public String getRequestPath(final HttpServletRequest request){
		return request.getRequestURI();
	}
}
