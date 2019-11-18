package com.coretex.core.activeorm.query.operations;

import com.coretex.core.activeorm.extractors.CoretexResultSetExtractor;
import com.coretex.core.activeorm.query.QueryTransformationProcessor;
import com.coretex.core.activeorm.query.QueryType;
import com.coretex.core.activeorm.query.specs.select.SelectOperationSpec;
import com.coretex.items.core.GenericItem;
import com.coretex.meta.AbstractGenericItem;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.sf.jsqlparser.statement.select.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class SelectOperation<T> extends SqlOperation<Select, SelectOperationSpec<T>> {

	private static final Logger LOGGER = LoggerFactory.getLogger(SelectOperation.class);

	private QueryTransformationProcessor<Select> transformationProcessor;
	private Function<SelectOperation, CoretexResultSetExtractor<T>> extractorFunction;

	private static Cache<Integer, Select> selectCache = CacheBuilder.newBuilder()
			.softValues()
			.maximumSize(255)
			.expireAfterAccess(10, TimeUnit.SECONDS)
			.build();

	private List<T> result;
	private boolean transformed;

	public SelectOperation(SelectOperationSpec<T> operationSpec, QueryTransformationProcessor<Select> transformationProcessor) {
		super(operationSpec);
		Assert.notNull(transformationProcessor, "Transformation processor should't be null");
		this.transformationProcessor = transformationProcessor;
	}

	public void setExtractorCreationFunction(Function<SelectOperation, CoretexResultSetExtractor<T>> extractorFunction) {
		this.extractorFunction = extractorFunction;
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
		if(!transformed){
			transformationProcessor.transform(getStatement());
		}
		result = getJdbcTemplate().query(getStatement().toString(), new  MapSqlParameterSource(getOperationSpec().getParameters()) {

			@Override
			public MapSqlParameterSource addValue(String paramName, @Nullable Object value) {
				if(value instanceof GenericItem){
					if(Objects.isNull(((GenericItem) value).getUuid())){
						throw new NullPointerException(String.format("Item type [%s] for parameter [%s] has no uuid", ((GenericItem) value).getMetaType().getTypeCode(), paramName));
					}
					return super.addValue(paramName, ((AbstractGenericItem) value).getUuid());
				}
				if(value instanceof Enum){
					var metaEnumValueTypeItem = getOperationSpec().getCortexContext().findMetaEnumValueTypeItem((Enum) value);
					if(Objects.nonNull(metaEnumValueTypeItem)){
						return super.addValue(paramName, metaEnumValueTypeItem.getUuid());
					}
					return super.addValue(paramName, value.toString());
				}
				return super.addValue(paramName, value);
			}

			public MapSqlParameterSource addValues(@Nullable Map<String, ?> values) {
				if (values != null) {
					values.forEach(this::addValue);
				}
				return this;
			}
		}, extractorFunction.apply(this));
	}

	public List<T> searchResult(){
		this.execute();
		return result;
	}

	public QueryTransformationProcessor<Select> getTransformationProcessor() {
		return transformationProcessor;
	}

	private Class<T> getResultValueType(){
        return this.getOperationSpec().getExpectedResultType();
	}

}
