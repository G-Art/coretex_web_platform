package com.coretex.core.activeorm.query.select.transformator.strategies;

import com.coretex.core.activeorm.query.select.SelectQueryTransformationHelper;
import com.coretex.core.activeorm.query.select.transformator.DataInjectionType;
import com.coretex.core.activeorm.query.select.transformator.dip.ExpressionDataInjectionPoint;
import net.sf.jsqlparser.statement.select.PlainSelect;

import java.util.Map;

public class WhereTransformationStrategy extends AbstractTransformationStrategy<ExpressionDataInjectionPoint, PlainSelect> {

	public WhereTransformationStrategy(Map<DataInjectionType, AbstractTransformationStrategy> transformationStrategies, SelectQueryTransformationHelper transformationHelper) {
		super(transformationStrategies, transformationHelper);
	}

	public PlainSelect apply(ExpressionDataInjectionPoint dataInjectionPoint){

		var scanner = dataInjectionPoint.getScanner();
		var ownerSelectBodyScanner = dataInjectionPoint.getSelectBodyScannerOwner();

		ownerSelectBodyScanner.ifPresent(selectBodyScanner -> getTransformationHelper().adjustColumn(scanner, selectBodyScanner));

		return null;
	}


}
