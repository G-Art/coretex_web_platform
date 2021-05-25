package com.coretex.core.activeorm.query.select.data;

import com.coretex.core.general.utils.ItemUtils;
import com.coretex.items.core.MetaTypeItem;
import com.google.common.collect.Sets;
import net.sf.jsqlparser.schema.Table;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

public class TableTransformationData {

	private Table table;
	private String tableName;
	private boolean localizedTable;
	private MetaTypeItem typeItemBind;
	private Set<MetaTypeItem> inheritance;
	private Set<MetaTypeItem> usedTypes;

	private boolean unionTable = false;

	public TableTransformationData(Table table, MetaTypeItem typeItemBind, Set<MetaTypeItem> metaTypesForTable) {
		this.table = table;
		this.localizedTable = false;
		if(CollectionUtils.isNotEmpty(metaTypesForTable)){
			this.usedTypes = Sets.newHashSet(metaTypesForTable);
		}
		if(Objects.nonNull(typeItemBind)){
			this.typeItemBind = typeItemBind;
			this.tableName = typeItemBind.getTableName();
			this.inheritance = ItemUtils.getAllSubtypes(typeItemBind);
			this.usedTypes = Sets.newHashSet(this.inheritance);
			this.usedTypes.add(typeItemBind);

			unionTable = inheritance.stream().anyMatch(ihrt-> !ihrt.getTableName().equals(tableName));
		}else{
			this.tableName = table.getFullyQualifiedName().replaceAll("\"", "");
		}
	}
	public TableTransformationData(Table table, MetaTypeItem typeItemBind, Set<MetaTypeItem> metaTypesForTable, boolean localizedTable) {
		this(table,typeItemBind, metaTypesForTable);
		this.localizedTable = localizedTable;
	}


	public Table getTable() {
		return table;
	}

	public Set<MetaTypeItem> getUsedTypes() {
		return usedTypes;
	}

	public String getTableName() {
		return tableName;
	}

	public boolean isBind(){
		return Objects.nonNull(this.typeItemBind);
	}

	public MetaTypeItem getTypeItemBind() {
		return typeItemBind;
	}

	public boolean hasInheritance(){
		return isNotEmpty(inheritance);
	}

	public boolean isUnionTable() {
		return unionTable;
	}

	public void setUnionTable(boolean unionTable) {
		this.unionTable = unionTable;
	}

	public Optional<Set<MetaTypeItem>> getInheritance(){
		return Optional.ofNullable(inheritance);
	}

	public boolean isLocalizedTable() {
		return localizedTable;
	}
}
