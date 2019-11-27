package com.coretex.commerce.admin.config.interceptors;

import com.coretex.commerce.admin.config.interceptors.extensions.BeforeViewHandlerInterceptorExtension;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class BeforeViewHandlerInterceptor extends HandlerInterceptorAdapter
{
	private List<BeforeViewHandlerInterceptorExtension> extensions;

	public BeforeViewHandlerInterceptor(List<BeforeViewHandlerInterceptorExtension> extensions) {
		this.extensions = extensions;
	}

	protected List<BeforeViewHandlerInterceptorExtension> getExtensions()
	{
		return extensions;
	}

	@Override
	public void postHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final ModelAndView modelAndView) throws Exception
	{
		if (modelAndView != null && !isIncludeRequest(request) && isSupportedView(modelAndView))
		{
			for (final BeforeViewHandlerInterceptorExtension beforeViewHandler : getExtensions())
			{
				beforeViewHandler.beforeView(request, response, modelAndView);
			}
		}
	}

	protected boolean isIncludeRequest(final HttpServletRequest request)
	{
		return request.getAttribute("javax.servlet.include.request_uri") != null;
	}

	protected boolean isSupportedView(final ModelAndView modelAndView)
	{
		return modelAndView.getViewName() != null && !isRedirectView(modelAndView);
	}

	protected boolean isRedirectView(final ModelAndView modelAndView)
	{
		final String viewName = modelAndView.getViewName();
		return viewName != null && viewName.startsWith("redirect:");
	}
}
