package com.coretex.core.activeorm.factories.mappers;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

public interface RowMapper <T> {
	T mapRow(Row row, RowMetadata rowMetadata);

}
