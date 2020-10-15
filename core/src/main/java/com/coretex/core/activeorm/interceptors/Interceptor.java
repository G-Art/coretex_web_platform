package com.coretex.core.activeorm.interceptors;

import com.coretex.meta.AbstractGenericItem;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Component
public @interface Interceptor {
	Class<? extends AbstractGenericItem>[] items();
}
