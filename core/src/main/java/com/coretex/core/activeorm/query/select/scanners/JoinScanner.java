package com.coretex.core.activeorm.query.select.scanners;


import net.sf.jsqlparser.statement.select.Join;

import java.util.Objects;

import static com.coretex.core.activeorm.query.select.transformator.DataInjectionType.JOIN_ON_EXPRESSION;

public class JoinScanner<Q> extends Scanner<Join, Q> {

	private Join join;

	private FromItemScanner<Join> fromItemScanner;
	private ExpressionScanner<Join> joinOnExpressionScanner;

	public JoinScanner(int deep, Q parentStatement) {
		super(deep, parentStatement);
	}

	public JoinScanner(int deep, Q parentStatement, Scanner parent) {
		super(deep, parentStatement, parent);
	}

	@Override
	public void scan(Join query) {
		this.join = query;

		if(Objects.nonNull(query.getRightItem())){
			fromItemScanner = new FromItemScanner<>(getDeep() + 1, this.join, this);
			fromItemScanner.scan(query.getRightItem());
		}

		if(Objects.nonNull(query.getOnExpression())){
			joinOnExpressionScanner = new ExpressionScanner<>(getDeep() +1, this.join, this, JOIN_ON_EXPRESSION);
			joinOnExpressionScanner.scan(query.getOnExpression());
		}

		if(Objects.nonNull(query.getUsingColumns())){
			query.getUsingColumns().forEach(column -> column.accept(this));
		}

	}

	@Override
	public Join scannedObject() {
		return join;
	}

	@Override
	public Class<? extends Join> scannedObjectClass() {
		return join.getClass();
	}

	public FromItemScanner<Join> getFromItemScanner() {
		return fromItemScanner;
	}

	public ExpressionScanner<Join> getJoinOnExpressionScanner() {
		return joinOnExpressionScanner;
	}
}
