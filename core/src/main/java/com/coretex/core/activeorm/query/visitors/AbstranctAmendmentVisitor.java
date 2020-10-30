package com.coretex.core.activeorm.query.visitors;

import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.arithmetic.*;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.*;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.*;
import net.sf.jsqlparser.statement.alter.Alter;
import net.sf.jsqlparser.statement.alter.sequence.AlterSequence;
import net.sf.jsqlparser.statement.comment.Comment;
import net.sf.jsqlparser.statement.create.index.CreateIndex;
import net.sf.jsqlparser.statement.create.schema.CreateSchema;
import net.sf.jsqlparser.statement.create.sequence.CreateSequence;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.create.view.AlterView;
import net.sf.jsqlparser.statement.create.view.CreateView;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.drop.Drop;
import net.sf.jsqlparser.statement.execute.Execute;
import net.sf.jsqlparser.statement.grant.Grant;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.merge.Merge;
import net.sf.jsqlparser.statement.replace.Replace;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.statement.truncate.Truncate;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.statement.upsert.Upsert;
import net.sf.jsqlparser.statement.values.ValuesStatement;

import java.util.Iterator;

public abstract class AbstranctAmendmentVisitor implements SelectVisitor, FromItemVisitor, ExpressionVisitor, ItemsListVisitor, SelectItemVisitor, StatementVisitor {
	private static final String NOT_SUPPORTED_YET = "Not supported yet.";
	private boolean allowColumnProcessing = true;


	public void visit(Select select) {
		if (select.getWithItemsList() != null) {
			Iterator var2 = select.getWithItemsList().iterator();

			while (var2.hasNext()) {
				WithItem withItem = (WithItem) var2.next();
				withItem.accept(this);
			}
		}

		select.getSelectBody().accept(this);
	}

	@Override
	public void visit(CreateFunctionalStatement createFunctionalStatement) {

	}

	@Override
	public void visit(CreateSchema aThis) {

	}

	@Override
	public void visit(Grant grant) {

	}

	@Override
	public void visit(CreateSequence createSequence) {

	}

	@Override
	public void visit(AlterSequence alterSequence) {

	}

	public void visit(WithItem withItem) {
		withItem.getSelectBody().accept(this);
	}

	public void visit(PlainSelect plainSelect) {
		Iterator var2;
		if (plainSelect.getSelectItems() != null) {
			var2 = plainSelect.getSelectItems().iterator();

			while (var2.hasNext()) {
				SelectItem item = (SelectItem) var2.next();
				item.accept(this);
			}
		}

		if (plainSelect.getFromItem() != null) {
			plainSelect.getFromItem().accept(this);
		}

		if (plainSelect.getJoins() != null) {
			var2 = plainSelect.getJoins().iterator();

			while (var2.hasNext()) {
				Join join = (Join) var2.next();
				join.getRightItem().accept(this);
			}
		}

		if (plainSelect.getWhere() != null) {
			plainSelect.getWhere().accept(this);
		}

		if (plainSelect.getHaving() != null) {
			plainSelect.getHaving().accept(this);
		}

		if (plainSelect.getOracleHierarchical() != null) {
			plainSelect.getOracleHierarchical().accept(this);
		}

	}

	@Override
	public void visit(ValuesStatement values) {
		for (Expression expr : values.getExpressions()) {
			expr.accept(this);
		}
	}

	@Override
	public void visit(DescribeStatement describe) {

	}

	@Override
	public void visit(ExplainStatement aThis) {

	}

	public void visit(Table tableName) {
	}

	public void visit(SubSelect subSelect) {
		if (subSelect.getWithItemsList() != null) {
			Iterator var2 = subSelect.getWithItemsList().iterator();

			while (var2.hasNext()) {
				WithItem withItem = (WithItem) var2.next();
				withItem.accept(this);
			}
		}

		subSelect.getSelectBody().accept(this);
	}

	public void visit(Addition addition) {
		this.visitBinaryExpression(addition);
	}

	public void visit(AndExpression andExpression) {
		this.visitBinaryExpression(andExpression);
	}

