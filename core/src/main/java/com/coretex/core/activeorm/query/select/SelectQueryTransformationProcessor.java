package com.coretex.core.activeorm.query.select;

import com.coretex.core.activeorm.query.QueryStatementContext;
import com.coretex.core.activeorm.query.QueryTransformationProcessor;
import com.coretex.core.activeorm.query.select.scanners.SelectBodyScanner;
import com.coretex.core.activeorm.query.select.transformator.DataInjectionType;
import com.coretex.core.activeorm.query.select.transformator.dip.SelectBodyDataInjectionPoint;
import com.coretex.core.activeorm.query.select.transformator.strategies.AbstractTransformationStrategy;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;


public class SelectQueryTransformationProcessor implements QueryTransformationProcessor<QueryStatementContext<Select>> {

	private Logger LOG = LoggerFactory.getLogger(SelectQueryTransformationProcessor.class);

	private SelectQueryTransformationHelper helper;

	private Map<DataInjectionType, AbstractTransformationStrategy> transformationStrategies;


	public SelectQueryTransformationProcessor(Map<DataInjectionType, AbstractTransformationStrategy> transformationStrategies,
	                                          SelectQueryTransformationHelper selectQueryTransformationHelper) {
		this.transformationStrategies = transformationStrategies;
		this.helper = selectQueryTransformationHelper;
	}

	@Override
	public void transform(QueryStatementContext<Select> statementContext) {

		SelectBodyScanner<Select> selectBodyScanner = new SelectBodyScanner<>(0, statementContext.getStatement());

		selectBodyScanner.scan(statementContext.getStatement().getSelectBody());

		SelectBodyDataInjectionPoint selectBodyDataInjectionPoint = helper.createDateInjectionPoint(selectBodyScanner, statementContext);
		SelectBody result = (SelectBody) transformationStrategies.get(selectBodyDataInjectionPoint.getDataInjectionType())
				.apply(selectBodyDataInjectionPoint);
		statementContext.getStatement().setSelectBody(result);

	}


}
