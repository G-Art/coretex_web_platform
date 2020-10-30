package com.coretex.core.activeorm.query.operations;

import com.coretex.core.activeorm.extractors.CoretexReactiveResultSetExtractor;
import com.coretex.core.activeorm.query.QueryStatementContext;
import com.coretex.core.activeorm.query.QueryTransformationProcessor;
import com.coretex.core.activeorm.query.QueryType;
import com.coretex.core.activeorm.query.operations.sources.SelectSqlParameterSource;
import com.coretex.core.activeorm.query.specs.select.SelectOperationSpec;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.sf.jsqlparser.statement.select.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SelectOperation<T> extends SqlOperation<Select, SelectOperationSpec<T>> {

	private static final Logger LOGGER = LoggerFactory.getLogger(SelectOperation.class);

	private QueryTransformationProcessor<QueryStatementContext<Select>> transformationProcessor;
	private Function<SelectOperation, CoretexReactiveResultSetExtractor<T>> extractorFunction;

	private static final Cache<Integer, QueryStatementContext> selectCache = CacheBuilder.newBuilder()
			.softValues()
			.maximumSize(512)
			.concurrencyLevel(1)
			.expireAfterAccess(20, TimeUnit.SECONDS)
			.build();

	private Stream<T> result;

	public SelectOperation(SelectOperationSpec<T> operationSpec, QueryTransformationProcessor<QueryStatementContext<Select>> transformationProcessor) {
		super(operationSpec);
		Assert.notNull(transformationProcessor, "Transformation processor shouldn't be null");
		this.transformationProcessor = transformationProcessor;
	}

	public void setExtractorCreationFunction(Function<SelectOperation, CoretexReactiveResultSetExtractor<T>> extractorFunction) {
		this.extractorFunction = extractorFunction;
	}

	protected Function<SelectOperation, CoretexReactiveResultSetExtractor<T>> getExtractorFunction() {
		return extractorFunction;
	}

	@Override
	protected Select parseQuery(String query) {
		try {
			return (Select) selectCache.get(Objects.hashCode(query), () -> {
				var select = super.parseQuery(query);
				var selectStatementContext = new QueryStatementContext<>(select);
				doTransformation(selectStatementContext);
				return selectStatementContext;
			}).getStatement();
		} catch (ExecutionException e) {
			LOGGER.error("Cache calculation error", e);
		}
		return super.parseQuery(query);
	}

	@Override
	public QueryType getQueryType() {
		return QueryType.SELECT;
	}

	@Override
	public void execute() {
		var query = getQuery();
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(String.format("Execute query: [%s]; type: [%s];", query, getQueryType()));
		}
		result = getJdbcTemplate().query(query,
				new SelectSqlParameterSource(getOperationSpec()),
				extractorFunction.apply(this));
	}

	protected void doTransformation(QueryStatementContext<Select> statement) {
		transformationProcessor.transform(statement);
	}

	public Stream<T> searchResultAsStream() {
		this.execute();
		return result;
	}

	public List<T> searchResult() {
		return this.searchResultAsStream().collect(Collectors.toList());
	}

	public QueryTransformationProcessor<QueryStatementContext<Select>> getTransformationProcessor() {
		return transformationProcessor;
	}

	private Class<T> getResultValueType() {
		return this.getOperationSpec().getExpectedResultType();
	}

}
