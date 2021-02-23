package com.coretex.core.activeorm.extractors;

import com.coretex.core.activeorm.factories.RowMapperFactory;
import com.coretex.core.activeorm.factories.mappers.RowMapper;
import com.coretex.core.services.bootstrap.meta.MetaTypeProvider;
import com.coretex.items.core.GenericItem;
import com.coretex.items.core.MetaTypeItem;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class CoretexReactiveRowMapper<T> implements BiFunction<Row, RowMetadata, T> {

	private Logger LOG = LoggerFactory.getLogger(RowMapperFactory.class);

	private MetaTypeProvider metaTypeProvider;
	private Supplier<RowMapperFactory> mapperFactorySupplier;

	public CoretexReactiveRowMapper(MetaTypeProvider context) {
		this.metaTypeProvider = context;
	}

	public CoretexReactiveRowMapper(MetaTypeProvider context, RowMapperFactory defaultRowMapperFactory) {
		this(context);
		mapperFactorySupplier = () -> defaultRowMapperFactory;
	}


	private boolean hasMetaTypeColumn(RowMetadata rowSet) {
		return rowSet.getColumnNames().contains(metaTypeProvider.findAttribute(GenericItem.ITEM_TYPE, MetaTypeItem.META_TYPE).getColumnName().toLowerCase());
	}

	public void setMapperFactorySupplier(Supplier<RowMapperFactory> mapperFactorySupplier) {
		this.mapperFactorySupplier = mapperFactorySupplier;
	}

	@SuppressWarnings("unchecked")
	private RowMapper<T> getRowMapper(Class tClass) {
		return mapperFactorySupplier.get().createMapper(tClass);
	}

	private T extractData(Row row, RowMetadata rowMetadata, RowMapper<T> rowMapper) {
		return rowMapper.mapRow(row, rowMetadata);
	}

	@Override
	public T apply(Row row, RowMetadata rowMetadata) {
		try {
			if (hasMetaTypeColumn(rowMetadata)) {
				return extractData(row, rowMetadata, getRowMapper(GenericItem.class));
			}
			return extractData(row, rowMetadata, getRowMapper(Map.class));
		} catch (Exception e) {
			return extractData(row, rowMetadata, getRowMapper(Map.class));
		}

	}
}
