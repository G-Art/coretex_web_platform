package com.coretex.core.activeorm.query.select.transformator.dip;

import com.coretex.core.activeorm.query.QueryStatementContext;
import com.coretex.core.activeorm.query.select.scanners.SubSelectScanner;
import net.sf.jsqlparser.statement.Statement;

public class SubSelectDataInjectionPoint extends AbstractScannerDataInjectionPoint<SubSelectScanner> {

	public SubSelectDataInjectionPoint(SubSelectScanner scanner, QueryStatementContext<? extends Statement> context) {
		super(scanner, context);
	}
}
