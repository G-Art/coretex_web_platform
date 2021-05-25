package com.coretex.core.activeorm.query.select.transformator.dip.factory;

import com.coretex.core.activeorm.query.operations.dataholders.QueryInfoHolder;
import com.coretex.core.activeorm.query.select.scanners.ExpressionScanner;
import com.coretex.core.activeorm.query.select.scanners.FromItemScanner;
import com.coretex.core.activeorm.query.select.scanners.JoinScanner;
import com.coretex.core.activeorm.query.select.scanners.Scanner;
import com.coretex.core.activeorm.query.select.scanners.SelectBodyScanner;
import com.coretex.core.activeorm.query.select.scanners.SelectItemScanner;
import com.coretex.core.activeorm.query.select.scanners.SubSelectScanner;
import com.coretex.core.activeorm.query.select.transformator.dip.AbstractScannerDataInjectionPoint;
import com.coretex.core.activeorm.query.select.transformator.dip.ExpressionDataInjectionPoint;
import com.coretex.core.activeorm.query.select.transformator.dip.JoinDataInjectionPoint;
import com.coretex.core.activeorm.query.select.transformator.dip.SelectBodyDataInjectionPoint;
import com.coretex.core.activeorm.query.select.transformator.dip.SelectItemDataInjectionPoint;
import com.coretex.core.activeorm.query.select.transformator.dip.SubSelectDataInjectionPoint;
import com.coretex.core.activeorm.query.select.transformator.dip.TableDataInjectionPoint;
import com.google.common.collect.Maps;
import net.sf.jsqlparser.statement.Statement;

import java.util.Map;
import java.util.function.BiFunction;

public class DefaultDataInjectionFactory implements DataInjectionPointFactory {

	private Map<Class<? extends Scanner>, BiFunction<? super Scanner, QueryInfoHolder<? extends Statement>, ? extends AbstractScannerDataInjectionPoint<? extends Scanner>>> injectPointsMap = Maps.newHashMap();

	public DefaultDataInjectionFactory() {
		injectPointsMap.put(FromItemScanner.class, (scanner, context) -> new TableDataInjectionPoint((FromItemScanner) scanner, context));
		injectPointsMap.put(SelectBodyScanner.class, (scanner, context) -> new SelectBodyDataInjectionPoint((SelectBodyScanner) scanner, context));
		injectPointsMap.put(SubSelectScanner.class, (scanner, context) -> new SubSelectDataInjectionPoint((SubSelectScanner) scanner, context));
		injectPointsMap.put(SelectItemScanner.class, (scanner, context) -> new SelectItemDataInjectionPoint((SelectItemScanner) scanner, context));
		injectPointsMap.put(ExpressionScanner.class, (scanner, context) -> new ExpressionDataInjectionPoint((ExpressionScanner) scanner, context));
		injectPointsMap.put(JoinScanner.class, (scanner, context) -> new JoinDataInjectionPoint((JoinScanner) scanner, context));
	}

	@Override
	public <S extends Scanner> AbstractScannerDataInjectionPoint<? extends Scanner> getDataInjectionPoint(S scanner, QueryInfoHolder<? extends Statement> statementContext) {
		return injectPointsMap.get(scanner.getClass())
				.apply(scanner, statementContext);
	}
}
