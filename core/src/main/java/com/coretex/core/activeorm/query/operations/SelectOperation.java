package com.coretex.core.activeorm.query.operations;

import com.coretex.core.activeorm.cache.CacheService;
import com.coretex.core.activeorm.cache.impl.FeaturedStatementCacheContext;
import com.coretex.core.activeorm.exceptions.QueryException;
import com.coretex.core.activeorm.query.QueryTransformationProcessor;
import com.coretex.core.activeorm.query.QueryType;
import com.coretex.core.activeorm.query.operations.contexts.SelectOperationConfigContext;
import com.coretex.core.activeorm.query.operations.dataholders.QueryInfoHolder;
import com.coretex.core.activeorm.query.operations.sources.SelectSqlParameterSource;
import com.coretex.core.activeorm.query.specs.select.SelectOperationSpec;
import com.coretex.core.services.bootstrap.impl.CortexContext;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.select.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Stream;

public class SelectOperation
		extends SqlOperation<Select, SelectOperationSpec, SelectOperationConfigContext> {

	private static final Logger LOGGER = LoggerFactory.getLogger(SelectOperation.class);

	private static final Cache<Integer, QueryInfoHolder<Select>> selectCache = CacheBuilder.newBuilder()
			.softValues()
			.maximumSize(512)
			.concurrencyLevel(1)
			.expireAfterAccess(20, TimeUnit.SECONDS)
			.build();

	private final QueryTransformationProcessor<QueryInfoHolder<Select>> transformationProcessor;
	private final CortexContext cortexContext;
	private final ResultSetExtractor<Stream<?>> extractor;

	private final CacheService cacheService;

	protected final Function<SelectOperationSpec, FeaturedStatementCacheContext<Select>> searchQuerySupplier;

	public SelectOperation(QueryTransformationProcessor<QueryInfoHolder<Select>> transformationProcessor,
	                       CortexContext cortexContext, ResultSetExtractor<Stream<?>> extractor, CacheService cacheService) {
		this.transformationProcessor = transformationProcessor;
		this.cortexContext = cortexContext;
		this.extractor = extractor;
		this.cacheService = cacheService;
		searchQuerySupplier = (operationSpec) -> {
			var query = operationSpec.getQuery();
			try {
				Select select = (Select) CCJSqlParserUtil.parse(query);
				try {
					return new FeaturedStatementCacheContext<>(selectCache.get(Objects.hashCode(query), () -> {
						var queryInfoHolder = new QueryInfoHolder<>(select);
						this.transformationProcessor.transform(queryInfoHolder);
						return queryInfoHolder;
					}), operationSpec.getParameters());
				} catch (ExecutionException e) {
					LOGGER.error("Cache calculation error", e);

					var queryInfoHolder = new QueryInfoHolder<>(select);
					this.transformationProcessor.transform(queryInfoHolder);
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
	public <T> Stream<T> execute(SelectOperationConfigContext operationConfigContext) {
		return (Stream<T>) searchQuerySupplier
				.andThen(queryStatementContext -> cacheService.get(queryStatementContext, () -> {
					var query = queryStatementContext.getStatement().toString();

					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug(String.format("Execute query: [%s]; type: [%s];", query, getQueryType()));
					}

					var customExtractor = operationConfigContext.customExtractor();
					return getJdbcTemplate().query(query,
							new SelectSqlParameterSource(operationConfigContext.getOperationSpec(), cortexContext),
							customExtractor.orElse(extractor));
				}))
				.apply(operationConfigContext.getOperationSpec());

	}

	protected ResultSetExtractor<Stream<?>> getExtractor() {
		return extractor;
	}
}
