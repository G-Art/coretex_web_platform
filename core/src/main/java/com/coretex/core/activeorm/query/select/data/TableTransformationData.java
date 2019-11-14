package com.coretex.core.activeorm.query.select.data;

import com.coretex.items.core.MetaTypeItem;
import net.sf.jsqlparser.schema.Table;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

public class TableTransformationData {

	private Table table;
	private String tableName;
	private MetaTypeItem typeItemBind;
	private Set<MetaTypeItem> inheritance;

	public TableTransformationData(Table table, MetaTypeItem typeItemBind) {
		this.table = table;
		if(typeItemBind != null){
			this.typeItemBind = typeItemBind;
			this.tableName = typeItemBind.getTableName();
			this.inheritance = typeItemBind.getSubtypes();
		}
	}

	public Table getTable() {
		return table;
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

	public Optional<Set<MetaTypeItem>> getInheritance(){
		return Optional.ofNullable(inheritance);
	}

}
