package com.coretex.core.activeorm.query.specs;

import com.coretex.core.activeorm.query.QueryTransformationProcessor;
import com.coretex.core.activeorm.query.operations.RemoveOperation;
import com.coretex.core.activeorm.query.operations.dataholders.RemoveValueDataHolder;
import com.coretex.items.core.GenericItem;
import com.coretex.items.core.MetaAttributeTypeItem;
import com.coretex.items.core.MetaRelationTypeItem;
import com.coretex.meta.AbstractGenericItem;
import com.google.common.collect.Maps;
import net.sf.jsqlparser.statement.delete.Delete;

import java.util.Map;
import java.util.stream.Collectors;

import static com.coretex.core.general.utils.ItemUtils.getTypeCode;

public class RemoveOperationSpec extends ModificationOperationSpec<Delete, RemoveOperation> {

	protected final static String DELETE_ITEM_QUERY = "delete from %s where %s";

	private Map<String, RemoveValueDataHolder> valueDatas = Maps.newHashMap();


	public RemoveOperationSpec(GenericItem item) {
		super(item);
		setQuerySupplier(this::createRemoveQuery);
	}

	public RemoveOperationSpec(GenericItem item, boolean cascade) {
		super(item, cascade);
		setQuerySupplier(this::createRemoveQuery);
	}

	public RemoveOperationSpec(GenericItem item, boolean cascade, boolean transactional) {
		super(item, cascade, transactional);
		setQuerySupplier(this::createRemoveQuery);
	}

	private String createRemoveQuery() {

		if(getItem().getMetaType() instanceof MetaRelationTypeItem){
			MetaAttributeTypeItem sourceAttribute = getMetaTypeProvider().findAttribute(getTypeCode(getItem()), "source");
			MetaAttributeTypeItem targetAttribute = getMetaTypeProvider().findAttribute(getTypeCode(getItem()), "target");

			valueDatas.put(sourceAttribute.getColumnName(), new RemoveValueDataHolder(sourceAttribute, this));
			valueDatas.put(targetAttribute.getColumnName(), new RemoveValueDataHolder(targetAttribute, this));
		}else{
			valueDatas.put(AbstractGenericItem.UUID, new RemoveValueDataHolder(getMetaTypeProvider().findAttribute(getTypeCode(getItem()), AbstractGenericItem.UUID), this));
		}
		return String.format(DELETE_ITEM_QUERY, getItem().getMetaType().getTableName(),
				valueDatas.keySet().stream().map(key -> String.format("%s = :%s", key, key)).collect(Collectors.joining(" AND ")));
	}

	public Map<String, RemoveValueDataHolder> getValueDatas() {
		return valueDatas;
	}

	@Override
	public RemoveOperation createOperation(QueryTransformationProcessor<Delete> processor) {
		return new RemoveOperation(this);
	}

}
