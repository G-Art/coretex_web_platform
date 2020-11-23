package com.coretex.core.activeorm.query.specs;

import com.coretex.core.activeorm.query.operations.contexts.AbstractOperationConfigContext;
import com.coretex.core.activeorm.query.operations.contexts.LocalizedDataRemoveOperationConfigContext;
import com.coretex.core.general.utils.AttributeTypeUtils;
import com.coretex.items.core.MetaAttributeTypeItem;
import net.sf.jsqlparser.statement.delete.Delete;

import java.util.HashMap;
import java.util.Map;

public class LocalizedDataRemoveOperationSpec extends ModificationOperationSpec<Delete, LocalizedDataRemoveOperationSpec, LocalizedDataRemoveOperationConfigContext> {

	private final static String DELETE_LOCALIZED_DATA_QUERY = "delete from %s_LOC where owner = :owner and attribute = :attribute";

	private MetaAttributeTypeItem attributeTypeItem;
	private Map<String, Object> params = new HashMap<>();


	public LocalizedDataRemoveOperationSpec(AbstractOperationConfigContext<?, ? extends ModificationOperationSpec<?,?,?> ,?> initiator, MetaAttributeTypeItem attributeTypeItem) {
		super(initiator.getOperationSpec().getItem());
		setNativeQuery(false);
		this.attributeTypeItem = attributeTypeItem;
		this.setQuerySupplier(this::buildQuery);
	}

	private String buildQuery() {
		if(AttributeTypeUtils.isRelationAttribute(attributeTypeItem)){
			throw new IllegalArgumentException(String.format("Relation attribute cant be localized [name: %s] [owner: %s]", attributeTypeItem.getAttributeName(), attributeTypeItem.getOwner().getTypeCode()));
		}
		params.put("owner", getItem().getUuid());
		params.put("attribute", getAttributeTypeItem().getUuid());

		return String.format(DELETE_LOCALIZED_DATA_QUERY, getItem().getMetaType().getTableName());
	}

	public Map<String, Object> getParams() {
		return params;
	}

	@Override
	public void flush() {
		//ignored
	}

	public MetaAttributeTypeItem getAttributeTypeItem() {
		return attributeTypeItem;
	}

	@Override
	public LocalizedDataRemoveOperationConfigContext createOperationContext() {
		return new LocalizedDataRemoveOperationConfigContext(this);
	}
}
