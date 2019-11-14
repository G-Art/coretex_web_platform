package com.coretex.core.activeorm.query.select.transformator.strategies;

import com.coretex.core.activeorm.query.select.SelectQueryTransformationHelper;
import com.coretex.core.activeorm.query.select.scanners.ExpressionScanner;
import com.coretex.core.activeorm.query.select.scanners.JoinScanner;
import com.coretex.core.activeorm.query.select.scanners.Scanner;
import com.coretex.core.activeorm.query.select.scanners.SelectBodyScanner;
import com.coretex.core.activeorm.query.select.transformator.DataInjectionType;
import com.coretex.core.activeorm.query.select.transformator.dip.*;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.SelectBody;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class PlainSelectTransformationStrategy extends AbstractTransformationStrategy<SelectBodyDataInjectionPoint, SelectBody> {

	public PlainSelectTransformationStrategy(Map<DataInjectionType, AbstractTransformationStrategy> transformationStrategies, SelectQueryTransformationHelper transformationHelper) {
		super(transformationStrategies, transformationHelper);
	}

	public SelectBody apply(SelectBodyDataInjectionPoint injectionPoint) {
		SelectBodyScanner selectBodyScanner = injectionPoint.getScanner();
		List<Scanner> subSelectScanners = selectBodyScanner.getSubSelectScanners();
		subSelectScanners.stream()
				.map(scanner -> (AbstractScannerDataInjectionPoint) getTransformationHelper().createDateInjectionPoint(scanner))
				.forEach(this::applyTransformation);

		if(CollectionUtils.isNotEmpty(selectBodyScanner.getJoinScanners())){
			adjustJoin(selectBodyScanner);
		}

		adjustWhereExpression(selectBodyScanner);
		adjustFromItem(selectBodyScanner);
		adjustOrderBy(selectBodyScanner);
		adjustGroupBy(selectBodyScanner);

		return selectBodyScanner.scannedObject();
	}

	private void adjustOrderBy(SelectBodyScanner selectBodyScanner) {
		var orderByExpresionScanners = selectBodyScanner.getOrderByExpresionScanners();

		if (CollectionUtils.isNotEmpty(orderByExpresionScanners)) {
			orderByExpresionScanners.forEach((Consumer<ExpressionScanner>) orderByExpresionScanner -> {
				ExpressionDataInjectionPoint dateInjectionPoint = getTransformationHelper().createDateInjectionPoint(orderByExpresionScanner);
				dateInjectionPoint.setSelectBodyScannerOwner(selectBodyScanner);
				applyTransformation(dateInjectionPoint);
			});
		}
	}

	private void adjustGroupBy(SelectBodyScanner selectBodyScanner) {
		var groupByExpresionScanners = selectBodyScanner.getGroupByExpresionScanners();

		if (CollectionUtils.isNotEmpty(groupByExpresionScanners)) {
			groupByExpresionScanners.forEach((Consumer<ExpressionScanner>) groupByExpresionScanner -> {
				ExpressionDataInjectionPoint dateInjectionPoint = getTransformationHelper().createDateInjectionPoint(groupByExpresionScanner);
				dateInjectionPoint.setSelectBodyScannerOwner(selectBodyScanner);
				applyTransformation(dateInjectionPoint);
			});
		}
	}

	private void adjustJoin(SelectBodyScanner selectBodyScanner) {
		selectBodyScanner.getJoinScanners().forEach((Consumer<JoinScanner>) joinScanner -> {
			JoinDataInjectionPoint joinDataInjectionPoint = getTransformationHelper().createDateInjectionPoint(joinScanner);
			joinDataInjectionPoint.setSelectBodyScannerOwner(selectBodyScanner);
			applyTransformation(joinDataInjectionPoint);
		});
	}

	private void adjustWhereExpression(SelectBodyScanner selectBodyScanner) {
		var whereExpresionScanner = selectBodyScanner.getWhereExpresionScanner();
		if (Objects.nonNull(whereExpresionScanner)) {
			ExpressionDataInjectionPoint dateInjectionPoint = getTransformationHelper().createDateInjectionPoint(whereExpresionScanner);
			dateInjectionPoint.setSelectBodyScannerOwner(selectBodyScanner);
			applyTransformation(dateInjectionPoint);
		}
	}

	private void adjustFromItem(SelectBodyScanner selectBodyScanner) {
		var fromItemScanner = selectBodyScanner.getFromItemScanner();
		if (Objects.nonNull(fromItemScanner) && fromItemScanner.isTable()) {
			var tableTransformationData = getTransformationHelper().bindItem((Table) fromItemScanner.scannedObject());
			if (tableTransformationData.isBind()) {
				TableDataInjectionPoint tableDataInjectionPoint = getTransformationHelper().createDateInjectionPoint(fromItemScanner);
				tableDataInjectionPoint.setTableTransformationData(tableTransformationData);
				tableDataInjectionPoint.setUseSubtypes(fromItemScanner.isUseInheritance());
				selectBodyScanner.setSelectBody(applyTransformation(tableDataInjectionPoint));
			}
		}
	}
}
