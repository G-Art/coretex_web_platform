package com.coretex.core.activeorm.query.operations;

import com.coretex.core.activeorm.extractors.CoretexResultSetExtractor;
import com.coretex.core.activeorm.query.QueryTransformationProcessor;
import com.coretex.core.activeorm.query.QueryType;
import com.coretex.core.activeorm.query.specs.select.SelectOperationSpec;
import com.coretex.items.core.GenericItem;
import com.coretex.meta.AbstractGenericItem;
import net.sf.jsqlparser.statement.select.Select;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public class SelectOperation<T> extends SqlOperation<Select, SelectOperationSpec<T>> {

	private QueryTransformationProcessor<Select> transformationProcessor;
	private Function<SelectOperation, CoretexResultSetExtractor<T>> extractorFunction;

	private List<T> result;

	public SelectOperation(SelectOperationSpec<T> operationSpec, QueryTransformationProcessor<Select> transformationProcessor) {
		super(operationSpec);
		Assert.notNull(transformationProcessor, "Transformation processor should't be null");
		this.transformationProcessor = transformationProcessor;
	}

	public void setExtractorCreationFunction(Function<SelectOperation, CoretexResultSetExtractor<T>> extractorFunction) {
		this.extractorFunction = extractorFunction;
	}

	@Override
	public QueryType getQueryType() {
		return QueryType.SELECT;
	}

	@Override
	public void execute() {
		transformationProcessor.transform(getStatement());
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
