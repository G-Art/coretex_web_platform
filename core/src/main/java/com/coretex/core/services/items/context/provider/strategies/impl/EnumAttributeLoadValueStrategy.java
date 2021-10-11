package com.coretex.core.services.items.context.provider.strategies.impl;

import com.coretex.core.activeorm.exceptions.SearchException;
import com.coretex.core.activeorm.extractors.CoretexReactiveResultSetExtractor;
import com.coretex.core.activeorm.factories.RowMapperFactory;
import com.coretex.core.activeorm.query.specs.select.SelectItemAttributeOperationSpec;
import com.coretex.core.activeorm.services.ReactiveSearchResult;
import com.coretex.core.services.items.context.ItemContext;
import com.coretex.core.services.items.context.provider.strategies.AbstractLoadAttributeValueStrategy;
import com.coretex.items.core.MetaAttributeTypeItem;
import com.coretex.items.core.MetaEnumTypeItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class EnumAttributeLoadValueStrategy extends AbstractLoadAttributeValueStrategy {
	private Logger LOG = LoggerFactory.getLogger(EnumAttributeLoadValueStrategy.class);

	@Override
	public Object load(ItemContext ctx, MetaAttributeTypeItem attribute) {
		CoretexReactiveResultSetExtractor<Object> extractor = new CoretexReactiveResultSetExtractor<>(getCortexContext());
		extractor.setMapperFactorySupplier(() -> creteMapperFactory(attribute));
		var searchResult = getOperationExecutor().execute(
				new SelectItemAttributeOperationSpec(
						attribute,
						ctx,
						getDbDialectService(),
						extractor).createOperationContext());
		return processResult(searchResult, attribute, ctx);
	}

	private RowMapperFactory creteMapperFactory(MetaAttributeTypeItem attribute) {
		return new RowMapperFactory() {
			@Override
			public <T> RowMapper<T> createMapper(Class<T> targetClass) {
				return (rs, rowNum) -> {
					try {
						Object resultSetValue = JdbcUtils.getResultSetValue(rs, rowNum);
						UUID value = resultSetValue instanceof UUID ? (UUID) resultSetValue : UUID.fromString((String) resultSetValue);
						return (T) getCortexContext().findMetaEnumValueTypeItem(((MetaEnumTypeItem) attribute.getAttributeType()).getEnumClass(), value);
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
