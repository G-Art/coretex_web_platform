package com.coretex.core.activeorm.query.select.transformator.dip;

import com.coretex.core.activeorm.query.select.transformator.DataInjectionType;

public interface DataInjectionPoint<S> {
	DataInjectionType getDataInjectionType();
}
