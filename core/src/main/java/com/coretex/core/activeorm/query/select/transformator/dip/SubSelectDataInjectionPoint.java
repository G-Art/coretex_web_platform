package com.coretex.core.activeorm.query.select.transformator.dip;

import com.coretex.core.activeorm.query.operations.dataholders.QueryInfoHolder;
import com.coretex.core.activeorm.query.select.scanners.SubSelectScanner;
import net.sf.jsqlparser.statement.Statement;

public class SubSelectDataInjectionPoint extends AbstractScannerDataInjectionPoint<SubSelectScanner> {

	public SubSelectDataInjectionPoint(SubSelectScanner scanner, QueryInfoHolder<? extends Statement> context) {
		super(scanner, context);
	}
}
