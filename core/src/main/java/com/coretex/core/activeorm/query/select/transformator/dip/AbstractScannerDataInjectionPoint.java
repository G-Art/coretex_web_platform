package com.coretex.core.activeorm.query.select.transformator.dip;

import com.coretex.core.activeorm.query.operations.dataholders.QueryInfoHolder;
import com.coretex.core.activeorm.query.select.scanners.Scanner;
import com.coretex.core.activeorm.query.select.transformator.DataInjectionType;
import net.sf.jsqlparser.statement.Statement;

import java.util.Objects;
import java.util.Optional;

public abstract class AbstractScannerDataInjectionPoint<S extends Scanner> extends DynamicParameterHolder implements DataInjectionPoint<S> {

	private S scanner;
	private QueryInfoHolder<? extends Statement> context;

	private DataInjectionType dataInjectionType;

	public AbstractScannerDataInjectionPoint(S scanner, QueryInfoHolder<? extends Statement> context) {
		this.scanner = scanner;
		this.context = context;
		this.dataInjectionType = DataInjectionType.getTypeForScanner(scanner);
	}

	public S getScanner() {
		return scanner;
	}

	public QueryInfoHolder<? extends Statement> getContext() {
		return context;
	}

	@Override
	public DataInjectionType getDataInjectionType() {
		return dataInjectionType;
	}

	protected <T, S extends Scanner> Optional<S> findParentByType(Class<T> tClass){
		Scanner parent = getScanner().getParent();
		while (Objects.nonNull(parent)){
			if(tClass.isAssignableFrom(parent.getClass()) || tClass.isAssignableFrom(parent.scannedObjectClass())){
				return Optional.of((S)parent);
			}
			parent = parent.getParent();
		}
		return Optional.empty();
	}

}
