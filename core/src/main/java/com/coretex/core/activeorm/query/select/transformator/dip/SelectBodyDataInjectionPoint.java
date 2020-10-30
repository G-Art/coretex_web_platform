package com.coretex.core.activeorm.query.select.transformator.dip;

import com.coretex.core.activeorm.query.QueryStatementContext;
import com.coretex.core.activeorm.query.select.scanners.SelectBodyScanner;
import net.sf.jsqlparser.statement.Statement;

public class SelectBodyDataInjectionPoint extends AbstractScannerDataInjectionPoint<SelectBodyScanner> {

	public SelectBodyDataInjectionPoint(SelectBodyScanner scanner, QueryStatementContext<? extends Statement> context) {
		super(scanner, context);
	}
}
