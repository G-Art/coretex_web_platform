package com.coretex.core.activeorm.query.select.transformator.strategies;

import com.coretex.core.activeorm.query.QueryStatementContext;
import com.coretex.core.activeorm.query.select.SelectQueryTransformationHelper;
import com.coretex.core.activeorm.query.select.scanners.ExpressionScanner;
import com.coretex.core.activeorm.query.select.scanners.JoinScanner;
import com.coretex.core.activeorm.query.select.scanners.Scanner;
import com.coretex.core.activeorm.query.select.scanners.SelectBodyScanner;
import com.coretex.core.activeorm.query.select.transformator.DataInjectionType;
import com.coretex.core.activeorm.query.select.transformator.dip.*;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
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
		QueryStatementContext<? extends Statement> queryStatementContext = injectionPoint.getContext();
		List<Scanner> subSelectScanners = selectBodyScanner.getSubSelectScanners();
		subSelectScanners.stream()
				.map(scanner -> (AbstractScannerDataInjectionPoint) getTransformationHelper().createDateInjectionPoint(scanner, queryStatementContext))
				.forEach(this::applyTransformation);

		if(CollectionUtils.isNotEmpty(selectBodyScanner.getJoinScanners())){
			adjustJoin(selectBodyScanner, queryStatementContext);
		}

		adjustWhereExpression(selectBodyScanner, queryStatementContext);
		adjustFromItem(selectBodyScanner, queryStatementContext);
		adjustOrderBy(selectBodyScanner, queryStatementContext);
		adjustGroupBy(selectBodyScanner, queryStatementContext);

		return selectBodyScanner.scannedObject();
	}

	private void adjustOrderBy(SelectBodyScanner selectBodyScanner, QueryStatementContext<? extends Statement> statementContext) {
		var orderByExpresionScanners = selectBodyScanner.getOrderByExpresionScanners();

		if (CollectionUtils.isNotEmpty(orderByExpresionScanners)) {
			orderByExpresionScanners.forEach((Consumer<ExpressionScanner>) orderByExpresionScanner -> {
				ExpressionDataInjectionPoint dateInjectionPoint = getTransformationHelper().createDateInjectionPoint(orderByExpresionScanner, statementContext);
				dateInjectionPoint.setSelectBodyScannerOwner(selectBodyScanner);
				applyTransformation(dateInjectionPoint);
			});
		}
	}

	private void adjustGroupBy(SelectBodyScanner selectBodyScanner, QueryStatementContext<? extends Statement> statementContext) {
		var groupByExpresionScanners = selectBodyScanner.getGroupByExpresionScanners();

		if (CollectionUtils.isNotEmpty(groupByExpresionScanners)) {
			groupByExpresionScanners.forEach((Consumer<ExpressionScanner>) groupByExpresionScanner -> {
				ExpressionDataInjectionPoint dateInjectionPoint = getTransformationHelper().createDateInjectionPoint(groupByExpresionScanner, statementContext);
				dateInjectionPoint.setSelectBodyScannerOwner(selectBodyScanner);
				applyTransformation(dateInjectionPoint);
			});
		}
	}

	private void adjustJoin(SelectBodyScanner selectBodyScanner, QueryStatementContext<? extends Statement> statementContext) {
		selectBodyScanner.getJoinScanners().forEach((Consumer<JoinScanner>) joinScanner -> {
			JoinDataInjectionPoint joinDataInjectionPoint = getTransformationHelper().createDateInjectionPoint(joinScanner, statementContext);
			joinDataInjectionPoint.setSelectBodyScannerOwner(selectBodyScanner);
			applyTransformation(joinDataInjectionPoint);
		});
	}

	private void adjustWhereExpression(SelectBodyScanner selectBodyScanner, QueryStatementContext<? extends Statement> statementContext) {
		var whereExpresionScanner = selectBodyScanner.getWhereExpresionScanner();
		if (Objects.nonNull(whereExpresionScanner)) {
			ExpressionDataInjectionPoint dateInjectionPoint = getTransformationHelper().createDateInjectionPoint(whereExpresionScanner, statementContext);
			dateInjectionPoint.setSelectBodyScannerOwner(selectBodyScanner);
			applyTransformation(dateInjectionPoint);
		}
	}

	private void adjustFromItem(SelectBodyScanner selectBodyScanner, QueryStatementContext<? extends Statement> statementContext) {
		var fromItemScanner = selectBodyScanner.getFromItemScanner();
		if (Objects.nonNull(fromItemScanner) && fromItemScanner.isTable()) {
			var tableTransformationData = getTransformationHelper().bindItem((Table) fromItemScanner.scannedObject());
			if (tableTransformationData.isBind()) {
				TableDataInjectionPoint tableDataInjectionPoint = getTransformationHelper().createDateInjectionPoint(fromItemScanner, statementContext);
				tableDataInjectionPoint.setTableTransformationData(tableTransformationData);
				tableDataInjectionPoint.setUseSubtypes(fromItemScanner.isUseInheritance());
				selectBodyScanner.setSelectBody(applyTransformation(tableDataInjectionPoint));
			}
		}
	}
}
