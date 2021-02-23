package com.coretex.core.activeorm.query.specs;

import com.coretex.core.activeorm.query.operations.contexts.AbstractOperationConfigContext;
import com.coretex.core.activeorm.query.operations.contexts.LocalizedDataSaveOperationConfigContext;
import com.coretex.core.general.utils.AttributeTypeUtils;
import com.coretex.items.core.MetaAttributeTypeItem;
import com.google.common.collect.Maps;
import net.sf.jsqlparser.statement.Statement;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class LocalizedDataSaveOperationSpec extends ModificationOperationSpec<Statement, LocalizedDataSaveOperationSpec, LocalizedDataSaveOperationConfigContext> {

	private final static String INSERT_LOCALIZED_DATA_QUERY = "insert into %s_LOC (owner, attribute, localeiso, value) values (:owner, :attribute, :localeiso, :value)";
	private final static String UPDATE_LOCALIZED_DATA_QUERY = "update %s_LOC set value = :value where owner = :owner and attribute = :attribute and localeiso = :localeiso";

	private MetaAttributeTypeItem attributeTypeItem;

	private LocalizedAttributeSaveFetcher fetcher;

	private String insertQuery;
	private String updateQuery;

	public LocalizedDataSaveOperationSpec(AbstractOperationConfigContext<?, ? extends ModificationOperationSpec<?, ?, ?>, ?> initiator, MetaAttributeTypeItem attributeTypeItem) {
		super(initiator.getOperationSpec().getItem());
		setNativeQuery(false);
		this.attributeTypeItem = attributeTypeItem;
		this.setQuerySupplier(this::buildQuery);
	}


	private String buildQuery() {
		if (AttributeTypeUtils.isRelationAttribute(attributeTypeItem)) {
			throw new IllegalArgumentException(String.format("Relation attribute cant be localized [name: %s] [owner: %s]", attributeTypeItem.getAttributeName(), attributeTypeItem.getOwner().getTypeCode()));
		}

		fetcher = new LocalizedAttributeSaveFetcher(
				getItem().getItemContext().getOriginLocalizedValues(attributeTypeItem.getAttributeName()),
				getItem().getItemContext().getLocalizedValues(attributeTypeItem.getAttributeName())
		);

		insertQuery = String.format(INSERT_LOCALIZED_DATA_QUERY, attributeTypeItem.getOwner().getTableName());
		updateQuery = String.format(UPDATE_LOCALIZED_DATA_QUERY, attributeTypeItem.getOwner().getTableName());

		return "select 'Fake query' AS fake";
	}

	@Override
	public void flush() {
		//ignored
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
	public LocalizedDataSaveOperationConfigContext createOperationContext() {
		return new LocalizedDataSaveOperationConfigContext(this);
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

		public boolean hasValuesForUpdate() {
			if (update == null) {
				update = localizedValues.entrySet()
						.stream()
						.filter(entry -> {
							if (originLocalizedValues.containsKey(entry.getKey())) {
								return !originLocalizedValues.get(entry.getKey()).equals(entry.getValue());
							}
							return false;
						}).collect(
								Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)
						);

			}

			return !update.isEmpty();
		}

		public boolean hasValuesForInsert() {
			if (insert == null) {
				insert = new HashMap<>();
				insert = localizedValues.entrySet()
						.stream()
						.filter(entry -> !originLocalizedValues.containsKey(entry.getKey()))
						.filter(entry -> entry.getValue() instanceof String ? StringUtils.isNotBlank((CharSequence) entry.getValue()) : Objects.nonNull(entry.getValue()))
						.collect(
								Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)
						);
			}

			return !insert.isEmpty();
		}


		public Map<Locale, Object> getUpdateValues() {
			return update;
		}

		public Map<Locale, Object> getInsertValues() {
			return insert;
		}

	}

}
