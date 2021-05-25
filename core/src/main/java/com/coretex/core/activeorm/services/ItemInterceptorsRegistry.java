package com.coretex.core.activeorm.services;

import com.coretex.core.activeorm.interceptors.OnCreateInterceptor;
import com.coretex.core.activeorm.interceptors.OnLoadInterceptor;
import com.coretex.core.activeorm.interceptors.OnRemoveInterceptor;
import com.coretex.core.activeorm.interceptors.OnSavePrepareInterceptor;
import com.coretex.core.activeorm.interceptors.OnSavedInterceptor;
import org.springframework.context.ApplicationContextAware;

import java.util.Collection;

public interface ItemInterceptorsRegistry extends ApplicationContextAware {

	Collection<OnCreateInterceptor> getCreateInterceptors(String type);

	Collection<OnLoadInterceptor> getLoadInterceptors(String type);

	Collection<OnRemoveInterceptor> getRemoveInterceptors(String type);

	Collection<OnSavePrepareInterceptor> getSavePrepareInterceptors(String type);

	Collection<OnSavedInterceptor> getSavedInterceptors(String type);
}
