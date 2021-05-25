package com.coretex.core.activeorm.query.select.transformator.dip.factory;

import com.coretex.core.activeorm.query.operations.dataholders.QueryInfoHolder;
import com.coretex.core.activeorm.query.select.scanners.Scanner;
import com.coretex.core.activeorm.query.select.transformator.dip.AbstractScannerDataInjectionPoint;
import net.sf.jsqlparser.statement.Statement;

public interface DataInjectionPointFactory {

	<S extends Scanner> AbstractScannerDataInjectionPoint<? extends Scanner> getDataInjectionPoint(S scanner, QueryInfoHolder<? extends Statement> statementContext);
 }
