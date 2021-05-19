package com.coretex.core.services.bootstrap.dialect;

import com.coretex.core.services.bootstrap.DbDialectService;
import org.postgresql.core.BaseConnection;
import org.postgresql.core.TypeInfo;
import org.postgresql.jdbc.TimestampUtils;
import org.springframework.jdbc.CannotGetJdbcConnectionException;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.function.Supplier;

public class PostgresqlDbDialectService implements DbDialectService {

	private DataSource dataSource;
	private TypeInfo typeInfo;
	private Supplier<BaseConnection> connectionSupplier;
	private TimestampUtils timestampUtils;

	protected PostgresqlDbDialectService(DataSource defaultDataSource) {
		this.dataSource = defaultDataSource;
		connectionSupplier = () -> {
			try {
				return (BaseConnection) dataSource.getConnection();
			} catch (SQLException e) {
				throw new CannotGetJdbcConnectionException("Unable create connection", e);
			}
		};

		this.typeInfo = connectionSupplier.get().getTypeInfo();
		this.timestampUtils = connectionSupplier.get().getTimestampUtils();
	}

	@Override
	public Integer getSqlTypeId(String typeName) {
		try {
			return typeInfo.getSQLType(typeName.toLowerCase());
		} catch (SQLException e) {
			throw new IllegalArgumentException(String.format("Type [%s] is not available for postgres dialect", typeName), e);
		}
	}

	@Override
	public String dateToString(Date date) {
		return timestampUtils.timeToString(date, true);
	}

	@Override
	public Date stringToDate(String date){
		try {
			return timestampUtils.toTimestamp(Calendar.getInstance(), date);
		} catch (SQLException e) {
			throw new IllegalArgumentException(String.format("Parse date exception [%s]", date), e);
		}
	}
}
