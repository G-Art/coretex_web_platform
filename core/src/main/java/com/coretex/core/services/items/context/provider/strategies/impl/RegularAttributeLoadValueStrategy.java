package com.coretex.core.services.items.context.provider.strategies.impl;

import com.coretex.core.activeorm.exceptions.SearchException;
import com.coretex.core.activeorm.extractors.CoretexReactiveResultSetExtractor;
import com.coretex.core.activeorm.factories.RowMapperFactory;
import com.coretex.core.activeorm.query.specs.select.SelectOperationSpec;
import com.coretex.core.activeorm.services.ReactiveSearchResult;
import com.coretex.core.general.utils.AttributeTypeUtils;
import com.coretex.core.services.items.context.ItemContext;
import com.coretex.core.services.items.context.provider.strategies.AbstractLoadAttributeValueStrategy;
import com.coretex.items.core.MetaAttributeTypeItem;
import com.coretex.items.core.RegularTypeItem;
import com.coretex.meta.AbstractGenericItem;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RegularAttributeLoadValueStrategy extends AbstractLoadAttributeValueStrategy {
	private Logger LOG = LoggerFactory.getLogger(RegularAttributeLoadValueStrategy.class);

	private static final String SELECT_REGULAR_FIELD_BY_UUID_TEMPLATE = "select %s from \"%s\" as item where item.uuid = :uuid";

	@Override
	public Object load(ItemContext ctx, MetaAttributeTypeItem attribute) {
		var loadedAttributes = ctx.loadedAttributes();
		var unloadedAttributes = getCortexContext().getAllAttributes(ctx.getTypeCode())
				.values()
				.stream()
				.filter(AttributeTypeUtils::isRegularTypeAttribute)
				.filter(attr -> !attr.getLocalized())
				.filter(attr -> !loadedAttributes.contains(attr.getAttributeName()))
				.collect(Collectors.toList());
		CoretexReactiveResultSetExtractor<List<Object>> extractor = new CoretexReactiveResultSetExtractor<>(getCortexContext());
		extractor.setMapperFactorySupplier(() -> creteMapperFactory(unloadedAttributes));
		ReactiveSearchResult<List<Object>> searchResult = getOperationExecutor().execute(
				new SelectOperationSpec(createQuery(unloadedAttributes, ctx), createParameters(ctx), extractor).createOperationContext());

		return processResult(searchResult, unloadedAttributes, attribute, ctx);
	}

	private RowMapperFactory creteMapperFactory(List<MetaAttributeTypeItem> unloadedAttributes) {
		return new RowMapperFactory() {
			@Override
			public <T> RowMapper<T> createMapper(Class<T> ignore) {
				return (rs, rowNum) -> {

					List<Object> resultRow = Lists.newArrayList();

					for (int column = 1; column <= unloadedAttributes.size(); column++) {
						var metaAttr = unloadedAttributes.get(column - 1);
						try {
							var result = getCortexContext().getTypeTranslator(((RegularTypeItem) metaAttr.getAttributeType())
									.getRegularClass())
									.read(rs, column);
							resultRow.add(result);
						} catch (Exception e) {
							if (LOG.isDebugEnabled()) {
								LOG.error(String.format("Can't read column [%s] number %s", metaAttr.getAttributeName(), rowNum), e);
							}
							try {
								resultRow.add(JdbcUtils.getResultSetValue(rs, rowNum));
							} catch (SQLException e1) {
								throw new RuntimeException(e1);
							}
						}
					}

					return (T) resultRow;

				};
			}
		};
	}

	protected Map<String, Object> createParameters(ItemContext ctx) {
		return Collections.singletonMap(AbstractGenericItem.UUID, ctx.getUuid());
	}

	protected String createQuery(List<MetaAttributeTypeItem> unloadedAttributes, ItemContext ctx) {
		var columns = unloadedAttributes.stream()
				.map(attr -> String.format("item.%s", attr.getColumnName()))
				.collect(Collectors.joining(","));
		return String.format(SELECT_REGULAR_FIELD_BY_UUID_TEMPLATE, columns, ctx.getTypeCode());
	}

	private Object processResult(ReactiveSearchResult<List<Object>> searchResultStream, List<MetaAttributeTypeItem> unloadedAttributes, MetaAttributeTypeItem attribute, ItemContext ctx) {
		List<Object> resultRow = null;
		List<List<Object>> searchResult = searchResultStream.getResultStream().collect(Collectors.toList());
		if (CollectionUtils.isEmpty(searchResult)) {
			return null;
		} else {
			if (searchResult.size() > 1) {
				throw new SearchException(String.format("Ambiguous search result for [%s:%s] attribute", ctx.getTypeCode(), attribute.getAttributeName()));
			}
			resultRow = searchResult.iterator().next();

		}

		return enrichContextAndGetResult(ctx, unloadedAttributes, attribute,
				resultRow
		);

	}

	private Object enrichContextAndGetResult(ItemContext ctx, List<MetaAttributeTypeItem> metaAttributeTypeItems, MetaAttributeTypeItem attribute, List<Object> resultRow) {
		Object result = null;
		for (int i = 0; i < metaAttributeTypeItems.size(); i++) {
			var metaAttributeTypeItem = metaAttributeTypeItems.get(i);
			var columnResult = resultRow.get(i);
			if (attribute.getUuid().equals(metaAttributeTypeItem.getUuid())) {
				result = columnResult;
			} else {
				ctx.initValue(metaAttributeTypeItem.getAttributeName(), columnResult);
			}
		}
		return result;
	}
}
