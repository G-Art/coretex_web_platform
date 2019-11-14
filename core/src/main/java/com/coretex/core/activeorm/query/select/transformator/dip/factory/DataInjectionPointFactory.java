package com.coretex.core.activeorm.query.select.transformator.dip.factory;

import com.coretex.core.activeorm.query.select.scanners.Scanner;
import com.coretex.core.activeorm.query.select.transformator.dip.AbstractScannerDataInjectionPoint;

public interface DataInjectionPointFactory {

	<S extends Scanner> AbstractScannerDataInjectionPoint<? extends Scanner> getDataInjectionPoint(S scanner);
 }
