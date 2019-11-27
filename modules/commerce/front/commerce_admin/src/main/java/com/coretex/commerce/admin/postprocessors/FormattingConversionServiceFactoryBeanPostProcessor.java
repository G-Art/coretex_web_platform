package com.coretex.commerce.admin.postprocessors;

import com.coretex.commerce.admin.converters.StringToGenericItemDataConverter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class FormattingConversionServiceFactoryBeanPostProcessor implements BeanPostProcessor {
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

		if(bean instanceof FormattingConversionServiceFactoryBean){
			((FormattingConversionServiceFactoryBean) bean).setConverters(Set.of(new StringToGenericItemDataConverter()));
		}
		return bean;
	}
}
