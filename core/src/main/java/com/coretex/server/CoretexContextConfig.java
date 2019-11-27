package com.coretex.server;

import com.coretex.core.activeorm.services.SearchResult;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleEvent;
import org.apache.catalina.startup.ContextConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 *         create by 29-03-2017
 */
public class CoretexContextConfig extends ContextConfig {
    private Logger LOG = LoggerFactory.getLogger(SearchResult.class);

    private Context context;
    private ContextConfigModifier configModifier;

    @Override
    public void lifecycleEvent(LifecycleEvent event) {
        context = (Context)event.getLifecycle();
        super.lifecycleEvent(event);
    }

    @Override
    protected synchronized void beforeStart() {
        configModifier.modifyBeforeStart(context);
        super.beforeStart();
    }

    public void setConfigModifier(ContextConfigModifier configModifier) {
        this.configModifier = configModifier;
    }
}
