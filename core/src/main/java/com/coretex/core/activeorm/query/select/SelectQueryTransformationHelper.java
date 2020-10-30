package com.coretex.core.activeorm.query.select;

import com.coretex.core.activeorm.exceptions.QueryException;
import com.coretex.core.activeorm.query.QueryStatementContext;
import com.coretex.core.activeorm.query.select.data.AliasInfoHolder;
import com.coretex.core.activeorm.query.select.data.TableTransformationData;
import com.coretex.core.activeorm.query.select.scanners.ExpressionScanner;
import com.coretex.core.activeorm.query.select.scanners.FromItemScanner;
import com.coretex.core.activeorm.query.select.scanners.Scanner;
import com.coretex.core.activeorm.query.select.scanners.SelectBodyScanner;
import com.coretex.core.activeorm.query.select.transformator.dip.AbstractScannerDataInjectionPoint;
import com.coretex.core.activeorm.query.select.transformator.dip.WrapperInjectionPoint;
import com.coretex.core.activeorm.query.select.transformator.dip.factory.DataInjectionPointFactory;
import com.coretex.core.activeorm.query.visitors.TableAmendVisitor;
import com.coretex.core.services.bootstrap.impl.CortexContext;
import com.coretex.items.core.MetaAttributeTypeItem;
import com.coretex.items.core.MetaTypeItem;
import com.google.common.collect.Lists;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.map.SingletonMap;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

public class SelectQueryTransformationHelper {

	private CortexContext cortexContext;
	private DataInjectionPointFactory injectionPointFactory;

	@SuppressWarnings("unchecked")
	public <S extends Scanner, DIP extends AbstractScannerDataInjectionPoint<S>> DIP createDateInjectionPoint(S scanner, QueryStatementContext<? extends Statement> statementContext) {
		return (DIP) injectionPointFactory.getDataInjectionPoint(scanner, statementContext);
	}

	public <S extends Scanner<?, ?>, R extends AbstractScannerDataInjectionPoint<S>> WrapperInjectionPoint<List<R>> createDateInjectionPointsWrapper(List<S> scanners, Function<R, R> enrichFunction, QueryStatementContext<? extends Statement> statementContext) {
		return new WrapperInjectionPoint<>(scanners.stream()
				.map(scanner -> {
					R dateInjectionPoint = createDateInjectionPoint(scanner, statementContext);
					return enrichFunction.apply(dateInjectionPoint);
				}).collect(Collectors.toList()));
	}

	public SelectQueryTransformationHelper(CortexContext cortexContext, DataInjectionPointFactory dataInjectionPointFactory) {
		this.cortexContext = cortexContext;
		this.injectionPointFactory = dataInjectionPointFactory;
	}

	public PlainSelect clone(PlainSelect select) {
		try {
			return (PlainSelect) ((Select) CCJSqlParserUtil.parse(select.toString())).getSelectBody();
		} catch (JSQLParserException e) {
			throw new QueryException(e);
		}
	}

	public PlainSelect amendTable(PlainSelect clone, MetaTypeItem metaTypeItem) {
		if (clone.getFromItem() instanceof Table) {
			clone.accept(new TableAmendVisitor(((Table) clone.getFromItem()).getName(), metaTypeItem.getTableName()));
		}
		return clone;
	}

	public Expression createMetaEqExpression(MetaTypeItem meta, Expression left, Set<MetaTypeItem> subTypeItemSet) {
		Expression whereExpression;
		if (CollectionUtils.isNotEmpty(subTypeItemSet)) {
			whereExpression = new InExpression();
			((InExpression) whereExpression).setLeftExpression(left);
			ExpressionList expressionList = new ExpressionList();
			List<Expression> expressions = new ArrayList<>();
			expressions.add(new StringValue(meta.getUuid().toString()));



			subTypeItemSet.forEach(sub -> expressions.add(new StringValue(sub.getUuid().toString())));
			expressionList.setExpressions(expressions);
			((InExpression) whereExpression).setRightItemsList(expressionList);

		} else {
			whereExpression = new EqualsTo();
			((EqualsTo) whereExpression).setLeftExpression(left);
			Expression right = new StringValue(meta.getUuid().toString());
			((EqualsTo) whereExpression).setRightExpression(right);

		}
		return whereExpression;
	}

	public TableTransformationData bindItem(Table table) {
		var mType = cortexContext.findMetaType(table.getFullyQualifiedName().replaceAll("\"", ""));
		return new TableTransformationData(table, mType);
	}

