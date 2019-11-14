package com.coretex.core.activeorm.query.select.transformator.dip;

import com.coretex.core.activeorm.query.select.scanners.SelectBodyScanner;

public class SelectBodyDataInjectionPoint extends AbstractScannerDataInjectionPoint<SelectBodyScanner> {

	public SelectBodyDataInjectionPoint(SelectBodyScanner scanner) {
		super(scanner);
	}
}
