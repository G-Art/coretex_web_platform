package com.coretex.core.activeorm.query.select.transformator.strategies;

import com.coretex.core.activeorm.query.select.SelectQueryTransformationHelper;
import com.coretex.core.activeorm.query.select.scanners.SubSelectScanner;
import com.coretex.core.activeorm.query.select.transformator.DataInjectionType;
import com.coretex.core.activeorm.query.select.transformator.dip.SelectBodyDataInjectionPoint;
import com.coretex.core.activeorm.query.select.transformator.dip.SubSelectDataInjectionPoint;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SubSelect;

import java.util.Map;

public class SubSelectTransformationStrategy extends AbstractTransformationStrategy<SubSelectDataInjectionPoint, SubSelect>{

	public SubSelectTransformationStrategy(Map<DataInjectionType, AbstractTransformationStrategy> transformationStrategies, SelectQueryTransformationHelper transformationHelper) {
		super(transformationStrategies, transformationHelper);
	}

	public SubSelect apply(SubSelectDataInjectionPoint injectionPoint){
		SubSelectScanner subSelectScanner = injectionPoint.getScanner();

		SelectBody selectBody = applyTransformation(new SelectBodyDataInjectionPoint(subSelectScanner.getSelectBodyScanner(), injectionPoint.getContext()));

		subSelectScanner.scannedObject().setSelectBody(selectBody);

		return subSelectScanner.scannedObject();
	}
}
