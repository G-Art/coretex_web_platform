package com.coretex.commerce.admin.config;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class WebModuleServletContextListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		var container = sce.getServletContext();
		var servlet = container.addServlet(
				"dispatcher", new DispatcherServlet(ContextLoader.getCurrentWebApplicationContext()));
		servlet.setLoadOnStartup(1);
		servlet.addMapping("/");

	}

}
