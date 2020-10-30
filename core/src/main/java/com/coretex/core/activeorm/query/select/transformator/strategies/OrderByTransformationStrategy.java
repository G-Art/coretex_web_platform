package com.coretex.core.activeorm.query.select.transformator.strategies;

import com.coretex.core.activeorm.query.select.SelectQueryTransformationHelper;
import com.coretex.core.activeorm.query.select.transformator.DataInjectionType;
import com.coretex.core.activeorm.query.select.transformator.dip.ExpressionDataInjectionPoint;
import net.sf.jsqlparser.statement.select.PlainSelect;

import java.util.Map;

public class OrderByTransformationStrategy extends AbstractTransformationStrategy<ExpressionDataInjectionPoint, PlainSelect> {

	public OrderByTransformationStrategy(Map<DataInjectionType, AbstractTransformationStrategy> transformationStrategies, SelectQueryTransformationHelper transformationHelper) {
		super(transformationStrategies, transformationHelper);
	}

	public PlainSelect apply(ExpressionDataInjectionPoint dataInjectionPoint){

		var scanner = dataInjectionPoint.getScanner();
		var ownerSelectBodyScanner = dataInjectionPoint.getSelectBodyScannerOwner();

		if(ownerSelectBodyScanner.isPresent()){
			adjustColumn(scanner, ownerSelectBodyScanner.get());
		}

		return null;
	}


}
