package com.coretex.core.activeorm.query.specs;

import com.coretex.core.activeorm.query.QueryTransformationProcessor;
import com.coretex.core.activeorm.query.operations.LocalizedDataSaveOperation;
import com.coretex.core.activeorm.query.operations.ModificationOperation;
import com.coretex.core.general.utils.AttributeTypeUtils;
import com.coretex.items.core.MetaAttributeTypeItem;
import com.google.common.collect.Maps;
import net.sf.jsqlparser.statement.Statement;

import java.util.*;
import java.util.stream.Collectors;

public class LocalizedDataSaveOperationSpec extends ModificationOperationSpec<Statement, LocalizedDataSaveOperation> {

	private final static String INSERT_LOCALIZED_DATA_QUERY = "insert into %s_LOC (owner, attribute, localeiso, value) values (:owner, :attribute, :localeiso, :value)";
	private final static String UPDATE_LOCALIZED_DATA_QUERY = "update %s_LOC set value = :value where owner = :owner and attribute = :attribute and localeiso = :localeiso";

	private MetaAttributeTypeItem attributeTypeItem;

	private LocalizedAttributeSaveFetcher fetcher;

	private String insertQuery;
	private String updateQuery;

	public LocalizedDataSaveOperationSpec(ModificationOperation<? extends Statement, ? extends ModificationOperationSpec> initiator, MetaAttributeTypeItem attributeTypeItem) {
		super(initiator.getOperationSpec().getItem());
		setNativeQuery(false);
		this.attributeTypeItem = attributeTypeItem;
		this.setQuerySupplier(this::buildQuery);
	}


	private String buildQuery() {
		if(AttributeTypeUtils.isRelationAttribute(attributeTypeItem)){
			throw new IllegalArgumentException(String.format("Relation attribute cant be localized [name: %s] [owner: %s]", attributeTypeItem.getAttributeName(), attributeTypeItem.getOwner().getTypeCode()));
		}

		fetcher = new LocalizedAttributeSaveFetcher(
				getItem().getItemContext().getOriginLocalizedValues(attributeTypeItem.getAttributeName()),
				getItem().getItemContext().getLocalizedValues(attributeTypeItem.getAttributeName())
		);

		insertQuery = String.format(INSERT_LOCALIZED_DATA_QUERY, attributeTypeItem.getOwner().getTableName());
		updateQuery = String.format(UPDATE_LOCALIZED_DATA_QUERY, attributeTypeItem.getOwner().getTableName());

		return "SELECT 'Fake query' AS fake";
	}

	@Override
	public void flush() {
		//ignored
	}

	@Override
	public boolean constraintsApplicable() {
		return false;
	}

	public String getInsertQuery() {
		return insertQuery;
	}

	public String getUpdateQuery() {
		return updateQuery;
	}

	public LocalizedAttributeSaveFetcher getFetcher() {
		return fetcher;
	}

	public MetaAttributeTypeItem getAttributeTypeItem() {
		return attributeTypeItem;
	}

	@Override
	public LocalizedDataSaveOperation createOperation(QueryTransformationProcessor processor) {
		return new LocalizedDataSaveOperation(this);
	}


	public final class LocalizedAttributeSaveFetcher {

		private Map<Locale, Object> update;
		private Map<Locale, Object> insert;

		private Map<Locale, Object> originLocalizedValues;
		private Map<Locale, Object> localizedValues;

		private LocalizedAttributeSaveFetcher(Map<Locale, Object> originLocalizedValues, Map<Locale, Object> localizedValues) {
			this.originLocalizedValues = Optional.ofNullable(originLocalizedValues).orElseGet(Maps::newHashMap);
			this.localizedValues = Optional.ofNullable(localizedValues).orElseGet(Maps::newHashMap);
		}

		public boolean hasValuesForUpdate(){
			if(update == null){
				update = localizedValues.entrySet()
						.stream()
						.filter(entry -> {
							if(originLocalizedValues.containsKey(entry.getKey())){
								return !originLocalizedValues.get(entry.getKey()).equals(entry.getKey());
							}
							return false;
						}).collect(
								Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)
						);

			}

			return !update.isEmpty();
		}

		public boolean hasValuesForInsert(){
			if(insert == null){
				insert = new HashMap<>();
				insert = localizedValues.entrySet()
						.stream()
						.filter(entry -> !originLocalizedValues.containsKey(entry.getKey())).collect(
								Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)
						);
			}

			return !insert.isEmpty();
		}


		public Map<Locale, Object> getUpdateValues(){
			return update;
		}

		public Map<Locale, Object> getInsertValues(){
			return insert;
		}

	}

}
