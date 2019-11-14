package com.coretex.core.activeorm.query.select.scanners;


import com.coretex.core.activeorm.query.select.data.AliasInfoHolder;
import com.coretex.core.activeorm.query.visitors.TableAmendVisitor;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.statement.select.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.coretex.core.activeorm.query.select.transformator.DataInjectionType.GROUP_BY_EXPRESSION;
import static com.coretex.core.activeorm.query.select.transformator.DataInjectionType.HAVING_EXPRESSION;
import static com.coretex.core.activeorm.query.select.transformator.DataInjectionType.ORDER_BY_EXPRESSION;
import static com.coretex.core.activeorm.query.select.transformator.DataInjectionType.WHERE_EXPRESSION;


public class SelectBodyScanner<Q> extends Scanner<SelectBody, Q> {

	private SelectBody selectBody;

	private boolean isPlaneSelect = false;

	private boolean wrapped = false;

	private boolean isUUIDColumnPresent = false;

	private FromItemScanner fromItemScanner;
	private ExpressionScanner whereExpresionScanner;
	private ExpressionScanner havingExpresionScanner;

	private List<Scanner> subSelectScanners = Lists.newArrayList();
	private List<ExpressionScanner> orderByExpresionScanners = Lists.newArrayList();
	private List<ExpressionScanner> groupByExpresionScanners = Lists.newArrayList();
	private List<SelectItemScanner<?>> selectItemScanners = Lists.newArrayList();
	private List<JoinScanner> joinScanners = Lists.newArrayList();

	private Map<String, AliasInfoHolder<?>> aliasInfoHolderMap = Maps.newHashMap();

	public SelectBodyScanner(int deep, Q parentStatement) {
		super(deep, parentStatement);
	}

	public SelectBodyScanner(int deep, Q parentStatement, Scanner parent) {
		super(deep, parentStatement, parent);
	}

	@Override
	public void scan(SelectBody query) {
		this.selectBody = query;
		query.accept(this);
	}

	@Override
	public void visit(SetOperationList list) {
		for (SelectBody plainSelect : list.getSelects()) {
			SelectBodyScanner<SetOperationList> operationListSelectBodyScanner = new SelectBodyScanner<>(getDeep() + 1, list, this);
			subSelectScanners.add(operationListSelectBodyScanner);
			operationListSelectBodyScanner.scan(plainSelect);
		}
	}

	@Override
	public void visit(PlainSelect plainSelect) {
		isPlaneSelect = true;
		if (plainSelect.getFromItem() != null) {
			this.fromItemScanner = new FromItemScanner<>(getDeep() + 1, plainSelect, this);
			fromItemScanner.scan(plainSelect.getFromItem());
		}

		if (plainSelect.getSelectItems() != null) {
			int index = 0;
			for (SelectItem item : plainSelect.getSelectItems()) {
				SelectItemScanner<PlainSelect> selectItemScanner = new SelectItemScanner<>(getDeep() + 1, plainSelect, this, index);
				selectItemScanners.add(selectItemScanner);
				selectItemScanner.scan(item);
			}
		}

		if (plainSelect.getJoins() != null) {
			for (Join join : plainSelect.getJoins()) {
				JoinScanner<PlainSelect> joinScanner = new JoinScanner<>(getDeep() + 1, plainSelect, this);
				joinScanners.add(joinScanner);
				joinScanner.scan(join);
			}
		}

		whereExpresionScanner = new ExpressionScanner<>(getDeep() + 1, plainSelect, this, WHERE_EXPRESSION);
		if (plainSelect.getWhere() != null) {
			whereExpresionScanner.scan(plainSelect.getWhere());
		}

		if (plainSelect.getHaving() != null) {
			havingExpresionScanner = new ExpressionScanner<>(getDeep() + 1, plainSelect, this, HAVING_EXPRESSION);
			havingExpresionScanner.scan(plainSelect.getHaving());
		}

		//is not supported yet
//		if (plainSelect.getOracleHierarchical() != null) {
//			ExpressionScanner<PlainSelect> expressionScanner = new ExpressionScanner<>(getDeep() +1, plainSelect, this);
//			subScanners.add(expressionScanner);
//			expressionScanner.scan(plainSelect.getOracleHierarchical());
//		}

		if (plainSelect.getOrderByElements() != null) {
			for (OrderByElement orderByElement : plainSelect.getOrderByElements()) {
				ExpressionScanner<OrderByElement> expressionScanner = new ExpressionScanner<>(getDeep() + 1, orderByElement, this, ORDER_BY_EXPRESSION);
				orderByExpresionScanners.add(expressionScanner);
				expressionScanner.scan(orderByElement.getExpression());
			}
		}

		if (plainSelect.getGroupBy() != null) {
			for (Expression expression : plainSelect.getGroupBy().getGroupByExpressions()) {
				ExpressionScanner<PlainSelect> expressionScanner = new ExpressionScanner<>(getDeep() + 1, plainSelect, this, GROUP_BY_EXPRESSION);
				groupByExpresionScanners.add(expressionScanner);
				expressionScanner.scan(expression);
			}
		}

	}

	protected void setUUIDColumnPresent(boolean UUIDColumnPresent) {
		isUUIDColumnPresent = UUIDColumnPresent;
	}

	public boolean isUUIDColumnPresent() {
		return isUUIDColumnPresent;
	}

	@Override
	public SelectBody scannedObject() {
		return selectBody;
	}

	@Override
	public Class<? extends SelectBody> scannedObjectClass() {
		return selectBody.getClass();
	}

	public FromItemScanner getFromItemScanner() {
		return fromItemScanner;
	}

	public ExpressionScanner getWhereExpresionScanner() {
		return whereExpresionScanner;
	}

	public ExpressionScanner getHavingExpresionScanner() {
		return havingExpresionScanner;
	}

	public List<ExpressionScanner> getOrderByExpresionScanners() {
		return orderByExpresionScanners;
	}

	public List<ExpressionScanner> getGroupByExpresionScanners() {
		return groupByExpresionScanners;
	}

	public List<SelectItemScanner<?>> getSelectItemScanners() {
		return selectItemScanners;
	}

	public void setSelectBody(SelectBody selectBody) {
		this.selectBody = selectBody;
	}

	public List<JoinScanner> getJoinScanners() {
		return joinScanners;
	}

	public List<? super Scanner> getSubSelectScanners() {
		return subSelectScanners;
	}

	public void addSubSelectScanners(SubSelectScanner<?> subSelectScanner) {
		this.subSelectScanners.add(subSelectScanner);
	}

	public void addAliasInfoHolder(String key, AliasInfoHolder<?> aliasInfoHolder) {
		this.aliasInfoHolderMap.put(key, aliasInfoHolder);
	}

	public Optional<AliasInfoHolder<?>> fingAliasInfoHolder(String key) {
		return Optional.ofNullable(this.aliasInfoHolderMap.get(key));
	}


	public boolean isPlaneSelect() {
		return isPlaneSelect;
	}

	public boolean isWrapped() {
		return wrapped;
	}

	public void setWrapped(boolean wrapped) {
		this.wrapped = wrapped;
	}
}