	public void visit(Between between) {
		between.getLeftExpression().accept(this);
		between.getBetweenExpressionStart().accept(this);
		between.getBetweenExpressionEnd().accept(this);
	}

	public void visit(Column tableColumn) {
		if (this.allowColumnProcessing && tableColumn.getTable() != null && tableColumn.getTable().getName() != null) {
			this.visit(tableColumn.getTable());
		}
	}

	public void visit(Division division) {
		this.visitBinaryExpression(division);
	}

	public void visit(DoubleValue doubleValue) {
	}

	public void visit(EqualsTo equalsTo) {
		this.visitBinaryExpression(equalsTo);
	}

	public void visit(Function function) {
		ExpressionList exprList = function.getParameters();
		if (exprList != null) {
			this.visit(exprList);
		}

	}

	public void visit(GreaterThan greaterThan) {
		this.visitBinaryExpression(greaterThan);
	}

	public void visit(GreaterThanEquals greaterThanEquals) {
		this.visitBinaryExpression(greaterThanEquals);
	}

	public void visit(InExpression inExpression) {
		if (inExpression.getLeftExpression() != null) {
			inExpression.getLeftExpression().accept(this);
		} else if (inExpression.getLeftItemsList() != null) {
			inExpression.getLeftItemsList().accept(this);
		}

		inExpression.getRightItemsList().accept(this);
	}

	public void visit(SignedExpression signedExpression) {
		signedExpression.getExpression().accept(this);
	}

	public void visit(IsNullExpression isNullExpression) {
	}

	public void visit(JdbcParameter jdbcParameter) {
	}

	public void visit(LikeExpression likeExpression) {
		this.visitBinaryExpression(likeExpression);
	}

	public void visit(ExistsExpression existsExpression) {
		existsExpression.getRightExpression().accept(this);
	}

	public void visit(LongValue longValue) {
	}

	public void visit(MinorThan minorThan) {
		this.visitBinaryExpression(minorThan);
	}

	public void visit(MinorThanEquals minorThanEquals) {
		this.visitBinaryExpression(minorThanEquals);
	}

	public void visit(Multiplication multiplication) {
		this.visitBinaryExpression(multiplication);
	}

	public void visit(NotEqualsTo notEqualsTo) {
		this.visitBinaryExpression(notEqualsTo);
	}

	public void visit(NullValue nullValue) {
	}

	public void visit(OrExpression orExpression) {
		this.visitBinaryExpression(orExpression);
	}

	public void visit(Parenthesis parenthesis) {
		parenthesis.getExpression().accept(this);
	}

	public void visit(StringValue stringValue) {
	}

	public void visit(Subtraction subtraction) {
		this.visitBinaryExpression(subtraction);
	}

	public void visit(NotExpression notExpr) {
		notExpr.getExpression().accept(this);
	}

	@Override
	public void visit(NextValExpression aThis) {

	}

	@Override
	public void visit(CollateExpression aThis) {

	}

	@Override
	public void visit(IntegerDivision division) {

	}

	@Override
	public void visit(FullTextSearch fullTextSearch) {

	}

	@Override
	public void visit(IsBooleanExpression isBooleanExpression) {

	}

	@Override
	public void visit(SimilarToExpression aThis) {

	}

	@Override
	public void visit(ArrayExpression aThis) {

	}

	@Override
	public void visit(ShowColumnsStatement set) {

	}

	@Override
	public void visit(DeclareStatement aThis) {

	}

	public void visit(BitwiseRightShift expr) {
		this.visitBinaryExpression(expr);
	}

	public void visit(BitwiseLeftShift expr) {
		this.visitBinaryExpression(expr);
	}

	public void visitBinaryExpression(BinaryExpression binaryExpression) {
		binaryExpression.getLeftExpression().accept(this);
		binaryExpression.getRightExpression().accept(this);
	}

	public void visit(ExpressionList expressionList) {
		Iterator var2 = expressionList.getExpressions().iterator();

		while (var2.hasNext()) {
			Expression expression = (Expression) var2.next();
			expression.accept(this);
		}

	}

	public void visit(DateValue dateValue) {
	}

