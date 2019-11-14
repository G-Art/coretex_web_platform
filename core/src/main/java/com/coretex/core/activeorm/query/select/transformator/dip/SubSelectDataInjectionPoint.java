package com.coretex.core.activeorm.query.select.transformator.dip;

import com.coretex.core.activeorm.query.select.scanners.SubSelectScanner;

public class SubSelectDataInjectionPoint extends AbstractScannerDataInjectionPoint<SubSelectScanner> {

	public SubSelectDataInjectionPoint(SubSelectScanner scanner) {
		super(scanner);
	}
}
