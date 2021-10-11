package com.coretex.core.services.bootstrap.dialect.postgres;

import com.coretex.core.activeorm.query.operations.contexts.SelectItemAttributeOperationConfigContext;
import com.coretex.core.activeorm.query.specs.select.SelectItemAttributeOperationSpec;
import com.coretex.core.general.utils.AttributeTypeUtils;
import com.coretex.core.services.bootstrap.dialect.QueryBuilder;
import com.coretex.core.services.bootstrap.impl.CoretexContext;
import com.coretex.core.services.items.context.ItemContext;
import com.coretex.core.services.items.context.provider.strategies.impl.GeneralRegularAttributeLoadValueStrategy;
import com.coretex.items.core.MetaAttributeTypeItem;
import com.coretex.meta.AbstractGenericItem;
import com.coretex.server.ApplicationContextProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PostgresRegularAttributeQueryBuilder implements QueryBuilder<SelectItemAttributeOperationConfigContext> {

	private Logger LOG = LoggerFactory.getLogger(GeneralRegularAttributeLoadValueStrategy.class);

	private final PostgresLocalizedItemAttributeQueryBuilder localizedItemAttributeQueryBuilder = new PostgresLocalizedItemAttributeQueryBuilder();

	private static final String SELECT_REGULAR_FIELD_BY_UUID_TEMPLATE = "select %s from \"%s\" as item where item.uuid = :uuid";

	@Override
	public String createQuery(SelectItemAttributeOperationConfigContext context) {
		SelectItemAttributeOperationSpec operationSpec = context.getOperationSpec();
		MetaAttributeTypeItem attribute = operationSpec.getAttribute();
		ItemContext ctx = operationSpec.getCtx();
		if (attribute.getLocalized()) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Prepare query for localized attribute Postgres::[{}]", attribute.getAttributeName());
			}
			return localizedItemAttributeQueryBuilder.createQuery(context);
		} else {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Prepare query for attribute Postgres::[{}]", attribute.getAttributeName());
			}
			var loadedAttributes = ctx.loadedAttributes();
			CoretexContext coretexContext = ApplicationContextProvider.getCoretexContext();
			var unloadedAttributes = coretexContext.getAllAttributes(ctx.getTypeCode())
					.values()
					.stream()
					.filter(AttributeTypeUtils::isRegularTypeAttribute)
					.filter(attr -> !attr.getLocalized())
					.filter(attr -> !loadedAttributes.contains(attr.getAttributeName()))
					.collect(Collectors.toList());
			return createQuery(unloadedAttributes, ctx);
		}
	}

	@Override
	public void prepareValueData(SelectItemAttributeOperationConfigContext context) {
		SelectItemAttributeOperationSpec operationSpec = context.getOperationSpec();
		MetaAttributeTypeItem attribute = operationSpec.getAttribute();
		ItemContext ctx = operationSpec.getCtx();
		if (attribute.getLocalized()) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Prepare query value data for localized attribute Postgres::[{}]", attribute.getAttributeName());
			}
			localizedItemAttributeQueryBuilder.prepareValueData(context);
		} else {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Prepare query value data for attribute Postgres::[{}]", attribute.getAttributeName());
			}
			operationSpec.addQueryParameters(Collections.singletonMap(AbstractGenericItem.UUID, ctx.getUuid()));
		}
	}

	protected String createQuery(List<MetaAttributeTypeItem> unloadedAttributes, ItemContext ctx) {
		var columns = unloadedAttributes.stream()
				.map(attr -> String.format("item.%s", attr.getAttributeName()))
				.collect(Collectors.joining(","));
		return String.format(SELECT_REGULAR_FIELD_BY_UUID_TEMPLATE, columns, ctx.getTypeCode());
	}
}
