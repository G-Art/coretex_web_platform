package com.coretex.core.activeorm.query.select.transformator.strategies;

import com.coretex.core.activeorm.query.select.SelectQueryTransformationHelper;
import com.coretex.core.activeorm.query.select.transformator.dip.ExpressionDataInjectionPoint;
import net.sf.jsqlparser.statement.select.PlainSelect;

public class OrderByTransformationStrategy extends AbstractTransformationStrategy<ExpressionDataInjectionPoint, PlainSelect> {

	public OrderByTransformationStrategy( SelectQueryTransformationHelper transformationHelper) {
		super(transformationHelper);
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
