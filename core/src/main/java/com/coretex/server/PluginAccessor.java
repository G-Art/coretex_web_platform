package com.coretex.server;

import java.util.Map;

/**
 * @author Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 *         create by 23-03-2017
 */
public interface PluginAccessor {

    Map<String, String> allWebContexts();

    void reloadWebContext(String contextPath);
}
