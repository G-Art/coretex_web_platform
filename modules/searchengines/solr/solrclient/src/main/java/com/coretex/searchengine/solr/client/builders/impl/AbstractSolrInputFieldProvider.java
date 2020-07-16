package com.coretex.searchengine.solr.client.builders.impl;

import com.coretex.searchengine.solr.client.builders.SolrInputFieldProvider;
import com.coretex.searchengine.solr.client.providers.SolrDocFieldConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.BeanNameAware;

import java.util.Locale;

public abstract class AbstractSolrInputFieldProvider<T> implements SolrInputFieldProvider<T>, BeanNameAware {

	private final SolrDocFieldConfig config;
	private String beanName;

	public AbstractSolrInputFieldProvider(SolrDocFieldConfig config) {
		this.config = config;
	}


	protected String createFieldName(Locale locale) {
		if(config.isLocaleBeforeType()){
			return config.getName() + String.format("_%s_%s", locale.toLanguageTag(), getSolrFieldType()) + (isMultiValue() ? "_mv" : StringUtils.EMPTY);
		}
		return config.getName() + String.format("_%s_%s", getSolrFieldType(), locale.toLanguageTag()) + (isMultiValue() ? "_mv" : StringUtils.EMPTY);
	}

	protected String createFieldName() {
		return config.getName() + String.format("_%s", getSolrFieldType()) + (isMultiValue() ? "_mv" : StringUtils.EMPTY);
	}
	@Override
	public String fieldName() {
		return config.getName();
	}

	public boolean isMultiValue() {
		return config.isMultiValue();
	}


	public String getSolrFieldType() {
		return config.getType();
	}

	public boolean isLocaleBeforeType() {
		return config.isLocaleBeforeType();
	}

	@Override
	public void setBeanName(String name) {
		this.beanName = name;
	}

	public String getBeanName() {
		return beanName;
	}
}
