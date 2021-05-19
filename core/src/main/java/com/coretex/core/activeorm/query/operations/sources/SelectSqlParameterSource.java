package com.coretex.core.activeorm.query.operations.sources;

import com.coretex.core.activeorm.query.specs.select.SelectOperationSpec;
import com.coretex.core.services.bootstrap.impl.CortexContext;
import com.coretex.items.core.GenericItem;
import com.coretex.meta.AbstractGenericItem;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.lang.Nullable;

import java.util.Map;
import java.util.Objects;

public class SelectSqlParameterSource extends MapSqlParameterSource {
	private SelectOperationSpec selectOperationSpec;
	private CortexContext cortexContext;

	public SelectSqlParameterSource(SelectOperationSpec selectOperationSpec, CortexContext cortexContext) {
		this.selectOperationSpec = selectOperationSpec;
		this.cortexContext = cortexContext;
		addValues(selectOperationSpec.getParameters());
	}

	@Override
	public MapSqlParameterSource addValue(String paramName, @Nullable Object value) {
		if(value instanceof GenericItem){
			if(Objects.isNull(((GenericItem) value).getUuid())){
				throw new NullPointerException(String.format("Item type [%s] for parameter [%s] has no uuid", ((GenericItem) value).getMetaType().getTypeCode(), paramName));
			}
			return super.addValue(paramName, ((AbstractGenericItem) value).getUuid());
		}
		if(value instanceof Enum){
			var metaEnumValueTypeItem = cortexContext.findMetaEnumValueTypeItem((Enum) value);
			if(Objects.nonNull(metaEnumValueTypeItem)){
				return super.addValue(paramName, metaEnumValueTypeItem.getUuid());
			}
			return super.addValue(paramName, value.toString(), cortexContext.getSqlType(cortexContext.getRegularType(String.class)));
		}
		return super.addValue(paramName, value, cortexContext.getSqlType(cortexContext.getRegularType(value.getClass())));
	}

	public MapSqlParameterSource addValues(@Nullable Map<String, ?> values) {
		if (values != null) {
			values.forEach(this::addValue);
		}
		return this;
	}
}
