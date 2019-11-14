package com.coretex.server;

import org.apache.catalina.Context;

/**
 * @author Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 *         create by 29-03-2017
 */
public interface ContextConfigModifier {
    void modifyBeforeStart(Context context);
}
