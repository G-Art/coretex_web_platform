package com.coretex.core.activeorm.query.operations;

import com.coretex.core.activeorm.cache.CacheService;
import com.coretex.core.activeorm.cache.impl.FeaturedStatementCacheContext;
import com.coretex.core.activeorm.exceptions.QueryException;
import com.coretex.core.activeorm.query.QueryTransformationProcessor;
import com.coretex.core.activeorm.query.QueryType;
import com.coretex.core.activeorm.query.operations.contexts.SelectOperationConfigContext;
import com.coretex.core.activeorm.query.operations.dataholders.QueryInfoHolder;
import com.coretex.core.activeorm.query.specs.select.SelectOperationSpec;
import com.coretex.core.services.bootstrap.impl.CortexContext;
import com.coretex.items.core.GenericItem;
import com.coretex.meta.AbstractGenericItem;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.select.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.function.Function;

public class SelectOperation
		extends SqlOperation<Select, SelectOperationSpec, SelectOperationConfigContext> {

	private static final Logger LOGGER = LoggerFactory.getLogger(SelectOperation.class);

	private static final Cache<Integer, QueryInfoHolder<Select>> selectCache = CacheBuilder.newBuilder()
			.softValues()
			.maximumSize(512)
			.concurrencyLevel(1)
			.expireAfterAccess(20, TimeUnit.MINUTES)
			.build();

	private final QueryTransformationProcessor<QueryInfoHolder<Select>> transformationProcessor;
	private final CortexContext cortexContext;
	private final BiFunction<Row, RowMetadata, ?> mapper;

	private final CacheService cacheService;

	protected final Function<SelectOperationSpec, FeaturedStatementCacheContext<Select>> searchQuerySupplier;

	public SelectOperation(QueryTransformationProcessor<QueryInfoHolder<Select>> transformationProcessor,
	                       CortexContext cortexContext, BiFunction<Row, RowMetadata, ?> mapper, CacheService cacheService) {
		this.transformationProcessor = transformationProcessor;
		this.cortexContext = cortexContext;
		this.mapper = mapper;
		this.cacheService = cacheService;
		searchQuerySupplier = (operationSpec) -> {
			var query = operationSpec.getQuery();
			try {
				Select select = (Select) CCJSqlParserUtil.parse(query);
				try {
					var selectQueryInfoHolder = selectCache.get(Objects.hashCode(query), () -> {
						var queryInfoHolder = new QueryInfoHolder<>(select);
						this.transformationProcessor.transform(queryInfoHolder);
						return queryInfoHolder;
					});
					operationSpec.setTransformedQuery(selectQueryInfoHolder.getStatement().toString());
					return new FeaturedStatementCacheContext<>(selectQueryInfoHolder, operationSpec.getParameters());
				} catch (ExecutionException e) {
					LOGGER.error("Cache calculation error", e);

					var queryInfoHolder = new QueryInfoHolder<>(select);
					this.transformationProcessor.transform(queryInfoHolder);
					operationSpec.setTransformedQuery(queryInfoHolder.getStatement().toString());
					return new FeaturedStatementCacheContext<>(queryInfoHolder, operationSpec.getParameters());
				}
			} catch (JSQLParserException e) {
				throw new QueryException(String.format("Query parsing error [%s]", query), e);
			}

		};
	}

	@Override
	public QueryType getQueryType() {
		return QueryType.SELECT;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> Flux<T> execute(SelectOperationConfigContext operationConfigContext) {
		return (Flux<T>) searchQuerySupplier
				.andThen(queryStatementContext -> cacheService.get(queryStatementContext, () -> {
					var query = queryStatementContext.getStatement().toString();

					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug(String.format("Execute query: [%s]; type: [%s];", query, getQueryType()));
					}

					var customMapper = operationConfigContext.customMapper();
					var sql = getDatabaseClient().sql(query);

					sql = bind(sql, operationConfigContext.getOperationSpec());

					return sql.map(customMapper.orElse(mapper)).all().subscribeOn(Schedulers.boundedElastic())
							.publishOn(Schedulers.boundedElastic());
				}))
				.apply(operationConfigContext.getOperationSpec());

	}

	private DatabaseClient.GenericExecuteSpec bind(DatabaseClient.GenericExecuteSpec sql, SelectOperationSpec operationSpec) {
		for (Map.Entry<String, Object> entry : operationSpec.getParameters().entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			if(entry.getValue() instanceof GenericItem){
				if(Objects.isNull(((GenericItem) entry.getValue()).getUuid())){
					throw new NullPointerException(String.format("Item type [%s] for parameter [%s] has no uuid", ((GenericItem) value).getMetaType().getTypeCode(), key));
				}
				value = ((AbstractGenericItem) value).getUuid();
			}
			if(entry.getValue() instanceof Enum){
				var metaEnumValueTypeItem = cortexContext.findMetaEnumValueTypeItem((Enum) entry.getValue());
				if(Objects.nonNull(metaEnumValueTypeItem)){
					value = metaEnumValueTypeItem.getUuid();
				} else {
					throw new NullPointerException(String.format("Enum type [%s] for parameter [%s] has not exist", ((GenericItem) value).getMetaType().getTypeCode(), key));
				}

			}

			sql = sql.bind(key, value);
		}
		return sql;
	}

	protected BiFunction<Row, RowMetadata, ?> getMapper() {
		return mapper;
	}
}
