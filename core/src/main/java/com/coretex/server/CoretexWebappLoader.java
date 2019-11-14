package com.coretex.server;

import org.apache.catalina.loader.WebappLoader;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 *         create by 21-01-2017
 */
public class CoretexWebappLoader extends WebappLoader {

    private Object project;
    private Object projectConverter;
    private Object accessorBuilder;

    public CoretexWebappLoader(Object converter, Object accessorBuilder, Object project, ClassLoader parent) {
        super(parent);
        this.project = project;
        this.projectConverter = converter;
        this.accessorBuilder = accessorBuilder;
    }

    public Object getProject(ClassLoader loader) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method apply = projectConverter.getClass().getMethod("apply", Object.class, Object.class);
        return apply.invoke(projectConverter, loader, project);
    }

    public Object getPluginAccessor(ClassLoader loader) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method apply = accessorBuilder.getClass().getMethod("build", Object.class);
        return apply.invoke(accessorBuilder, loader);
    }
}
