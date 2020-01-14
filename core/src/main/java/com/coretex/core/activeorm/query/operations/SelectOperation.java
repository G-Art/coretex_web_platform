package com.coretex.core.activeorm.query.operations;

import com.coretex.core.activeorm.extractors.CoretexReactiveResultSetExtractor;
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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SelectOperation<T> extends SqlOperation<Select, SelectOperationSpec<T>> {

	private static final Logger LOGGER = LoggerFactory.getLogger(SelectOperation.class);

	private QueryTransformationProcessor<Select> transformationProcessor;
	private Function<SelectOperation, CoretexReactiveResultSetExtractor<T>> extractorFunction;

	private static Cache<Integer, Select> selectCache = CacheBuilder.newBuilder()
			.softValues()
			.maximumSize(255)
			.concurrencyLevel(1)
			.expireAfterAccess(20, TimeUnit.SECONDS)
			.build();

	private Stream<T> result;
	private boolean transformed;

	public SelectOperation(SelectOperationSpec<T> operationSpec, QueryTransformationProcessor<Select> transformationProcessor) {
		super(operationSpec);
		Assert.notNull(transformationProcessor, "Transformation processor should't be null");
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
		this.transformed = true;
		try {
			return selectCache.get(query.hashCode(), ()-> {
				var select = super.parseQuery(query);
				this.transformed = false;
				return select;
			});
		} catch (ExecutionException e) {
			LOGGER.error("Cache calculation error", e);
		}
		transformed = false;
		return super.parseQuery(query);
	}

	@Override
	public QueryType getQueryType() {
		return QueryType.SELECT;
	}

	@Override
	public void execute() {
		doTransformation(getStatement());
		result = getJdbcTemplate().query(getStatement().toString(),
				new SelectSqlParameterSource(getOperationSpec()),
				extractorFunction.apply(this));
	}

	protected void doTransformation(Select statement){
		if(!transformed){
			transformationProcessor.transform(statement);
		}
	}

	public Stream<T> searchResultAsStream(){
		this.execute();
		return result;
	}

	public List<T> searchResult(){
		return this.searchResultAsStream().collect(Collectors.toList());
	}

	public QueryTransformationProcessor<Select> getTransformationProcessor() {
		return transformationProcessor;
	}

	private Class<T> getResultValueType(){
        return this.getOperationSpec().getExpectedResultType();
	}

}