	public void adjustColumn(ExpressionScanner scanner, SelectBodyScanner ownerSelectBodyScanner) {
		if (!scanner.isColumn()) {
			if(ownerSelectBodyScanner.isWrapped()
					&& scanner.isFunction()
					&& ownerSelectBodyScanner.isPlaneSelect()){
				var columns = getColumnFromFunction(scanner);
				if(!columns.isEmpty()){
					PlainSelect ps = (PlainSelect) ownerSelectBodyScanner.scannedObject();
					ps.getSelectItems().remove(scanner.getParentStatement());
					columns.forEach(column -> {
						var exp = new SelectExpressionItem(column);
						ps.getSelectItems().add(exp);
						scanner.scan(column);
					});
				}
			}
			processInternalExpression(scanner, ownerSelectBodyScanner);
		} else {
			Column column = (Column) scanner.getExpression();

			TableTransformationData tableTransformationData = getTableTransformationDataForColumn(column, ownerSelectBodyScanner);

			if (Objects.nonNull(tableTransformationData)) {
				if (Objects.isNull(column.getTable()) && !ownerSelectBodyScanner.isWrapped()) {
					column.setTable(tableTransformationData.getTable());
				}
				if(Objects.isNull(column.getTable()) && ownerSelectBodyScanner.isWrapped()){
					var table = new Table();
					table.setAlias(((PlainSelect)ownerSelectBodyScanner.scannedObject()).getFromItem().getAlias());
					column.setTable(table);
				}

				if(tableTransformationData.isBind()){
					MetaAttributeTypeItem metaAttributeTypeItem = getCortexContext()
							.findAttribute(tableTransformationData.getTypeItemBind().getTypeCode(), column.getColumnName().replaceAll("\"", ""));
					if (Objects.nonNull(metaAttributeTypeItem)) {
						column.setColumnName(metaAttributeTypeItem.getColumnName());
					}
				}
			}
		}
	}

	public TableTransformationData getTableTransformationDataForTable(Table table, SelectBodyScanner ownerSelectBodyScanner){
		var fromItemScanner = ownerSelectBodyScanner.getFromItemScanner();
		if (isSameAlias(table, fromItemScanner.scannedObject())) {
			if (fromItemScanner.isTable() && Objects.nonNull(fromItemScanner.getTableTransformationData())) {
				return fromItemScanner.getTableTransformationData();
			} else {
				Alias alias = fromItemScanner.scannedObject().getAlias();
				if (Objects.nonNull(alias)) {
					var optional = (Optional<AliasInfoHolder<?>>) ownerSelectBodyScanner.fingAliasInfoHolder(alias.getName());
					if (optional.isPresent() && optional.get().isTable()) {
						AliasInfoHolder<FromItemScanner> aliasInfoHolder = (AliasInfoHolder<FromItemScanner>) optional.get();
						return aliasInfoHolder.getOwner().getTableTransformationData();
					}
				}
			}
		} else {
			var optional = (Optional<AliasInfoHolder<?>>) ownerSelectBodyScanner.fingAliasInfoHolder(table.getName());
			if (optional.isPresent() && optional.get().isTable()) {
				AliasInfoHolder<FromItemScanner> aliasInfoHolder = (AliasInfoHolder<FromItemScanner>) optional.get();
				return aliasInfoHolder.getOwner().getTableTransformationData();
			}
		}
		return null;
	}

	public TableTransformationData getTableTransformationDataForColumn(Column column, SelectBodyScanner ownerSelectBodyScanner) {
		var fromItemScanner = ownerSelectBodyScanner.getFromItemScanner();
		var table = column.getTable();
		if (isNull(table) && fromItemScanner.isTable() && Objects.nonNull(fromItemScanner.getTableTransformationData())) {
			return fromItemScanner.getTableTransformationData();
		}
		return getTableTransformationDataForTable(table, ownerSelectBodyScanner);
	}

	private List<Expression> getColumnFromFunction(ExpressionScanner scanner) {
		var columns = Lists.<Expression>newArrayList();
			if(scanner.isColumn()){
				columns.add(scanner.getExpression());
			}
			scanner.getInternalExpressions()
					.forEach(exp-> columns.addAll(getColumnFromFunction((ExpressionScanner)exp)));

		return columns;
	}

	private void processInternalExpression(ExpressionScanner expressionScanner, SelectBodyScanner selectBodyScanner) {
		if (expressionScanner.isColumn()) {
			adjustColumn(expressionScanner, selectBodyScanner);
		} else {
			List<ExpressionScanner> internalExpressions = expressionScanner.getInternalExpressions();
			internalExpressions.forEach(internalExpression -> processInternalExpression(internalExpression, selectBodyScanner));
		}
	}

	private boolean isSameAlias(FromItem left, FromItem right) {
		if (isNull(left) || isNull(right) || isNull(left.getAlias()) || isNull(right.getAlias())) {
			return false;
		}
		SingletonMap<String, String> leftTableAliasPair = new SingletonMap<>(retrieveName(left), retrieveAlias(left));
		SingletonMap<String, String> rightTableAliasPair = new SingletonMap<>(retrieveName(right), retrieveAlias(right));

		return leftTableAliasPair.containsKey(rightTableAliasPair.getKey()) || leftTableAliasPair.containsValue(rightTableAliasPair.getValue());
	}

	private String retrieveAlias(FromItem fromItem) {
		if (fromItem instanceof Table) {
			return Objects.isNull(fromItem.getAlias()) ? ((Table) fromItem).getName() : fromItem.getAlias().getName();
		} else {
			return Objects.isNull(fromItem.getAlias()) ? "" : fromItem.getAlias().getName();
		}
	}

	private String retrieveName(FromItem fromItem) {
		if (fromItem instanceof Table) {
			return ((Table) fromItem).getName();
		} else {
			return Objects.isNull(fromItem.getAlias()) ? "" : fromItem.getAlias().getName();
		}
	}

	public CortexContext getCortexContext() {
		return cortexContext;
	}
}
