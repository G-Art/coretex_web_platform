package com.coretex.core.activeorm.query.select.transformator;

import com.coretex.core.activeorm.query.select.scanners.*;
import com.coretex.core.activeorm.query.select.transformator.dip.DataInjectionPoint;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

public enum DataInjectionType {

	PLAIN_SELECT, SET_OPERATION_LIST, SUB_SELECT,
	SELECT_ITEM_ALL, SELECT_ITEM_EXPRESSION, COLUMN_EXPRESSION, FUNCTION_EXPRESSION,
	FROM_ITEM_TABLE, FROM_ITEM_SUBS_ELECT,
	WHERE_EXPRESSION, HAVING_EXPRESSION, ORDER_BY_EXPRESSION, GROUP_BY_EXPRESSION, JOIN_ON_EXPRESSION,
	JOIN;

	private static Map<Class<? extends Scanner>, Function<? super Scanner, DataInjectionType>> ditMapping = Maps.newHashMap();

	static {
		ditMapping.put(SelectBodyScanner.class, scanner -> {
			SelectBodyScanner selectBodyScanner = (SelectBodyScanner) scanner;
			return selectBodyScanner.isPlaneSelect() ? PLAIN_SELECT : SET_OPERATION_LIST;
		});

		ditMapping.put(SelectItemScanner.class, scanner -> {
			SelectItemScanner selectItemScanner = (SelectItemScanner) scanner;
			return selectItemScanner.isAllColumn() ? SELECT_ITEM_ALL : SELECT_ITEM_EXPRESSION;
		});

		ditMapping.put(FromItemScanner.class, scanner -> {
			FromItemScanner fromItemScanner = (FromItemScanner) scanner;
			if(fromItemScanner.isTable()){
				return FROM_ITEM_TABLE;
			}
			return FROM_ITEM_SUBS_ELECT;
		});

		ditMapping.put(SubSelectScanner.class, scanner -> SUB_SELECT);

		ditMapping.put(JoinScanner.class, scanner -> JOIN);

		ditMapping.put(ExpressionScanner.class, scanner -> ((ExpressionScanner)scanner).getExpresionType());
	}

	public static <S extends Scanner> DataInjectionType getTypeForScanner(S scanner){
		return ditMapping.get(scanner.getClass()).apply(scanner);
	}

	public static <T> DataInjectionType getDITypeForType(T type){
		if(type instanceof Collection && CollectionUtils.isNotEmpty((Collection<?>) type)){
			var sample = ((Collection) type).iterator().next();
			if(sample instanceof Scanner){
				return ditMapping.get(((Scanner)sample).getClass()).apply((Scanner) sample);
			}
			if (sample instanceof DataInjectionPoint){
				return ((DataInjectionPoint) sample).getDataInjectionType();
			}
		}
		throw new IllegalArgumentException(String.format("Can't recognize a data injection type for [%s]", String.valueOf(type)));
	}
}
