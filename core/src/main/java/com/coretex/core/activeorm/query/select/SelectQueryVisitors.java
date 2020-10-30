package com.coretex.core.activeorm.query.select;

import com.coretex.core.activeorm.exceptions.QueryException;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.arithmetic.*;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.*;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.*;
import net.sf.jsqlparser.statement.alter.Alter;
import net.sf.jsqlparser.statement.comment.Comment;
import net.sf.jsqlparser.statement.create.index.CreateIndex;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.create.view.AlterView;
import net.sf.jsqlparser.statement.create.view.CreateView;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.drop.Drop;
import net.sf.jsqlparser.statement.execute.Execute;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.merge.Merge;
import net.sf.jsqlparser.statement.replace.Replace;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.statement.truncate.Truncate;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.statement.upsert.Upsert;
import net.sf.jsqlparser.statement.values.ValuesStatement;

public interface SelectQueryVisitors extends SelectVisitor, FromItemVisitor, ExpressionVisitor, ItemsListVisitor, SelectItemVisitor, StatementVisitor, OrderByVisitor {

	String NOT_SUPPORTED_YET = "Not supported yet.";


	@Override
	default void visit(ShowStatement set) {

	}

	@Override
	default void visit(Table tableName) {

	}


	@Override
	default void visit(Select select) {
	}

	@Override
	default void visit(Comment comment) {
		if (comment.getTable() != null) {
			visit(comment.getTable());
		}
		if (comment.getColumn() != null) {
			Table table = comment.getColumn().getTable();
			if (table != null) {
				visit(table);
			}
		}
	}

	@Override
	default void visit(OrderByElement orderBy) {
		orderBy.getExpression().accept(this);
	}

	@Override
	default void visit(Between between) {
		between.getLeftExpression().accept(this);
		between.getBetweenExpressionStart().accept(this);
		between.getBetweenExpressionEnd().accept(this);
	}

	@Override
	default void visit(Column tableColumn) {
	}

	@Override
	default void visit(Function function) {
		ExpressionList exprList = function.getParameters();
		if (exprList != null) {
			visit(exprList);
		}
	}

	@Override
	default void visit(ValuesStatement values) {
		for (Expression expr : values.getExpressions()) {
			expr.accept(this);
		}
	}

	@Override
	default void visit(InExpression inExpression) {
		if (inExpression.getLeftExpression() != null) {
			inExpression.getLeftExpression().accept(this);
		} else if (inExpression.getLeftItemsList() != null) {
			inExpression.getLeftItemsList().accept(this);
		}
		inExpression.getRightItemsList().accept(this);
	}

	@Override
	default void visit(SignedExpression signedExpression) {
		signedExpression.getExpression().accept(this);
	}

	@Override
	default void visit(ExistsExpression existsExpression) {
		existsExpression.getRightExpression().accept(this);
	}


	@Override
	default void visit(Parenthesis parenthesis) {
		parenthesis.getExpression().accept(this);
	}

