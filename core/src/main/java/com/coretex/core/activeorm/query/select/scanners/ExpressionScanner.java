package com.coretex.core.activeorm.query.select.scanners;


import com.coretex.core.activeorm.query.select.transformator.DataInjectionType;
import com.coretex.items.core.GenericItem;
import com.google.common.collect.Lists;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.schema.Column;

import java.util.List;
import java.util.Objects;

import static com.coretex.core.activeorm.query.select.transformator.DataInjectionType.COLUMN_EXPRESSION;
import static com.coretex.core.activeorm.query.select.transformator.DataInjectionType.FUNCTION_EXPRESSION;

public class ExpressionScanner<Q> extends Scanner<Expression, Q> {

	private Expression expression;
	private boolean isColumn = false;
	private boolean isUUIDColumn = false;
	private boolean isFunction = false;

	private List<ExpressionScanner> internalExpressions = Lists.newArrayList();

	private DataInjectionType expresionType;

	public ExpressionScanner(int deep, Q parentStatement, DataInjectionType expresionType) {
		super(deep, parentStatement);
		this.expresionType = expresionType;
	}

	public ExpressionScanner(int deep, Q parentStatement, Scanner parent, DataInjectionType expresionType) {
		super(deep, parentStatement, parent);
		this.expresionType = expresionType;
	}

	@Override
	public void scan(Expression query) {
		this.expression = query;
		query.accept(this);
	}

	@Override
	public Expression scannedObject() {
		return expression;
	}

	@Override
	public Class<? extends Expression> scannedObjectClass() {
		return expression.getClass();
	}

	@Override
	public void visit(Function function) {
		if(expression != function){
			ExpressionScanner internalExpression = new ExpressionScanner<>(getDeep()+1, expression, this, FUNCTION_EXPRESSION);
			internalExpressions.add(internalExpression);
			internalExpression.scan(function);
		}else{
			this.isFunction = true;
		}
	}

	@Override
	public void visit(Column tableColumn) {
		if(expression != tableColumn){
			ExpressionScanner internalExpression = new ExpressionScanner<>(getDeep()+1, expression, this, COLUMN_EXPRESSION);
			internalExpressions.add(internalExpression);
			internalExpression.scan(tableColumn);
		}else{
			if(tableColumn.getColumnName().equals(GenericItem.UUID)){
				Scanner prnt = getParent();
				while (Objects.nonNull(prnt)){
					if(prnt instanceof SelectBodyScanner){
						((SelectBodyScanner) prnt).setUUIDColumnPresent(true);
						prnt=null;
					}else{
						prnt = prnt.getParent();
					}
				}
				this.isUUIDColumn = true;
			}
			this.isColumn = true;
		}
	}

	public boolean isUUIDColumn() {
		return isUUIDColumn;
	}

	public boolean isColumn() {
		return isColumn;
	}

	public boolean isFunction() {
		return isFunction;
	}

	public Expression getExpression() {
		return expression;
	}

	public List<ExpressionScanner> getInternalExpressions() {
		return internalExpressions;
	}

	public DataInjectionType getExpresionType() {
		return expresionType;
	}
}
