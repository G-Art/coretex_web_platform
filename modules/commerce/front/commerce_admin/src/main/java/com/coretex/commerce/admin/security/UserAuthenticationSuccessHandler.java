package com.coretex.commerce.admin.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserAuthenticationSuccessHandler extends AbstractAuthenticationSuccessHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserAuthenticationSuccessHandler.class);

	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();


	public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
		this.redirectStrategy = redirectStrategy;
	}

	protected RedirectStrategy getRedirectStrategy() {
		return redirectStrategy;
	}

	@Override
	protected void redirectAfterSuccess(HttpServletRequest request, HttpServletResponse response) throws Exception {
		redirectStrategy.sendRedirect(request, response, "/home");
	}

}
