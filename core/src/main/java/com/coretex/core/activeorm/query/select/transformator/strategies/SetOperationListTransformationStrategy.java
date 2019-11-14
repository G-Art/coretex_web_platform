package com.coretex.core.activeorm.query.select.transformator.strategies;

import com.coretex.core.activeorm.query.select.SelectQueryTransformationHelper;
import com.coretex.core.activeorm.query.select.scanners.Scanner;
import com.coretex.core.activeorm.query.select.scanners.SelectBodyScanner;
import com.coretex.core.activeorm.query.select.transformator.DataInjectionType;
import com.coretex.core.activeorm.query.select.transformator.dip.AbstractScannerDataInjectionPoint;
import com.coretex.core.activeorm.query.select.transformator.dip.SelectBodyDataInjectionPoint;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SetOperationList;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Map;

public class SetOperationListTransformationStrategy extends AbstractTransformationStrategy<SelectBodyDataInjectionPoint, SelectBody> {

	public SetOperationListTransformationStrategy(Map<DataInjectionType, AbstractTransformationStrategy> transformationStrategies, SelectQueryTransformationHelper transformationHelper) {
		super(transformationStrategies, transformationHelper);
	}

	@Override
	public SelectBody apply(SelectBodyDataInjectionPoint injectionPoint) {
		SelectBodyScanner selectBodyScanner = injectionPoint.getScanner();

		List<Scanner> subSelectScanners = selectBodyScanner.getSubSelectScanners();
		SetOperationList setOperationList = (SetOperationList) selectBodyScanner.scannedObject();
		if(CollectionUtils.isNotEmpty(subSelectScanners)){
			setOperationList.getSelects().clear();
			subSelectScanners.stream()
					.map(scanner -> (AbstractScannerDataInjectionPoint)getTransformationHelper().createDateInjectionPoint(scanner))
					.forEach(dataInjectionPoint -> {
						SelectBody selectBody = this.applyTransformation(dataInjectionPoint);
						setOperationList.getSelects().add(selectBody);
					});
		}

		return setOperationList;
	}
}
