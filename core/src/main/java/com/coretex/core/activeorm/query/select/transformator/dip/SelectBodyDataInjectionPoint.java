package com.coretex.core.activeorm.query.select.transformator.dip;

import com.coretex.core.activeorm.query.operations.dataholders.QueryInfoHolder;
import com.coretex.core.activeorm.query.select.scanners.SelectBodyScanner;
import net.sf.jsqlparser.statement.Statement;

public class SelectBodyDataInjectionPoint extends AbstractScannerDataInjectionPoint<SelectBodyScanner> {

	public SelectBodyDataInjectionPoint(SelectBodyScanner scanner, QueryInfoHolder<? extends Statement> context) {
		super(scanner, context);
	}
}
