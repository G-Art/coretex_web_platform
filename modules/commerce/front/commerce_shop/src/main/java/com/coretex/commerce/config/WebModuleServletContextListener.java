package com.coretex.commerce.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.server.reactive.TomcatHttpHandlerAdapter;
import org.springframework.web.server.adapter.WebHttpHandlerBuilder;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import static org.springframework.web.context.ContextLoader.getCurrentWebApplicationContext;

@WebListener
public class WebModuleServletContextListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		var currentWebApplicationContext = getCurrentWebApplicationContext();
		refreshApplicationContext(currentWebApplicationContext);
		var handler = WebHttpHandlerBuilder
				.applicationContext(currentWebApplicationContext)
				.build();

		var handlerAdapter = new TomcatHttpHandlerAdapter(handler);

		var container = sce.getServletContext();
		var servlet = container.addServlet(
				"dispatcher", handlerAdapter);
		servlet.setLoadOnStartup(1);
		servlet.setAsyncSupported(true);
		servlet.addMapping("/");
	}

	protected void refreshApplicationContext(ApplicationContext context) {
		if (context instanceof ConfigurableApplicationContext) {
			ConfigurableApplicationContext cac = (ConfigurableApplicationContext) context;
			if (!cac.isActive()) {
				cac.refresh();
			}
		}
	}
}
