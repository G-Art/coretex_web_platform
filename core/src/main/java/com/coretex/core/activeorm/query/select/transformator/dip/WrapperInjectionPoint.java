package com.coretex.core.activeorm.query.select.transformator.dip;

import com.coretex.core.activeorm.query.select.scanners.SelectBodyScanner;
import com.coretex.core.activeorm.query.select.transformator.DataInjectionType;

import java.util.Optional;

public class WrapperInjectionPoint<S> extends DynamicParameterHolder implements DataInjectionPoint<S> {

	private S data;

	private DataInjectionType dataInjectionType;

	public WrapperInjectionPoint(S data) {
		this.data = data;
		this.dataInjectionType = DataInjectionType.getDITypeForType(data);
	}

	public S getData() {
		return data;
	}

	@Override
	public DataInjectionType getDataInjectionType() {
		return dataInjectionType;
	}

	public void setSelectBodyScannerOwner(SelectBodyScanner selectBodyScannerOwner){
		this.addParam(SELECT_BODY_OWNER, selectBodyScannerOwner);
	}

	@Override
	public Optional<SelectBodyScanner> getSelectBodyScannerOwner() {
		return super.getSelectBodyScannerOwner();
	}
}
