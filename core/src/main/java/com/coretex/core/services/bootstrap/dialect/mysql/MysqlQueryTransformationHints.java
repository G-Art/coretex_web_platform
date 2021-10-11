package com.coretex.core.services.bootstrap.dialect.mysql;

import com.coretex.core.activeorm.query.select.SelectQueryTransformationHelper;
import com.coretex.core.activeorm.query.select.data.TableTransformationData;
import com.coretex.core.activeorm.query.select.scanners.ExpressionScanner;
import com.coretex.core.activeorm.query.select.scanners.SelectBodyScanner;
import com.coretex.core.activeorm.query.select.transformator.DataInjectionType;
import com.coretex.core.general.utils.AttributeTypeUtils;
import com.coretex.core.services.bootstrap.dialect.QueryTransformationHints;
import com.coretex.core.services.bootstrap.impl.CoretexContext;
import com.coretex.items.core.MetaAttributeTypeItem;
import com.coretex.items.core.MetaTypeItem;
import com.coretex.items.core.RegularTypeItem;
import com.coretex.meta.AbstractGenericItem;
import com.coretex.server.ApplicationContextProvider;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.JdbcNamedParameter;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.relational.ComparisonOperator;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class MysqlQueryTransformationHints implements QueryTransformationHints {

	private CoretexContext coretexContext;
	private SelectQueryTransformationHelper queryTransformationHelper;

	public MysqlQueryTransformationHints() {
		coretexContext = ApplicationContextProvider.getCoretexContext();
	}

	protected SelectQueryTransformationHelper getQueryTransformationHelper() {
		if (queryTransformationHelper == null) {
			queryTransformationHelper = ApplicationContextProvider.getBean(SelectQueryTransformationHelper.class);
		}
		return queryTransformationHelper;
	}

	private Function createBinToUuidFunction(Expression... expressions) {
		Function function = new Function();
		function.setName("BIN_TO_UUID");
		function.withParameters(new ExpressionList(expressions));
		return function;
	}

	private Function createUuidToBitFunction(Expression... expressions) {
		Function function = new Function();
		function.setName("UUID_TO_BIN");
		function.withParameters(new ExpressionList(expressions));
		return function;
	}

	private boolean isUuidPersistenceType(MetaAttributeTypeItem attribute) {
		return AttributeTypeUtils.isEnumTypeAttribute(attribute) ||
				AttributeTypeUtils.isItemAttribute(attribute) ||
				(AttributeTypeUtils.isRegularTypeAttribute(attribute) &&
						((RegularTypeItem) attribute.getAttributeType()).getPersistenceType()
								.equalsIgnoreCase(AbstractGenericItem.UUID));
	}

	@Override
	public Expression createColumn(TableTransformationData tableTransformationData, MetaAttributeTypeItem attribute) {
		if (isUuidPersistenceType(attribute)) {
			return createBinToUuidFunction(QueryTransformationHints.super.createColumn(tableTransformationData, attribute));
		}
		return QueryTransformationHints.super.createColumn(tableTransformationData, attribute);
	}

	@Override
	public SelectExpressionItem createSelectExpressionItem(TableTransformationData tableTransformationData, MetaAttributeTypeItem attribute) {
		SelectExpressionItem selectExpressionItem = new SelectExpressionItem();
		selectExpressionItem.setExpression(createColumn(tableTransformationData, attribute));
		if (isUuidPersistenceType(attribute)) {
			selectExpressionItem.setAlias(new Alias(attribute.getColumnName()).withUseAs(true));
		}
		return selectExpressionItem;
	}

	@Override
	public Expression createMetaEqExpression(MetaTypeItem meta, Expression left, Set<MetaTypeItem> subTypeItemSet) {
		Expression whereExpression;
		if (CollectionUtils.isNotEmpty(subTypeItemSet)) {

			whereExpression = new InExpression();
			((InExpression) whereExpression).setLeftExpression(left);
			ExpressionList expressionList = new ExpressionList();
			List<Expression> expressions = new ArrayList<>();
			expressions.add(createUuidToBitFunction(new StringValue(meta.getUuid().toString())));

			subTypeItemSet.forEach(sub -> expressions.add(createUuidToBitFunction(new StringValue(sub.getUuid().toString()))));
			expressionList.setExpressions(expressions);
			((InExpression) whereExpression).setRightItemsList(expressionList);

		} else {
			whereExpression = new EqualsTo();
			((EqualsTo) whereExpression).setLeftExpression(left);
			((EqualsTo) whereExpression).setRightExpression(createUuidToBitFunction(new StringValue(meta.getUuid().toString())));

		}
		return whereExpression;
	}

	@Override
	public void adjustParameter(ExpressionScanner expressionScanner, SelectBodyScanner selectBodyScanner) {
		ComparisonOperator comparisonOperator = (ComparisonOperator) expressionScanner.getExpression();
		List<ExpressionScanner> internalExpressions = expressionScanner.getInternalExpressions();
		List<Column> columns = internalExpressions
				.stream()
				.filter(ExpressionScanner::isColumn)
				.map(es -> (Column) es.getExpression())
				.collect(Collectors.toList());
		MetaAttributeTypeItem metaAttributeTypeItem = null;
		if (CollectionUtils.isNotEmpty(columns)) {

			for (Column column : columns) {
				TableTransformationData tableTransformationData = getQueryTransformationHelper().getTableTransformationDataForColumn(column, selectBodyScanner);
				MetaAttributeTypeItem attribute = coretexContext
						.findAttribute(
								tableTransformationData.getTypeItemBind().getTypeCode(),
								column.getColumnName().replaceAll("\"", "")
						);
				if (Objects.nonNull(attribute) && isUuidPersistenceType(attribute)) {
					metaAttributeTypeItem = attribute;
					break;
				}
			}
		}

		if (Objects.nonNull(metaAttributeTypeItem) && comparisonOperator.getRightExpression() instanceof JdbcNamedParameter) {
			comparisonOperator.setRightExpression(createUuidToBitFunction(comparisonOperator.getRightExpression()));
		}
	}

	@Override
	public void adjustColumn(ExpressionScanner scanner, TableTransformationData tableTransformationData, SelectBodyScanner ownerSelectBodyScanner) {
		Column column = (Column) scanner.getExpression();
		if (Objects.nonNull(tableTransformationData)) {
			if (Objects.isNull(column.getTable()) && !ownerSelectBodyScanner.isWrapped()) {
				column.setTable(tableTransformationData.getTable());
			}
			if (Objects.isNull(column.getTable()) && ownerSelectBodyScanner.isWrapped()) {
				var table = new Table();
				table.setAlias(((PlainSelect) ownerSelectBodyScanner.scannedObject()).getFromItem().getAlias());
				column.setTable(table);
			}

			if (tableTransformationData.isBind()) {
				MetaAttributeTypeItem metaAttributeTypeItem = ApplicationContextProvider.getCoretexContext()
						.findAttribute(
								tableTransformationData.getTypeItemBind().getTypeCode(),
								column.getColumnName().replaceAll("\"", "")
						);
				if (Objects.nonNull(metaAttributeTypeItem)) {
					if (scanner.getExpressionType() == DataInjectionType.SELECT_ITEM_EXPRESSION) {
						if (isUuidPersistenceType(metaAttributeTypeItem)) {
							SelectExpressionItem selectExpressionItem = (SelectExpressionItem) scanner.getParentStatement();
							selectExpressionItem.setExpression(createBinToUuidFunction(column));
							selectExpressionItem.setAlias(new Alias(metaAttributeTypeItem.getColumnName()));
						}
					}
					column.setColumnName(metaAttributeTypeItem.getColumnName());
				}
			}
		}

	}
}
