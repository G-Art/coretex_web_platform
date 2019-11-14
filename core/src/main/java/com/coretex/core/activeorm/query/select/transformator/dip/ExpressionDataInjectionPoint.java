package com.coretex.core.activeorm.query.select.transformator.dip;

import com.coretex.core.activeorm.query.select.scanners.ExpressionScanner;
import com.coretex.core.activeorm.query.select.scanners.SelectBodyScanner;

import java.util.Optional;

public class ExpressionDataInjectionPoint extends AbstractScannerDataInjectionPoint<ExpressionScanner> {

	public ExpressionDataInjectionPoint(ExpressionScanner scanner) {
		super(scanner);
	}

	public void setSelectBodyScannerOwner(SelectBodyScanner selectBodyScannerOwner){
		this.addParam(SELECT_BODY_OWNER, selectBodyScannerOwner);
	}

	@Override
	public Optional<SelectBodyScanner> getSelectBodyScannerOwner() {
		return super.getSelectBodyScannerOwner();
	}
}
