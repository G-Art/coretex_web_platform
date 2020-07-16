package com.coretex.searchengine.solr.client.builders.impl;

import com.coretex.core.general.utils.AttributeTypeUtils;
import com.coretex.core.general.utils.ItemUtils;
import com.coretex.core.services.bootstrap.meta.MetaTypeProvider;
import com.coretex.items.core.GenericItem;
import com.coretex.searchengine.solr.client.providers.SolrDocFieldConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Objects;

public class GenericItemConfiguredSolrInputFieldProvider extends AbstractSolrInputFieldProvider<GenericItem> {

	private final Logger LOG = LoggerFactory.getLogger(GenericItemConfiguredSolrInputFieldProvider.class);

	@Resource
	private MetaTypeProvider metaTypeProvider;

	private String itemFieldName;

	public GenericItemConfiguredSolrInputFieldProvider(SolrDocFieldConfig config) {
		super(config);
	}

	@Override
	public void setSolrInputField(SolrInputDocument document, GenericItem source) {
		Assert.notNull(source, "Source item is null");

		if (StringUtils.isBlank(itemFieldName)) {
			itemFieldName = fieldName();
		}

		var attribute = metaTypeProvider.findAttribute(source.getMetaType().getTypeCode(), itemFieldName);
		if (Objects.nonNull(attribute)) {
			if (attribute.getLocalized()) {
				var localizedValues = source.getItemContext().getLocalizedValues(itemFieldName);
				localizedValues.forEach((locale, o) -> document.addField(createFieldName(locale), o));
			} else {
				if (AttributeTypeUtils.isRegularTypeAttribute(attribute)) {
					document.addField(createFieldName(), source.getAttributeValue(itemFieldName));
				} else {
					LOG.warn(String.format("SolrInputFieldProvider is not support relations attributed [%s]", itemFieldName));
				}
			}
		} else {
			LOG.warn(String.format("Attribute [name: %s] is not available for type [%s]", itemFieldName, ItemUtils.getTypeCode(source)));
		}

	}

	public void setItemFieldName(String itemFieldName) {
		this.itemFieldName = itemFieldName;
	}

}