	public void visit(TimestampValue timestampValue) {
	}

	public void visit(TimeValue timeValue) {
	}

	public void visit(CaseExpression caseExpression) {
		if (caseExpression.getSwitchExpression() != null) {
			caseExpression.getSwitchExpression().accept(this);
		}

		if (caseExpression.getWhenClauses() != null) {
			Iterator var2 = caseExpression.getWhenClauses().iterator();

			while (var2.hasNext()) {
				WhenClause when = (WhenClause) var2.next();
				when.accept(this);
			}
		}

		if (caseExpression.getElseExpression() != null) {
			caseExpression.getElseExpression().accept(this);
		}

	}

	public void visit(WhenClause whenClause) {
		if (whenClause.getWhenExpression() != null) {
			whenClause.getWhenExpression().accept(this);
		}

		if (whenClause.getThenExpression() != null) {
			whenClause.getThenExpression().accept(this);
		}

	}

	public void visit(AllComparisonExpression allComparisonExpression) {
		allComparisonExpression.getSubSelect().getSelectBody().accept(this);
	}

	public void visit(AnyComparisonExpression anyComparisonExpression) {
		anyComparisonExpression.getSubSelect().getSelectBody().accept(this);
	}

	public void visit(SubJoin subjoin) {
		subjoin.getLeft().accept(this);
		Iterator var2 = subjoin.getJoinList().iterator();

		while (var2.hasNext()) {
			Join join = (Join) var2.next();
			join.getRightItem().accept(this);
		}

	}

	public void visit(Concat concat) {
		this.visitBinaryExpression(concat);
	}

	public void visit(Matches matches) {
		this.visitBinaryExpression(matches);
	}

	public void visit(BitwiseAnd bitwiseAnd) {
		this.visitBinaryExpression(bitwiseAnd);
	}

	public void visit(BitwiseOr bitwiseOr) {
		this.visitBinaryExpression(bitwiseOr);
	}

	public void visit(BitwiseXor bitwiseXor) {
		this.visitBinaryExpression(bitwiseXor);
	}

	public void visit(CastExpression cast) {
		cast.getLeftExpression().accept(this);
	}

	public void visit(Modulo modulo) {
		this.visitBinaryExpression(modulo);
	}

	public void visit(AnalyticExpression analytic) {
	}

	public void visit(SetOperationList list) {
		Iterator var2 = list.getSelects().iterator();

		while (var2.hasNext()) {
			SelectBody plainSelect = (SelectBody) var2.next();
			plainSelect.accept(this);
		}

	}

	public void visit(ExtractExpression eexpr) {
	}

	public void visit(LateralSubSelect lateralSubSelect) {
		lateralSubSelect.getSubSelect().getSelectBody().accept(this);
	}

	public void visit(MultiExpressionList multiExprList) {
		Iterator var2 = multiExprList.getExprList().iterator();

		while (var2.hasNext()) {
			ExpressionList exprList = (ExpressionList) var2.next();
			exprList.accept(this);
		}

	}

	public void visit(ValuesList valuesList) {
	}

	public void visit(IntervalExpression iexpr) {
	}

	public void visit(JdbcNamedParameter jdbcNamedParameter) {
	}

	public void visit(OracleHierarchicalExpression oexpr) {
		if (oexpr.getStartExpression() != null) {
			oexpr.getStartExpression().accept(this);
		}

		if (oexpr.getConnectExpression() != null) {
			oexpr.getConnectExpression().accept(this);
		}

	}

	public void visit(RegExpMatchOperator rexpr) {
		this.visitBinaryExpression(rexpr);
	}

	public void visit(RegExpMySQLOperator rexpr) {
		this.visitBinaryExpression(rexpr);
	}

	public void visit(JsonExpression jsonExpr) {
	}

	public void visit(JsonOperator jsonExpr) {
	}

	public void visit(AllColumns allColumns) {
	}

	public void visit(AllTableColumns allTableColumns) {
	}

	public void visit(SelectExpressionItem item) {
		item.getExpression().accept(this);
	}

	public void visit(UserVariable var) {
	}

