package com.coretex.searchengine.solr.client.builders.impl;

import com.coretex.core.general.utils.AttributeTypeUtils;
import com.coretex.core.services.bootstrap.meta.MetaTypeProvider;
import com.coretex.items.core.GenericItem;
import com.coretex.searchengine.solr.client.builders.SolrInputFieldProvider;
import com.google.common.base.CaseFormat;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

public class GenericItemConfiguredSolrInputFieldProvider implements SolrInputFieldProvider<GenericItem> {

	private Logger LOG = LoggerFactory.getLogger(GenericItemConfiguredSolrInputFieldProvider.class);

	@Resource
	private MetaTypeProvider metaTypeProvider;

	private final String fieldName;


	public GenericItemConfiguredSolrInputFieldProvider(String fieldName) {
		this.fieldName = fieldName;
	}

	@Override
	public void setSolrInputField(SolrInputDocument document, GenericItem source) {

		var attribute = metaTypeProvider.findAttribute(source.getMetaType().getTypeCode(), fieldName);
		if (attribute.getLocalized()) {
			LOG.warn(String.format("Localized type is not supported yet [%s]", fieldName));
		} else {
			if(AttributeTypeUtils.isRegularTypeAttribute(attribute)){
				document.addField(createFieldName(), source.getAttributeValue(fieldName));
			}else{
				LOG.warn(String.format("SolrInputFieldProvider is not support relations attributed [%s]", fieldName));
			}
		}

	}

	private String createFieldName() {
		return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fieldName());
	}


	@Override
	public String fieldName() {
		return fieldName;
	}
}
