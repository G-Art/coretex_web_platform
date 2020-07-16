package com.coretex.searchengine.solr.client.builders.impl;

import com.coretex.core.general.utils.AttributeTypeUtils;
import com.coretex.core.services.bootstrap.meta.MetaTypeProvider;
import com.coretex.items.core.GenericItem;
import com.coretex.items.core.MetaAttributeTypeItem;
import com.coretex.searchengine.solr.client.providers.SolrDocFieldConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Objects;

public class AdvancedGenericItemConfiguredSolrInputFieldProvider extends AbstractSolrInputFieldProvider<GenericItem> {

	private final Logger LOG = LoggerFactory.getLogger(AdvancedGenericItemConfiguredSolrInputFieldProvider.class);
	private String pathSeparator = ".";

	@Resource
	private MetaTypeProvider metaTypeProvider;

	private final String itemFieldPath;
	private String[] splittedItemFieldPath;

	public AdvancedGenericItemConfiguredSolrInputFieldProvider(SolrDocFieldConfig config, String itemFieldPath) {
		super(config);
		this.itemFieldPath = itemFieldPath;
	}

	public AdvancedGenericItemConfiguredSolrInputFieldProvider(SolrDocFieldConfig config, String itemFieldPath, String pathSeparator) {
		this(config, itemFieldPath);
		this.pathSeparator = pathSeparator;
	}

	@Override
	public void setSolrInputField(SolrInputDocument document, GenericItem source) {
		Assert.notNull(source, "Source item is null");

		if (Objects.isNull(splittedItemFieldPath)) {
			splittedItemFieldPath = StringUtils.split(itemFieldPath, pathSeparator);
		}

		Pair<GenericItem, MetaAttributeTypeItem> itemAttributePair = getAttributeByPath(source, splittedItemFieldPath);

		if (Objects.nonNull(itemAttributePair)) {
			GenericItem item = itemAttributePair.getLeft();
			MetaAttributeTypeItem attribute = itemAttributePair.getRight();
			if (attribute.getLocalized()) {
				var localizedValues = item.getItemContext().getLocalizedValues(attribute.getAttributeName());
				localizedValues.forEach((locale, o) -> document.addField(createFieldName(locale), o));
			} else {
				if (AttributeTypeUtils.isRegularTypeAttribute(attribute)) {
					document.addField(createFieldName(), String.valueOf(item.getAttributeValue(attribute.getAttributeName())));
				} else {
					LOG.warn(String.format("SolrInputFieldProvider is not support relations attributed [%s]", attribute.getAttributeName()));
				}
			}
		} else {
			LOG.warn(String.format("Item field path is not reachable or incorrect [%s]", itemFieldPath));
		}


	}

	private Pair<GenericItem, MetaAttributeTypeItem> getAttributeByPath(GenericItem source, String[] splittedItemFieldPath) {
		int position = 0;
		try {
			var attributeValue = metaTypeProvider.findAttribute(source.getMetaType().getTypeCode(), splittedItemFieldPath[position]);
			if (Objects.isNull(attributeValue)) {
				return null;
			}
			if (splittedItemFieldPath.length > position + 1) {
				return getAttributeByPath((GenericItem) source.getAttributeValue(splittedItemFieldPath[position]), splittedItemFieldPath, position + 1);
			}
			return Pair.of(source, attributeValue);
		} catch (Exception any) {
			LOG.warn(String.format("Item [%s] attribute path [%s] is not reachable or not exist", source.getItemContext().getTypeCode(), itemFieldPath));
			return null;
		}
	}

	private Pair<GenericItem, MetaAttributeTypeItem> getAttributeByPath(GenericItem source, String[] splittedItemFieldPath, int position) {

		var attributeValue = metaTypeProvider.findAttribute(source.getMetaType().getTypeCode(), splittedItemFieldPath[position]);
		if (splittedItemFieldPath.length > position + 1) {
			return getAttributeByPath((GenericItem) source.getAttributeValue(splittedItemFieldPath[position]), splittedItemFieldPath, position + 1);
		}
		return Pair.of(source, attributeValue);

	}

}
