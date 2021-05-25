package com.coretex.server.listeners;

import com.coretex.server.PluginAccessor;
import com.coretex.server.data.AbstractProject;
import com.coretex.server.data.ModuleProject;
import com.coretex.server.data.WebProject;
import com.coretex.server.spring.CortexEnvironmentInitializer;
import org.apache.catalina.loader.ParallelWebappClassLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

import static java.lang.String.format;
import static java.util.function.Predicate.not;

/**
 * @author Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 *         create by 07-12-2016
 *
 *         This listener will be loaded automatically for each of web modules
 */
public class CoretexContextLoaderListener extends ContextLoaderListener {

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
    protected WebApplicationContext createWebApplicationContext(ServletContext servletContext) {
        return super.createWebApplicationContext(servletContext);
    }

    @Override
    protected ApplicationContext loadParentContext(ServletContext servletContext) {
        ApplicationContext applicationContext = null;
        try {
            var classLoader = servletContext.getClassLoader();
            var moduleProject = getCoreModule(classLoader);
            applicationContext = getSpringContext(classLoader, moduleProject, project -> {
                var configLocation = project.getSpringXml().getAbsolutePath().substring(project.getPath().getAbsolutePath().length() + "/resources/".length());
                var classPathXmlApplicationContext = new ClassPathXmlApplicationContext(new String[]{format("classpath:%s", configLocation)}, false);
                new CortexEnvironmentInitializer().initialize(classPathXmlApplicationContext);
                classPathXmlApplicationContext.refresh();
                return classPathXmlApplicationContext;
            });
        } catch (Exception e) {
            LOG.error("Application config creation exception", e);
        }

        return applicationContext;
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

        setContextInitializers(new CortexEnvironmentInitializer(webProject));
        wac.setConfigLocations(configLocation.toArray(new String[configLocation.size()]));

//        var servletContext = wac.getServletContext();
//
//        var servlet = servletContext.addServlet("dispatcher", new DispatcherServlet(wac));
//        servlet.setLoadOnStartup(1);
//        servlet.addMapping("/");

        super.customizeContext(sc, wac);
    }

    private Set<String> getAllSpringConfig(WebProject webProject) {
        Set<String> configs = new HashSet<>();
        collectSpringXml(webProject, configs);
        return configs;
    }

    private void collectSpringXml(AbstractProject project, Set<String> configs) {
        if(!CollectionUtils.isEmpty(project.getRelatedProjects())){
            project.getRelatedProjects().stream()
                    .filter(not(AbstractProject::isCore)).forEach(p -> collectSpringXml(p, configs));
        }
        if(project.getSpringXml().exists() && !project.isCore()){
            configs.add(project.getSpringXml().getAbsolutePath().substring(project.getPath().getAbsolutePath().length()+"/resources/".length()));
        }else {
            LOG.error(format("Error: file [%s] doesn't exist", project.getSpringXml().getName()));
        }
    }


    private WebProject findWebProject(ConfigurableWebApplicationContext applicationContext) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return findWebProject(applicationContext.getClassLoader());
    }

    private WebProject findWebProject(ClassLoader classLoader) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Object loader = retrieveLoader(classLoader);
        return getProject(classLoader, loader);
    }

    private ModuleProject getCoreModule(ClassLoader classLoader) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Object loader = retrieveLoader(classLoader);
        Class loaderClass = loader.getClass();

        Method getProjectMethod = loaderClass.getMethod("getCoreModule", ClassLoader.class);
        return (ModuleProject) getProjectMethod.invoke(loader, classLoader);
    }

    private ApplicationContext getSpringContext(ClassLoader classLoader, AbstractProject abstractProject, Function<AbstractProject, ApplicationContext> contextFunction) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Object loader = retrieveLoader(classLoader);
        Class loaderClass = loader.getClass();

        Method getProjectMethod = loaderClass.getMethod("getSpringContext", ClassLoader.class, Object.class, Function.class);
        return (ApplicationContext) getProjectMethod.invoke(loader, classLoader, abstractProject.getObject(), contextFunction);
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
