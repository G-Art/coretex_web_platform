package com.coretex.core.services.bootstrap.dialect.postgres;

import com.coretex.core.activeorm.query.operations.contexts.UpdateOperationConfigContext;
import com.coretex.core.activeorm.query.operations.dataholders.AbstractValueDataHolder;
import com.coretex.core.activeorm.query.operations.dataholders.UpdateValueDataHolder;
import com.coretex.core.services.bootstrap.dialect.AbstractUpdateQueryBuilder;
import com.coretex.meta.AbstractGenericItem;

import java.util.Collection;
import java.util.stream.Collectors;

import static com.coretex.core.general.utils.ItemUtils.getTypeCode;

public class PostgresUpdateQueryBuilder extends AbstractUpdateQueryBuilder<UpdateOperationConfigContext> {

	@Override
	public String createQuery(UpdateOperationConfigContext context) {
		var operationSpec = context.getOperationSpec();
		var item = operationSpec.getItem();
		var metaTypeProvider = operationSpec.getMetaTypeProvider();
		Collection<? extends AbstractValueDataHolder<?>> entries = context.getOperationSpec().getValueDataHolders().values();
		operationSpec.addValueDataHolder(new UpdateValueDataHolder(metaTypeProvider.findAttribute(getTypeCode(item), AbstractGenericItem.UUID), operationSpec));
		return String.format(DEFAULT_UPDATE_ITEM_QUERY, context.getOperationSpec().getItem().getMetaType().getTableName(),
				entries.stream()
						.map(e -> String.format("%s = :%s", e.columnName(), e.getAttributeName()))
						.collect(Collectors.joining(",")));

	}
}
