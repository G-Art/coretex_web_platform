package com.coretex.core.activeorm.query.select.transformator.dip.factory;

import com.coretex.core.activeorm.query.select.scanners.*;
import com.coretex.core.activeorm.query.select.transformator.dip.*;
import com.google.common.collect.Maps;

import java.util.Map;
import java.util.function.Function;

public class DefaultDataInjectionFactory implements DataInjectionPointFactory {

	private Map<Class<? extends Scanner>, Function<? super Scanner, ? extends AbstractScannerDataInjectionPoint<? extends Scanner>>> injectPointsMap = Maps.newHashMap();

	public DefaultDataInjectionFactory() {
		injectPointsMap.put(FromItemScanner.class, scanner -> new TableDataInjectionPoint((FromItemScanner) scanner));
		injectPointsMap.put(SelectBodyScanner.class, scanner -> new SelectBodyDataInjectionPoint((SelectBodyScanner) scanner));
		injectPointsMap.put(SubSelectScanner.class, scanner -> new SubSelectDataInjectionPoint((SubSelectScanner) scanner));
		injectPointsMap.put(SelectItemScanner.class, scanner -> new SelectItemDataInjectionPoint((SelectItemScanner) scanner));
		injectPointsMap.put(ExpressionScanner.class, scanner -> new ExpressionDataInjectionPoint((ExpressionScanner) scanner));
		injectPointsMap.put(JoinScanner.class, scanner -> new JoinDataInjectionPoint((JoinScanner) scanner));
	}

	@Override
	public <S extends Scanner> AbstractScannerDataInjectionPoint<? extends Scanner> getDataInjectionPoint(S scanner) {
		return injectPointsMap.get(scanner.getClass()).apply(scanner);
	}
}
