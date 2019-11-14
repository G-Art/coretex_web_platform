package com.coretex.core.activeorm.query.select.transformator.strategies;

import com.coretex.core.activeorm.query.select.SelectQueryTransformationHelper;
import com.coretex.core.activeorm.query.select.transformator.DataInjectionType;
import com.coretex.core.activeorm.query.select.transformator.dip.DataInjectionPoint;

import java.util.Map;

public abstract class AbstractTransformationStrategy<S extends DataInjectionPoint, R> {

	private SelectQueryTransformationHelper transformationHelper;
	private Map<DataInjectionType, AbstractTransformationStrategy> transformationStrategies;

	public AbstractTransformationStrategy(Map<DataInjectionType, AbstractTransformationStrategy> transformationStrategies, SelectQueryTransformationHelper transformationHelper) {
		this.transformationStrategies = transformationStrategies;
		this.transformationHelper = transformationHelper;
	}

	protected SelectQueryTransformationHelper getTransformationHelper() {
		return transformationHelper;
	}

	public abstract R apply(S dataInjectionPoint);

	protected <T> T applyTransformation(DataInjectionPoint dataInjectionPoint){
		return (T) transformationStrategies.get(dataInjectionPoint.getDataInjectionType()).apply(dataInjectionPoint);
	}
}
