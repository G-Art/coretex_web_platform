package com.coretex.core.activeorm.query.select.transformator.strategies;

import com.coretex.core.activeorm.query.select.SelectQueryTransformationHelper;
import com.coretex.core.activeorm.query.select.scanners.ExpressionScanner;
import com.coretex.core.activeorm.query.select.scanners.SelectBodyScanner;
import com.coretex.core.activeorm.query.select.transformator.DataInjectionType;
import com.coretex.core.activeorm.query.select.transformator.dip.DataInjectionPoint;
import com.coretex.items.core.MetaTypeItem;
import net.sf.jsqlparser.statement.select.PlainSelect;

import java.util.Map;

public abstract class AbstractTransformationStrategy<S extends DataInjectionPoint, R>
		implements
		CloneAction,
		AmendTableAction,
		AdjustColumnAction {

	private SelectQueryTransformationHelper transformationHelper;
	private Map<DataInjectionType, AbstractTransformationStrategy> transformationStrategies;

	public AbstractTransformationStrategy(Map<DataInjectionType, AbstractTransformationStrategy> transformationStrategies, SelectQueryTransformationHelper transformationHelper) {
		this.transformationStrategies = transformationStrategies;
		this.transformationHelper = transformationHelper;
	}

	protected SelectQueryTransformationHelper getTransformationHelper() {
		return transformationHelper;
	}

	@Override
	public PlainSelect clone(PlainSelect select) {
		return transformationHelper.clone(select);
	}

	@Override
	public PlainSelect amendTable(PlainSelect plainSelect, MetaTypeItem metaTypeItem) {
		return transformationHelper.amendTable(plainSelect, metaTypeItem);
	}

	@Override
	public void adjustColumn(ExpressionScanner scanner, SelectBodyScanner ownerSelectBodyScanner) {
		transformationHelper.adjustColumn(scanner, ownerSelectBodyScanner);
	}

	public abstract R apply(S dataInjectionPoint);

	protected <T> T applyTransformation(DataInjectionPoint dataInjectionPoint) {
		return (T) transformationStrategies.get(dataInjectionPoint.getDataInjectionType()).apply(dataInjectionPoint);
	}
}
