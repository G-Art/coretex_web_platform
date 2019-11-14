package com.coretex.converter;

import com.coretex.core.services.bootstrap.impl.CortexContext;
import com.coretex.items.core.*;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.coretex.core.general.utils.AttributeTypeUtils.isCollection;
import static com.coretex.core.general.utils.ItemUtils.getTypeCode;
import static java.util.Objects.requireNonNull;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

public class ItemConverter {

	private Logger LOG = LoggerFactory.getLogger(ItemConverter.class);

	@Autowired
	private CortexContext cortexContext;

	public Map convertToMap(GenericItem item) {
		Map<String, Object> result = Maps.newHashMap();
		Map<String, MetaAttributeTypeItem> attributeTypeItemMap = cortexContext.getAllAttributes(getTypeCode(item));

		attributeTypeItemMap.forEach((key, attr) -> {

			try{
				switch (requireNonNull(attr.getAttributeTypeCode())){
					case MetaRelationTypeItem.ITEM_TYPE :
					case MetaEnumTypeItem.ITEM_TYPE: {
						if(isCollection(attr)){
							Collection values = item.getItemContext().getValue(key);
							result.put(key, createRelationValuesView(attr, values));
							break;
						}
					}

					default: {
						Object value = attr.getLocalized() ? item.getItemContext().getLocalizedValues(key) : item.getItemContext().getValue(key);
						result.put(key, genValue(attr, value));
					}

				}
			}catch (Exception ex){
				LOG.error(String.format("Error for [%s.%s] ", getTypeCode(item), key), ex);
				throw  ex;
			}

		});
		return result;
	}

	private String genValue(MetaAttributeTypeItem attributeTypeItem, Object value) {
		if (attributeTypeItem.getAttributeType() instanceof RegularTypeItem || attributeTypeItem.getAttributeType() instanceof MetaEnumTypeItem) {
			return String.valueOf(value);
		} else {
			return Objects.nonNull(value) ? String.format("%s[%s]", getTypeCode(((GenericItem) value)),((GenericItem) value).getUuid()) : String.valueOf(value);
		}
	}

	private String createRelationValuesView(MetaAttributeTypeItem attr, Collection values) {
		if(isNotEmpty(values)){
			return String.format("%s(%s)", values instanceof List ? "List" : "Set", String.join(",", (Iterable<? extends CharSequence>) values.stream().map(val -> genValue(attr, val)).collect(Collectors.toList())));
		}
		return null;
	}
}
