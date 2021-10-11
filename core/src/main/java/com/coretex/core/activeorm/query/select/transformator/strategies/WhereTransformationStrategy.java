package com.coretex.core.activeorm.query.select.transformator.strategies;

import com.coretex.core.activeorm.query.select.SelectQueryTransformationHelper;
import com.coretex.core.activeorm.query.select.transformator.dip.ExpressionDataInjectionPoint;
import net.sf.jsqlparser.statement.select.PlainSelect;

public class WhereTransformationStrategy extends AbstractTransformationStrategy<ExpressionDataInjectionPoint, PlainSelect> {

	public WhereTransformationStrategy(SelectQueryTransformationHelper transformationHelper) {
		super(transformationHelper);
	}

	public PlainSelect apply(ExpressionDataInjectionPoint dataInjectionPoint){

		var scanner = dataInjectionPoint.getScanner();
		var ownerSelectBodyScanner = dataInjectionPoint.getSelectBodyScannerOwner();

		ownerSelectBodyScanner.ifPresent(selectBodyScanner -> adjustColumn(scanner, selectBodyScanner));

		return null;
	}


}
