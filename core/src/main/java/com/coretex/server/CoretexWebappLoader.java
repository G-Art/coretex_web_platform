package com.coretex.server;

import com.coretex.server.data.AbstractProject;
import org.apache.catalina.loader.WebappLoader;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Function;

/**
 * @author Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 *         create by 21-01-2017
 */
public class CoretexWebappLoader extends WebappLoader {

    private Object project;
    private Object coreProject;
    private Object projectDataHandler;
    private Object accessorBuilder;

    public CoretexWebappLoader(Object converter, Object accessorBuilder, Object project, Object coreProject, ClassLoader parent) {
        super(parent);
//        this.setDelegate(true);
        this.project = project;
        this.coreProject = coreProject;
        this.projectDataHandler = converter;
        this.accessorBuilder = accessorBuilder;
    }

    public Object getProject(ClassLoader loader) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method apply = projectDataHandler.getClass().getMethod("loadProject", Object.class, Object.class);
        return apply.invoke(projectDataHandler, loader, project);
    }

    public Object getCoreModule(ClassLoader loader) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method apply = projectDataHandler.getClass().getMethod("loadProject", Object.class, Object.class);
        return apply.invoke(projectDataHandler, loader, coreProject);
    }

    public ApplicationContext getSpringContext(ClassLoader loader, Object project, Function<AbstractProject, ApplicationContext> applicationContextSupplier) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method apply = projectDataHandler.getClass().getMethod("loadApplicationContext", Object.class, Object.class, Function.class);
        return (ApplicationContext) apply.invoke(projectDataHandler, loader, project, applicationContextSupplier);
    }

    public Object getPluginAccessor(ClassLoader loader) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method apply = accessorBuilder.getClass().getMethod("build", Object.class);
        return apply.invoke(accessorBuilder, loader);
    }
}
