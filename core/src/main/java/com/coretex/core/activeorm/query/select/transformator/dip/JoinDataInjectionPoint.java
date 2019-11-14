package com.coretex.core.activeorm.query.select.transformator.dip;

import com.coretex.core.activeorm.query.select.scanners.JoinScanner;
import com.coretex.core.activeorm.query.select.scanners.SelectBodyScanner;

import java.util.Optional;

public class JoinDataInjectionPoint extends AbstractScannerDataInjectionPoint<JoinScanner> {

	public JoinDataInjectionPoint(JoinScanner scanner) {
		super(scanner);
	}

	public void setSelectBodyScannerOwner(SelectBodyScanner selectBodyScannerOwner){
		this.addParam(SELECT_BODY_OWNER, selectBodyScannerOwner);
	}

	@Override
	public Optional<SelectBodyScanner> getSelectBodyScannerOwner(){
		return super.getSelectBodyScannerOwner();
	}
}
