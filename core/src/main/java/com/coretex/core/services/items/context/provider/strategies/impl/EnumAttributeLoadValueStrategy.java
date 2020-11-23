package com.coretex.core.services.items.context.provider.strategies.impl;

import com.coretex.core.activeorm.exceptions.SearchException;
import com.coretex.core.activeorm.extractors.CoretexReactiveResultSetExtractor;
import com.coretex.core.activeorm.factories.RowMapperFactory;
import com.coretex.core.activeorm.query.specs.select.SelectOperationSpec;
import com.coretex.core.activeorm.services.ReactiveSearchResult;
import com.coretex.core.services.items.context.ItemContext;
import com.coretex.core.services.items.context.provider.strategies.AbstractLoadAttributeValueStrategy;
import com.coretex.items.core.MetaAttributeTypeItem;
import com.coretex.items.core.MetaEnumTypeItem;
import com.coretex.items.core.MetaRelationTypeItem;
import com.coretex.meta.AbstractGenericItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class EnumAttributeLoadValueStrategy extends AbstractLoadAttributeValueStrategy {
	private Logger LOG = LoggerFactory.getLogger(EnumAttributeLoadValueStrategy.class);
	private static final String SELECT_ENUM_FIELD_BY_UUID_TEMPLATE = "select item.%s from \"%s\" as item where item.uuid = :uuid";

	@Override
	public Object load(ItemContext ctx, MetaAttributeTypeItem attribute) {
		CoretexReactiveResultSetExtractor<Object> extractor = new CoretexReactiveResultSetExtractor<>(getCortexContext());
		extractor.setMapperFactorySupplier(() -> creteMapperFactory(attribute));
		var searchResult = getOperationExecutor().execute(
				new SelectOperationSpec(
						createQuery(attribute, ctx),
						createParameters(ctx),
						extractor).createOperationContext());
		return processResult(searchResult, attribute, ctx);
	}

	private RowMapperFactory creteMapperFactory(MetaAttributeTypeItem attribute) {
		return new RowMapperFactory() {
			@Override
			public <T> RowMapper<T> createMapper(Class<T> targetClass) {
				return (rs, rowNum) -> {
					try {
						return (T) getCortexContext().findMetaEnumValueTypeItem(((MetaEnumTypeItem) attribute.getAttributeType()).getEnumClass(), (UUID) JdbcUtils.getResultSetValue(rs, rowNum));
					} catch (Exception e) {
						if (LOG.isDebugEnabled()) {
							LOG.error(String.format("Can't read column [%s] number %s", attribute.getAttributeName(), rowNum), e);
						}
						try {
							return (T) JdbcUtils.getResultSetValue(rs, rowNum);
						} catch (SQLException e1) {
							throw new RuntimeException(e1);
						}
					}
				};
			}
		};
	}

	protected Map<String, Object> createParameters(ItemContext ctx) {
		return Collections.singletonMap(AbstractGenericItem.UUID, ctx.getUuid());
	}

	protected String createQuery(MetaAttributeTypeItem attribute, ItemContext ctx) {
		return String.format(SELECT_ENUM_FIELD_BY_UUID_TEMPLATE, attribute.getColumnName(), ctx.getTypeCode());
	}

	protected static String getColumnName(MetaRelationTypeItem metaRelationTypeItem, String attributeName) {
		return metaRelationTypeItem.getItemAttributes().stream()
				.filter(metaAttr -> metaAttr.getAttributeName().equals(attributeName))
				.findFirst()
				.orElseThrow(() -> new SearchException(String.format("Column for attribute [%s] is not exist", attributeName))).getColumnName();
	}

	private Object processResult(ReactiveSearchResult<?> searchResultStream, MetaAttributeTypeItem attribute, ItemContext ctx) {
		Object result = null;
		List<Object> searchResult = searchResultStream.getResultStream().collect(Collectors.toList());

		if (!searchResult.isEmpty()) {

			if (searchResult.size() > 1) {
				throw new SearchException(String.format("Ambiguous search result for [%s:%s] attribute", ctx.getTypeCode(), attribute.getAttributeName()));
			}
			result = searchResult.iterator().next();
		}
		return result;
	}
}
