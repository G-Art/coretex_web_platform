package com.coretex.core.services.bootstrap.dialect;

import com.coretex.core.services.bootstrap.dialect.mysql.MysqlDbDialectService;
import com.coretex.core.services.bootstrap.dialect.mysql.MysqlMetaDataExtractor;
import com.coretex.core.services.bootstrap.dialect.mysql.MysqlQueryTransformationHints;
import com.coretex.core.services.bootstrap.dialect.postgres.PostgresDbDialectService;
import com.coretex.core.services.bootstrap.dialect.postgres.PostgresMetaDataExtractor;
import com.coretex.core.services.bootstrap.dialect.postgres.PostgresQueryTransformationHints;
import com.coretex.core.services.bootstrap.meta.MetaDataExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Supplier;

public class DbDialectFactory {

	protected enum SqlDialect {
		MYSQL(MysqlDbDialectService::new, MysqlMetaDataExtractor::new, MysqlQueryTransformationHints::new),
		POSTGRES(PostgresDbDialectService::new, PostgresMetaDataExtractor::new, PostgresQueryTransformationHints::new),
		ORACLE(null, null, null);

		private final Function<DataSource, DbDialectService> dbDialectService;
		private final Function<NamedParameterJdbcTemplate, MetaDataExtractor> metaDataExtractor;
		private final Supplier<QueryTransformationHints> queryTransformationHints;

		private static final Map<String, SqlDialect> ENUM_MAP;

		static {
			Map<String, SqlDialect> map = new ConcurrentHashMap<>();
			for (SqlDialect instance : SqlDialect.values()) {
				map.put(instance.name().toLowerCase(), instance);
			}
			map.put("postgresql", SqlDialect.POSTGRES); //required to avoid misunderstanding and improve reliability
			ENUM_MAP = Collections.unmodifiableMap(map);
		}


		SqlDialect(Function<DataSource, DbDialectService> dbDialectService,
		           Function<NamedParameterJdbcTemplate, MetaDataExtractor> metaDataExtractor,
		           Supplier<QueryTransformationHints> queryTransformationHints) {
			this.dbDialectService = dbDialectService;
			this.metaDataExtractor = metaDataExtractor;
			this.queryTransformationHints = queryTransformationHints;
		}

		public DbDialectService getDbDialectService(DataSource dataSource) {
			return Optional.ofNullable(dbDialectService)
					.orElseThrow(() -> {
						throw new UnsupportedOperationException(this.name() + " dialect is not supported yet");
					})
					.apply(dataSource);
		}

		public MetaDataExtractor getMetaDataExtractor(NamedParameterJdbcTemplate jdbcTemplate) {
			return Optional.ofNullable(metaDataExtractor)
					.orElseThrow(() -> {
						throw new UnsupportedOperationException(this.name() + " dialect is not supported yet");
					})
					.apply(jdbcTemplate);
		}

		public QueryTransformationHints getQueryTransformationHints() {
			return Optional.ofNullable(queryTransformationHints)
					.orElseThrow(() -> {
						throw new UnsupportedOperationException(this.name() + " dialect is not supported yet");
					})
					.get();
		}

		public static SqlDialect get(String name) {
			return ENUM_MAP.get(name.toLowerCase());
		}
	}

	public DbDialectService getDialectService(DataSource defaultDataSource, String dialect) {
		return SqlDialect.get(dialect).getDbDialectService(defaultDataSource);
	}

	public MetaDataExtractor getMetaDataExtractor(NamedParameterJdbcTemplate jdbcTemplate, String dialect) {
		return SqlDialect.get(dialect).getMetaDataExtractor(jdbcTemplate);
	}

	public QueryTransformationHints getQueryTransformationHints(String dialect) {
		return SqlDialect.get(dialect).getQueryTransformationHints();
	}
}