	@Override
	default void visit(ExpressionList expressionList) {
		for (Expression expression : expressionList.getExpressions()) {
			expression.accept(this);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see net.sf.jsqlparser.expression.ExpressionVisitor#visit(net.sf.jsqlparser.expression.CaseExpression)
	 */
	@Override
	default void visit(CaseExpression caseExpression) {
		if (caseExpression.getSwitchExpression() != null) {
			caseExpression.getSwitchExpression().accept(this);
		}
		if (caseExpression.getWhenClauses() != null) {
			for (WhenClause when : caseExpression.getWhenClauses()) {
				when.accept(this);
			}
		}
		if (caseExpression.getElseExpression() != null) {
			caseExpression.getElseExpression().accept(this);
		}
	}

	@Override
	default void visit(WhenClause whenClause) {
		if (whenClause.getWhenExpression() != null) {
			whenClause.getWhenExpression().accept(this);
		}
		if (whenClause.getThenExpression() != null) {
			whenClause.getThenExpression().accept(this);
		}
	}

	@Override
	default void visit(AllComparisonExpression allComparisonExpression) {
		allComparisonExpression.getSubSelect().getSelectBody().accept(this);
	}

	@Override
	default void visit(AnyComparisonExpression anyComparisonExpression) {
		anyComparisonExpression.getSubSelect().getSelectBody().accept(this);
	}

	@Override
	default void visit(SubJoin subjoin) {
		subjoin.getLeft().accept(this);
		for (Join join : subjoin.getJoinList()) {
			join.getRightItem().accept(this);
		}
	}


	@Override
	default void visit(CastExpression cast) {
		cast.getLeftExpression().accept(this);
	}

	@Override
	default void visit(SetOperationList list) {
		for (SelectBody plainSelect : list.getSelects()) {
			plainSelect.accept(this);
		}
	}
	@Override
	default void visit(LateralSubSelect lateralSubSelect) {
		lateralSubSelect.getSubSelect().getSelectBody().accept(this);
	}

	@Override
	default void visit(MultiExpressionList multiExprList) {
		for (ExpressionList exprList : multiExprList.getExprList()) {
			exprList.accept(this);
		}
	}

	@Override
	default void visit(OracleHierarchicalExpression oexpr) {
		if (oexpr.getStartExpression() != null) {
			oexpr.getStartExpression().accept(this);
		}

		if (oexpr.getConnectExpression() != null) {
			oexpr.getConnectExpression().accept(this);
		}
	}

	@Override
	default void visit(SelectExpressionItem item) {
		item.getExpression().accept(this);
	}

	@Override
	default void visit(ValueListExpression valueList) {
		valueList.getExpressionList().accept(this);
	}

	@Override
	default void visit(Delete delete) {

	}

	@Override
	default void visit(Update update) {

	}

	@Override
	default void visit(Insert insert) {

	}

	@Override
	default void visit(Replace replace) {

	}


	@Override
	default void visit(CreateTable create) {

	}

	@Override
	default void visit(RowConstructor rowConstructor) {
		for (Expression expr : rowConstructor.getExprList().getExpressions()) {
			expr.accept(this);
		}
	}


	@Override
	default void visit(Merge merge) {

	}


	@Override
	default void visit(Upsert upsert) {

	}

	@Override
	default void visit(ParenthesisFromItem parenthesis) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	default void visit(Block block) {
		if (block.getStatements() != null) {
			visit(block.getStatements());
		}
	}

	@Override
	default void visit(WithItem withItem) {
		throw new QueryException(NOT_SUPPORTED_YET + " With items query expressions.");
	}

	@Override
	default void visit(Concat concat) {
		visitBinaryExpression(concat);
	}

	@Override
	default void visit(Matches matches) {
		visitBinaryExpression(matches);
	}

	@Override
	default void visit(BitwiseAnd bitwiseAnd) {
		visitBinaryExpression(bitwiseAnd);
	}

	@Override
	default void visit(BitwiseOr bitwiseOr) {
		visitBinaryExpression(bitwiseOr);
	}

	@Override
	default void visit(BitwiseXor bitwiseXor) {
		visitBinaryExpression(bitwiseXor);
	}

	@Override
	default void visit(HexValue hexValue) {

	}
	@Override
	default void visit(NextValExpression aThis) {

	}

	@Override
	default void visit(CollateExpression aThis) {

	}

	@Override
	default void visit(DescribeStatement describe) {

	}

	@Override
	default void visit(ExplainStatement aThis) {

	}
	@Override
	default void visit(UseStatement use) {
	}

	@Override
	default void visit(OracleHint hint) {
	}

	@Override
	default void visit(TableFunction valuesList) {
	}

	@Override
	default void visit(AlterView alterView) {
		throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
	}

	@Override
	default void visit(TimeKeyExpression timeKeyExpression) {
	}

	@Override
	default void visit(DateTimeLiteralExpression literal) {

	}

	@Override
	default void visit(Commit commit) {
		throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
	}

	@Override
	default void visit(CreateView createView) {
		throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
	}

	@Override
	default void visit(Alter alter) {
		throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
	}

	@Override
	default void visit(Statements stmts) {
		throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
	}

	@Override
	default void visit(Execute execute) {
		throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
	}

	@Override
	default void visit(SetStatement set) {
		throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
	}

	@Override
	default void visit(Drop drop) {
		throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
	}

	@Override
	default void visit(Truncate truncate) {
		throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
	}

	@Override
	default void visit(CreateIndex createIndex) {
		throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
	}

	@Override
	default void visit(UserVariable var) {
	}

	@Override
	default void visit(IntegerDivision division) {

	}

	@Override
	default void visit(FullTextSearch fullTextSearch) {

	}

	@Override
	default void visit(IsBooleanExpression isBooleanExpression) {

	}

	@Override
	default void visit(SimilarToExpression aThis) {

	}

	@Override
	default void visit(ArrayExpression aThis) {

	}

	@Override
	default void visit(ShowColumnsStatement set) {

	}

	@Override
	default void visit(DeclareStatement aThis) {

	}

	@Override
	default void visit(NumericBind bind) {

	}

	@Override
	default void visit(KeepExpression aexpr) {
	}

	@Override
	default void visit(MySQLGroupConcat groupConcat) {
	}

	@Override
	default void visit(JsonExpression jsonExpr) {
	}

	@Override
	default void visit(JsonOperator jsonExpr) {
	}

	@Override
	default void visit(AllColumns allColumns) {
	}

	@Override
	default void visit(AllTableColumns allTableColumns) {
	}

	@Override
	default void visit(ValuesList valuesList) {
	}


	@Override
	default void visit(IntervalExpression iexpr) {
	}

	@Override
	default void visit(JdbcNamedParameter jdbcNamedParameter) {
	}

	@Override
	default void visit(ExtractExpression eexpr) {
	}

	@Override
	default void visit(Modulo modulo) {
		visitBinaryExpression(modulo);
	}

	@Override
	default void visit(AnalyticExpression analytic) {
	}

	@Override
	default void visit(DateValue dateValue) {
	}

	@Override
	default void visit(TimestampValue timestampValue) {
	}

	@Override
	default void visit(TimeValue timeValue) {
	}

	@Override
	default void visit(StringValue stringValue) {
	}

	@Override
	default void visit(Subtraction subtraction) {
		visitBinaryExpression(subtraction);
	}

	@Override
	default void visit(NotExpression notExpr) {
		notExpr.getExpression().accept(this);
	}

	@Override
	default void visit(BitwiseRightShift expr) {
		visitBinaryExpression(expr);
	}

	@Override
	default void visit(BitwiseLeftShift expr) {
		visitBinaryExpression(expr);
	}

	@Override
	default void visit(LongValue longValue) {
	}

	@Override
	default void visit(MinorThan minorThan) {
		visitBinaryExpression(minorThan);
	}

	@Override
	default void visit(MinorThanEquals minorThanEquals) {
		visitBinaryExpression(minorThanEquals);
	}

	@Override
	default void visit(Multiplication multiplication) {
		visitBinaryExpression(multiplication);
	}

	@Override
	default void visit(NotEqualsTo notEqualsTo) {
		visitBinaryExpression(notEqualsTo);
	}

	@Override
	default void visit(NullValue nullValue) {
	}

	@Override
	default void visit(OrExpression orExpression) {
		visitBinaryExpression(orExpression);
	}

	@Override
	default void visit(IsNullExpression isNullExpression) {
		isNullExpression.getLeftExpression().accept(this);
	}

	@Override
	default void visit(JdbcParameter jdbcParameter) {
	}

	@Override
	default void visit(LikeExpression likeExpression) {
		visitBinaryExpression(likeExpression);
	}

	@Override
	default void visit(GreaterThan greaterThan) {
		visitBinaryExpression(greaterThan);
	}

	@Override
	default void visit(GreaterThanEquals greaterThanEquals) {
		visitBinaryExpression(greaterThanEquals);
	}

	@Override
	default void visit(Division division) {
		visitBinaryExpression(division);
	}

	@Override
	default void visit(DoubleValue doubleValue) {
	}

	@Override
	default void visit(EqualsTo equalsTo) {
		visitBinaryExpression(equalsTo);
	}

	@Override
	default void visit(Addition addition) {
		visitBinaryExpression(addition);
	}

	@Override
	default void visit(AndExpression andExpression) {
		visitBinaryExpression(andExpression);
	}

	@Override
	default void visit(RegExpMatchOperator rexpr) {
		visitBinaryExpression(rexpr);
	}

	@Override
	default void visit(RegExpMySQLOperator rexpr) {
		visitBinaryExpression(rexpr);
	}

	default void visitBinaryExpression(BinaryExpression binaryExpression) {
		binaryExpression.getLeftExpression().accept(this);
		binaryExpression.getRightExpression().accept(this);
	}

	@Override
	default void visit(NamedExpressionList namedExpressionList) {
		for (Expression expression : namedExpressionList.getExpressions()) {
			expression.accept(this);
		}
	}
}