	public void visit(NumericBind bind) {
	}

	public void visit(KeepExpression aexpr) {
	}

	public void visit(MySQLGroupConcat groupConcat) {
	}

	public void visit(ValueListExpression valueList) {
		valueList.getExpressionList().accept(this);
	}

	public void visit(Delete delete) {
		this.visit(delete.getTable());
		if (delete.getJoins() != null) {
			Iterator var2 = delete.getJoins().iterator();

			while (var2.hasNext()) {
				Join join = (Join) var2.next();
				join.getRightItem().accept(this);
			}
		}

		if (delete.getWhere() != null) {
			delete.getWhere().accept(this);
		}

	}

	@Override
	public void visit(Update update) {
		visit(update.getTable());
		if (update.getStartJoins() != null) {
			for (Join join : update.getStartJoins()) {
				join.getRightItem().accept(this);
			}
		}
		if (update.getExpressions() != null) {
			for (Expression expression : update.getExpressions()) {
				expression.accept(this);
			}
		}

		if (update.getFromItem() != null) {
			update.getFromItem().accept(this);
		}

		if (update.getJoins() != null) {
			for (Join join : update.getJoins()) {
				join.getRightItem().accept(this);
			}
		}

		if (update.getWhere() != null) {
			update.getWhere().accept(this);
		}
	}

	public void visit(Insert insert) {
		this.visit(insert.getTable());
		if (insert.getItemsList() != null) {
			insert.getItemsList().accept(this);
		}

		if (insert.getSelect() != null) {
			this.visit(insert.getSelect());
		}

	}

	public void visit(Replace replace) {
		this.visit(replace.getTable());
		if (replace.getExpressions() != null) {
			Iterator var2 = replace.getExpressions().iterator();

			while (var2.hasNext()) {
				Expression expression = (Expression) var2.next();
				expression.accept(this);
			}
		}

		if (replace.getItemsList() != null) {
			replace.getItemsList().accept(this);
		}

	}

	public void visit(Drop drop) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void visit(Truncate truncate) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void visit(CreateIndex createIndex) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void visit(CreateTable create) {
		this.visit(create.getTable());
		if (create.getSelect() != null) {
			create.getSelect().accept(this);
		}

	}

	public void visit(CreateView createView) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void visit(Alter alter) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void visit(Statements stmts) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void visit(Execute execute) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void visit(SetStatement set) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void visit(RowConstructor rowConstructor) {
		Iterator var2 = rowConstructor.getExprList().getExpressions().iterator();

		while (var2.hasNext()) {
			Expression expr = (Expression) var2.next();
			expr.accept(this);
		}

	}

	public void visit(HexValue hexValue) {
	}

	public void visit(Merge merge) {
		this.visit(merge.getTable());
		if (merge.getUsingTable() != null) {
			merge.getUsingTable().accept(this);
		}
	}

	public void visit(OracleHint hint) {
	}

	public void visit(TableFunction valuesList) {
	}

	public void visit(AlterView alterView) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void visit(TimeKeyExpression timeKeyExpression) {
	}

	public void visit(DateTimeLiteralExpression literal) {
	}

	public void visit(Commit commit) {
	}

	public void visit(Upsert upsert) {
		this.visit(upsert.getTable());
		if (upsert.getItemsList() != null) {
			upsert.getItemsList().accept(this);
		}

		if (upsert.getSelect() != null) {
			this.visit(upsert.getSelect());
		}

	}

	public void visit(UseStatement use) {
	}

	public void visit(ParenthesisFromItem parenthesis) {
		parenthesis.getFromItem().accept(this);
	}

	public void visit(Block block) {
		if (block.getStatements() != null) {
			this.visit(block.getStatements());
		}

	}

	@Override
	public void visit(NamedExpressionList namedExpressionList) {
		for (Expression expression : namedExpressionList.getExpressions()) {
			expression.accept(this);
		}
	}

	@Override
	public void visit(Comment comment) {
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
	public void visit(ShowStatement set) {
		throw new UnsupportedOperationException(NOT_SUPPORTED_YET);
	}
}
