package com.coretex.core.activeorm.query.select.scanners;

import com.coretex.core.activeorm.query.select.SelectQueryTransformationHelper;
import com.coretex.core.activeorm.query.select.SelectQueryVisitors;
import com.coretex.server.ApplicationContextProvider;
import net.sf.jsqlparser.expression.AllComparisonExpression;
import net.sf.jsqlparser.expression.AnyComparisonExpression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.statement.CreateFunctionalStatement;
import net.sf.jsqlparser.statement.alter.sequence.AlterSequence;
import net.sf.jsqlparser.statement.create.schema.CreateSchema;
import net.sf.jsqlparser.statement.create.sequence.CreateSequence;
import net.sf.jsqlparser.statement.grant.Grant;
import net.sf.jsqlparser.statement.select.*;

import java.util.Objects;

public abstract class Scanner<T, Q> implements SelectQueryVisitors {

	private int deep;

	private Q parentStatement;

	private Scanner parent;

	private boolean wrapperRequired = false;

	protected static SelectQueryTransformationHelper transformationHelper;

	public Scanner(int deep, Q parentStatement) {
		this.deep = deep;
		this.parentStatement = parentStatement;
		if(Objects.isNull(transformationHelper)){
			transformationHelper = ApplicationContextProvider.getApplicationContext().getBean(SelectQueryTransformationHelper.class);
		}
	}

	public Scanner(int deep, Q parentStatement, Scanner parent) {
		this(deep, parentStatement);
		this.parent = parent;
	}

	public abstract void scan(T query);

	public abstract T scannedObject();

	public abstract Class<? extends T> scannedObjectClass();

	@Override
	public void visit(CreateFunctionalStatement createFunctionalStatement) {
		throw new UnsupportedOperationException("Not supported.");
	}

	@Override
	public void visit(CreateSchema aThis) {
		throw new UnsupportedOperationException("Not supported.");
	}

	@Override
	public void visit(Grant grant) {
		throw new UnsupportedOperationException("Not supported.");
	}

	@Override
	public void visit(CreateSequence createSequence) {
		throw new UnsupportedOperationException("Not supported.");
	}

	@Override
	public void visit(SelectExpressionItem item) {
		item.getExpression().accept(this);
	}

	@Override
	public void visit(SetOperationList list) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void visit(PlainSelect plainSelect) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void visit(SubSelect subSelect) {
		SubSelectScanner<Q> subSelectScanner = new SubSelectScanner<>(deep + 1, getParentStatement(), this);
		subSelectScanner.scan(subSelect);
	}

	@Override
	public void visit(AlterSequence alterSequence) {
		throw new UnsupportedOperationException("Not supported.");
	}

	@Override
	public void visit(LateralSubSelect lateralSubSelect) {
		SubSelect subSelect = lateralSubSelect.getSubSelect();
		SubSelectScanner<LateralSubSelect> subSelectScanner = new SubSelectScanner<>(deep + 1, lateralSubSelect, this);
		subSelectScanner.scan(subSelect);
	}

	@Override
	public void visit(AllComparisonExpression allComparisonExpression) {
		SubSelect subSelect = allComparisonExpression.getSubSelect();
		SubSelectScanner<AllComparisonExpression> subSelectScanner = new SubSelectScanner<>(deep + 1, allComparisonExpression, this);
		subSelectScanner.scan(subSelect);
	}

	@Override
	public void visit(AnyComparisonExpression anyComparisonExpression) {
		SubSelect subSelect = anyComparisonExpression.getSubSelect();
		SubSelectScanner<AnyComparisonExpression> subSelectScanner = new SubSelectScanner<>(deep + 1, anyComparisonExpression, this);
		subSelectScanner.scan(subSelect);
	}

	@Override
	public void visit(Function function) {
		if (!this.wrapperRequired) {
			this.wrapperRequired = true;
		}
		ExpressionList exprList = function.getParameters();
		if (exprList != null) {
			visit(exprList);
		}
	}

	public Scanner getParent() {
		return parent;
	}

	public Q getParentStatement() {
		return parentStatement;
	}

	public Class<Q> getParentStatementClass() {
		return (Class<Q>) parentStatement.getClass();
	}

	public int getDeep() {
		return this.deep;
	}
}
