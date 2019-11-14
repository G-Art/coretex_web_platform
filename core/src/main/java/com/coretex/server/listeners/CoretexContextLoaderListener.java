package com.coretex.server.listeners;

import com.coretex.server.PluginAccessor;
import com.coretex.server.data.Project;
import com.coretex.server.data.WebProject;
import com.coretex.server.spring.CortexEnvironmentInitializer;
import org.apache.catalina.loader.ParallelWebappClassLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static java.lang.String.format;

/**
 * @author Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 *         create by 07-12-2016
 *
 *         This listener will be loaded automatically for each of web modules
 */
public class CoretexContextLoaderListener extends ContextLoaderListener implements ServletContextListener {

    private Logger LOG = LoggerFactory.getLogger(CoretexContextLoaderListener.class);

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        LOG.debug("Initializing " + servletContextEvent.getServletContext().getContextPath());
        super.contextInitialized(servletContextEvent);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        LOG.debug("Destroying " + servletContextEvent.getServletContext().getContextPath());
        super.contextDestroyed(servletContextEvent);
    }

    @Override
    public WebApplicationContext initWebApplicationContext(ServletContext servletContext) {
        return super.initWebApplicationContext(servletContext);
    }

    @Override
    protected void customizeContext(ServletContext sc, ConfigurableWebApplicationContext wac) {
        Set<String> configLocation = new HashSet<>();
        Collections.addAll(configLocation, wac.getConfigLocations());

        WebProject webProject = null;
        try {
            webProject = findWebProject(wac);
        } catch (Exception e) {
            LOG.error("No one web project isn't available", e);
        }

        wac.addBeanFactoryPostProcessor(beanFactory -> {
            try {
                PluginAccessor accessor = getPluginAccessor(wac);
                beanFactory.registerSingleton("pluginAccessor", accessor);
            } catch (Exception e) {
                LOG.error("Plugin accessor cant be received", e);
            }
        });
        getAllSpringConfig(webProject).stream()
                                      .map(s -> format("classpath:%s", s))
                                      .forEach(configLocation::add);

        setContextInitializers(new CortexEnvironmentInitializer());
        wac.setConfigLocations(configLocation.toArray(new String[configLocation.size()]));
        var servletContext = wac.getServletContext();

        var servlet = servletContext.addServlet("dispatcher", new DispatcherServlet(wac));
        servlet.setLoadOnStartup(1);
        servlet.addMapping("/");

        super.customizeContext(sc, wac);
    }

    private Set<String> getAllSpringConfig(WebProject webProject) {
        Set<String> configs = new HashSet<>();
        collectSpringXml(webProject, configs);
        return configs;
    }

    private void collectSpringXml(Project project, Set<String> configs) {
        if(!CollectionUtils.isEmpty(project.getRelatedProjects())){
            project.getRelatedProjects().forEach(p -> collectSpringXml(p, configs));
        }
        if(project.getSpringXml().exists()){
            configs.add(project.getSpringXml().getAbsolutePath().substring(project.getPath().getAbsolutePath().length()+"/resources/".length()));
        }else {
            LOG.error(format("Error: file [%s] doesn't exist", project.getSpringXml().getName()));
        }
    }


    private WebProject findWebProject(ConfigurableWebApplicationContext applicationContext) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Object loader = retrieveLoader(applicationContext.getClassLoader());
        return getProject(applicationContext.getClassLoader(), loader);
    }

    private Object retrieveLoader(ClassLoader classLoader){
        return ((ParallelWebappClassLoader)classLoader).getResources().getContext().getLoader();
    }

    @SuppressWarnings("unchecked")
    private WebProject getProject(ClassLoader classLoader, Object loader) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class loaderClass = loader.getClass();

        Method getProjectMethod = loaderClass.getMethod("getProject", ClassLoader.class);
        return (WebProject) getProjectMethod.invoke(loader, classLoader);
    }

    @SuppressWarnings("unchecked")
    private PluginAccessor getPluginAccessor(ConfigurableWebApplicationContext applicationContext) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Object loader = retrieveLoader(applicationContext.getClassLoader());
        Class loaderClass = loader.getClass();
        Method getProjectMethod = loaderClass.getMethod("getPluginAccessor", ClassLoader.class);
        return (PluginAccessor) getProjectMethod.invoke(loader, applicationContext.getClassLoader());
    }
}
