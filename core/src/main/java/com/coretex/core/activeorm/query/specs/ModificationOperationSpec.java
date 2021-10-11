package com.coretex.core.activeorm.query.specs;

import com.coretex.core.activeorm.query.operations.dataholders.AbstractValueDataHolder;
import com.coretex.core.services.bootstrap.impl.MetaTypeProvider;
import com.coretex.items.core.GenericItem;
import com.coretex.items.core.MetaAttributeTypeItem;
import com.coretex.items.core.MetaRelationTypeItem;
import com.coretex.server.ApplicationContextProvider;
import com.google.common.collect.Maps;
import net.sf.jsqlparser.statement.Statement;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class ModificationOperationSpec<
		S extends Statement>
		extends SqlOperationSpec<S> {

	private MetaTypeProvider metaTypeProvider;

	private GenericItem item;

	private boolean cascadeEnabled = true;
	private boolean transactionEnabled = true;
	private List<MetaAttributeTypeItem> localizedFields;
	private Map<String, MetaAttributeTypeItem> allAttributes;
	private List<MetaAttributeTypeItem> relationAttributes;
	private final Map<String, AbstractValueDataHolder<?>> valueDataHolders = Maps.newHashMap();

	public ModificationOperationSpec(GenericItem item) {
		metaTypeProvider = ApplicationContextProvider.getBean(MetaTypeProvider.class);
		this.item = item;
		setNativeQuery(true);
	}


	public ModificationOperationSpec(GenericItem item, boolean cascade) {
		this(item);
		this.cascadeEnabled = cascade;
	}

	public ModificationOperationSpec(GenericItem item, boolean cascade, boolean transactional) {
		this(item, cascade);
		this.transactionEnabled = transactional;
	}

	public GenericItem getItem() {
		return item;
	}

	public MetaTypeProvider getMetaTypeProvider() {
		return metaTypeProvider;
	}

	public boolean isCascadeEnabled() {
		return cascadeEnabled;
	}

	public boolean isTransactionEnabled() {
		return transactionEnabled;
	}

	public boolean getHasLocalizedFields() {
		return CollectionUtils.isNotEmpty(getLocalizedFields());
	}

	public boolean getHasRelationAttributes() {
		return CollectionUtils.isNotEmpty(getRelationAttributes());
	}

	public List<MetaAttributeTypeItem> getLocalizedFields() {
		if (localizedFields == null) {
			localizedFields = getAllAttributes().values()
					.stream()
					.filter(MetaAttributeTypeItem::getLocalized)
					.collect(Collectors.toList());
		}
		return localizedFields;
	}

	public List<MetaAttributeTypeItem> getRelationAttributes() {
		if (relationAttributes == null) {
			relationAttributes = getAllAttributes().values()
					.stream()
					.filter(attr -> attr.getAttributeType() instanceof MetaRelationTypeItem)
					.collect(Collectors.toList());
		}
		return relationAttributes;
	}

	public Map<String, MetaAttributeTypeItem> getAllAttributes() {
		if (allAttributes == null) {
			allAttributes = getMetaTypeProvider().getAllAttributes(getItem().getMetaType());
		}
		return allAttributes;
	}

	public Map<String, AbstractValueDataHolder<?>> getValueDataHolders() {
		return valueDataHolders;
	}

	public void flush() {
		item.getItemContext().flush();
	}

	public void addValueDataHolders(Collection<? extends AbstractValueDataHolder<?>> values) {
		valueDataHolders.putAll(
				values.stream()
						.collect(
								Collectors.toMap(AbstractValueDataHolder::getAttributeName, Function.identity())
						)
		);
	}

	public void addValueDataHolder(AbstractValueDataHolder<?> dataHolder) {
		valueDataHolders.put(dataHolder.getAttributeName(), dataHolder);
	}
}
