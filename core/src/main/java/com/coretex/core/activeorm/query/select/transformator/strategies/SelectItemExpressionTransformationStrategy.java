package com.coretex.core.activeorm.query.select.transformator.strategies;

import com.coretex.core.activeorm.query.select.SelectQueryTransformationHelper;
import com.coretex.core.activeorm.query.select.scanners.SelectBodyScanner;
import com.coretex.core.activeorm.query.select.scanners.SelectItemScanner;
import com.coretex.core.activeorm.query.select.transformator.DataInjectionType;
import com.coretex.core.activeorm.query.select.transformator.dip.SelectItemDataInjectionPoint;
import com.coretex.core.activeorm.query.select.transformator.dip.WrapperInjectionPoint;
import com.coretex.items.core.GenericItem;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SelectItemExpressionTransformationStrategy extends AbstractTransformationStrategy<WrapperInjectionPoint<List<SelectItemDataInjectionPoint>>, SelectBody> {

	public SelectItemExpressionTransformationStrategy(Map<DataInjectionType, AbstractTransformationStrategy> transformationStrategies, SelectQueryTransformationHelper transformationHelper) {
		super(transformationStrategies, transformationHelper);
	}

	@Override
	public SelectBody apply(WrapperInjectionPoint<List<SelectItemDataInjectionPoint>> dataInjectionPointWrapper) {

		var optionalSelectBodyScanner = dataInjectionPointWrapper.getSelectBodyScannerOwner();

		if (optionalSelectBodyScanner.isPresent() && optionalSelectBodyScanner.get().isPlaneSelect()) {
			var selectBodyScanner = optionalSelectBodyScanner.get();
			PlainSelect originalPlainSelect = (PlainSelect) selectBodyScanner.scannedObject();
			modifySelectItems(originalPlainSelect, dataInjectionPointWrapper);
			return originalPlainSelect;

		}
		throw new UnsupportedOperationException(String.format("Unable to wrap [%s]", optionalSelectBodyScanner.map(SelectBodyScanner::scannedObject).orElse(null)));
	}

	private void modifySelectItems(PlainSelect originalPlainSelect, WrapperInjectionPoint<List<SelectItemDataInjectionPoint>> wrapperInjectionPoint) {
		var selectBodyScanner = wrapperInjectionPoint.getSelectBodyScannerOwner().get();

		var selectItemsOrigin = originalPlainSelect.getSelectItems();
		var data = wrapperInjectionPoint.getData();
		data.forEach(selectItemDIP -> {
			SelectItemScanner<?> selectItemScanner = selectItemDIP.getScanner();
			if(selectItemScanner.getExpressionScanner().isFunction()){
				var function = (Function) selectItemScanner.getExpressionScanner().getExpression();
				if (function.isAllColumns()) {
					if (selectBodyScanner.isUUIDColumnPresent()) {
						selectItemsOrigin.add(selectItemScanner.getIndex(), null);
					}else{
						SelectExpressionItem selectExpressionItem = (SelectExpressionItem) selectItemsOrigin.get(selectItemScanner.getIndex());
						selectExpressionItem.setExpression(new Column((Table) originalPlainSelect.getFromItem(), GenericItem.UUID));
					}
				}
			}else {
				if (selectItemScanner.getExpressionScanner().isColumn()){
					getTransformationHelper().adjustColumn(selectItemScanner.getExpressionScanner(), selectBodyScanner);
				}
			}
		});

		selectItemsOrigin.removeIf(Objects::isNull);

	}

}
