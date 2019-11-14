package com.coretex.server;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;

/**
 * @author Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 *         create by 17-02-2017
 */

public final class ApplicationContextProvider {
    private static ApplicationContext applicationContext;

    public static void setApplicationContext(ApplicationContext applicationContext) {
        ApplicationContextProvider.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        if(ContextLoader.getCurrentWebApplicationContext() == null){
            return applicationContext;
        }
        return ContextLoader.getCurrentWebApplicationContext();
    }

}