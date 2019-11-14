package com.coretex.core.activeorm.query.select.transformator.dip;

import com.coretex.core.activeorm.query.select.scanners.SelectBodyScanner;
import com.google.common.collect.Maps;

import java.util.Map;
import java.util.Optional;

public abstract class DynamicParameterHolder {

	protected static final String SELECT_BODY_OWNER = "SELECT_BODY_OWNER";

	private Map<String, Object> params = Maps.newHashMap();

	protected <P> P getParam(String key) {
		return (P) params.get(key);
	}

	protected <P> void addParam(String key, P param) {
		params.put(key, param);
	}

	protected Optional<SelectBodyScanner> getSelectBodyScannerOwner(){
		return Optional.ofNullable(this.getParam(SELECT_BODY_OWNER));
	}
}
