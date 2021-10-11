package com.coretex.server;

import com.coretex.core.services.bootstrap.impl.CoretexContext;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;

/**
 * @author Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 * create by 17-02-2017
 */

public final class ApplicationContextProvider {
	private static ApplicationContext applicationContext;
	private static CoretexContext coretexContext;

	public static void setApplicationContext(ApplicationContext applicationContext) {
		ApplicationContextProvider.applicationContext = applicationContext;
	}

	public static ApplicationContext getApplicationContext() {
		if (ContextLoader.getCurrentWebApplicationContext() == null) {
			return applicationContext;
		}
		return ContextLoader.getCurrentWebApplicationContext();
	}

	public static CoretexContext getCoretexContext() {
		if (coretexContext == null) {
			coretexContext = getBean(CoretexContext.class);
		}
		return coretexContext;
	}

	public static <T> T getBean(Class<T> requiredType) {
		return getApplicationContext().getBean(requiredType);
	}

	public static <T> T getBean(String name, Class<T> requiredType) {
		return getApplicationContext().getBean(name, requiredType);
	}
}