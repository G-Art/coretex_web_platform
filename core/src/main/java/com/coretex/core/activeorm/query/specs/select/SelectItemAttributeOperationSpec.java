package com.coretex.core.activeorm.query.specs.select;

import com.coretex.core.activeorm.exceptions.SearchException;
import com.coretex.core.general.utils.AttributeTypeUtils;
import com.coretex.core.services.items.context.ItemContext;
import com.coretex.items.core.MetaAttributeTypeItem;
import com.coretex.items.core.MetaEnumTypeItem;
import com.coretex.items.core.MetaRelationTypeItem;
import com.coretex.items.core.MetaTypeItem;
import com.coretex.items.core.RegularTypeItem;
import com.coretex.meta.AbstractGenericItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;

import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SelectItemAttributeOperationSpec<R> extends SelectOperationSpec<R> {

	private Logger LOG = LoggerFactory.getLogger(SelectItemAttributeOperationSpec.class);

	private static final String SELECT_REGULAR_FIELD_BY_UUID_TEMPLATE = "select item.%s from \"%s\" as item where item.uuid = :uuid";
	private static final String SELECT_ITEM_FIELD_BY_UUID_TEMPLATE = "select item.* from \"%s\" as item left join \"%s\" as j on (j.%s = item.uuid) where j.uuid = :uuid";
	private static final String SELECT_RELATION_ITEM_FIELD_BY_UUID_TEMPLATE = "select item.* FROM \"%s\" as item LEFT JOIN \"%s\" as rel ON item.uuid = rel.%s where rel.%s = :uuid ORDER BY rel.createDate";
	private static final String SELECT_LOCALIZED_ITEM_FIELD_BY_UUID_TEMPLATE = "select item.localeiso, item.value FROM %s_LOC as item WHERE item.owner = :ownerUuid AND item.attribute = :uuid";

	private MetaAttributeTypeItem metaAttributeTypeItem;

	public SelectItemAttributeOperationSpec(MetaAttributeTypeItem metaAttributeTypeItem, ItemContext ctx) {
		super(createQuery(metaAttributeTypeItem, ctx), createParameters(metaAttributeTypeItem, ctx));
		this.metaAttributeTypeItem = metaAttributeTypeItem;
	}

	public RowMapper<?> createRowMapper() {
		if (AttributeTypeUtils.isRegularTypeAttribute(metaAttributeTypeItem) && !metaAttributeTypeItem.getLocalized()) {
			return (RowMapper<Object>) (rs, rowNum) -> {
				try {
					return getCortexContext().getTypeTranslator(((RegularTypeItem) metaAttributeTypeItem.getAttributeType()).getRegularClass()).read(rs, rowNum);
				} catch (Exception e) {
					if (LOG.isDebugEnabled()) {
						LOG.error(String.format("Can't read column [%s] number %s", metaAttributeTypeItem.getAttributeName(), rowNum), e);
					}
					try {
						return JdbcUtils.getResultSetValue(rs, rowNum);
					} catch (SQLException e1) {
						throw new RuntimeException(e1);
					}
				}
			};
		}
		if (AttributeTypeUtils.isEnumTypeAttribute(metaAttributeTypeItem)) {
			return (RowMapper<Object>) (rs, rowNum) -> {
				try {
					return getCortexContext().findMetaEnumValueTypeItem(((MetaEnumTypeItem) metaAttributeTypeItem.getAttributeType()).getEnumClass(), (UUID) JdbcUtils.getResultSetValue(rs, rowNum));
				} catch (Exception e) {
					if (LOG.isDebugEnabled()) {
						LOG.error(String.format("Can't read column [%s] number %s", metaAttributeTypeItem.getAttributeName(), rowNum), e);
					}
					try {
						return JdbcUtils.getResultSetValue(rs, rowNum);
					} catch (SQLException e1) {
						throw new RuntimeException(e1);
					}
				}
			};
		}
		return null;
	}

	private static Map<String, Object> createParameters(MetaAttributeTypeItem metaAttributeTypeItem, ItemContext ctx) {
		if (metaAttributeTypeItem.getLocalized()) {
			return new HashMap<>() {{
				put(AbstractGenericItem.UUID, metaAttributeTypeItem.getUuid());
				put("ownerUuid", ctx.getUuid());
			}};
		}
		return Collections.singletonMap(AbstractGenericItem.UUID, ctx.getUuid());
	}

	private static String createQuery(MetaAttributeTypeItem metaAttributeTypeItem, ItemContext ctx) {
		if (AttributeTypeUtils.isRelationAttribute(metaAttributeTypeItem)) {
			MetaRelationTypeItem metaRelationTypeItem = (MetaRelationTypeItem) metaAttributeTypeItem.getAttributeType();
			String targetColumnName = getColumnName(metaRelationTypeItem, "target");
			String sourceColumnName = getColumnName(metaRelationTypeItem, "source");
			return String.format(SELECT_RELATION_ITEM_FIELD_BY_UUID_TEMPLATE,
					metaAttributeTypeItem.getSource() ? metaRelationTypeItem.getSourceType().getTypeCode() : metaRelationTypeItem.getTargetType().getTypeCode(),
					metaRelationTypeItem.getTypeCode(),
					metaAttributeTypeItem.getSource() ? targetColumnName : sourceColumnName,
					metaAttributeTypeItem.getSource() ? sourceColumnName : targetColumnName);
		}
		if (metaAttributeTypeItem.getAttributeType() instanceof MetaTypeItem) {
			return String.format(SELECT_ITEM_FIELD_BY_UUID_TEMPLATE,
					((MetaTypeItem) metaAttributeTypeItem.getAttributeType()).getTypeCode(), ctx.getTypeCode(), metaAttributeTypeItem.getAttributeName());
		}

		if (metaAttributeTypeItem.getLocalized()) {
			return String.format(SELECT_LOCALIZED_ITEM_FIELD_BY_UUID_TEMPLATE, metaAttributeTypeItem.getOwner().getTableName());
		}

		return String.format(SELECT_REGULAR_FIELD_BY_UUID_TEMPLATE, metaAttributeTypeItem.getColumnName(), ctx.getTypeCode());

	}

	private static String getColumnName(MetaRelationTypeItem metaRelationTypeItem, String attributeName) {
		return metaRelationTypeItem.getItemAttributes().stream()
				.filter(metaAttr -> metaAttr.getAttributeName().equals(attributeName))
				.findFirst()
				.orElseThrow(() -> new SearchException(String.format("Column for attribute [%s] is not exist", attributeName))).getColumnName();
	}

}
