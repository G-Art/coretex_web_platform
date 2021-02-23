package com.coretex.core.activeorm.factories.mappers;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import org.springframework.r2dbc.core.ColumnMapRowMapper;

import java.util.Map;

public class ColumnMapRowMapperAdapter implements RowMapper<Map<String, Object>>{

	@Override
	public Map<String, Object> mapRow(Row row, RowMetadata rowMetadata) {
		return ColumnMapRowMapper.INSTANCE.apply(row, rowMetadata);
	}
}
