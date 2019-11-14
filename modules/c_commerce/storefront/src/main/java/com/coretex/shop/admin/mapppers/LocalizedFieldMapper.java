package com.coretex.shop.admin.mapppers;

import com.coretex.items.core.GenericItem;
import com.coretex.items.core.MetaAttributeTypeItem;
import com.coretex.items.core.RegularTypeItem;
import org.apache.commons.lang3.LocaleUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.MapMapping;
import org.mapstruct.MappingTarget;
import org.springframework.util.ReflectionUtils;

import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public interface LocalizedFieldMapper<I extends GenericItem, T> {

	@AfterMapping
	default void defineLocalizedField(I source, @MappingTarget T target) {
		source.getMetaType().getItemAttributes().stream()
				.filter(MetaAttributeTypeItem::getLocalized)
				.filter(metaAttributeTypeItem -> metaAttributeTypeItem.getAttributeType() instanceof RegularTypeItem)
				.forEach(metaAttributeTypeItem -> {
					var field = ReflectionUtils.findField(target.getClass(), metaAttributeTypeItem.getAttributeName());
					var localizedValue = source.getItemContext().getLocalizedValues(metaAttributeTypeItem.getAttributeName());
					if (Objects.nonNull(field) && Objects.nonNull(localizedValue)) {
						ReflectionUtils.makeAccessible(field);
						ReflectionUtils.setField(field, target, localizedFieldMapping((Map<Locale, ?>) localizedValue));
					}
				});
	}

	@AfterMapping
	default void defineLocalizedFieldReverse(T source, @MappingTarget I target) {
		target.getMetaType().getItemAttributes().stream()
				.filter(MetaAttributeTypeItem::getLocalized)
				.filter(metaAttributeTypeItem -> metaAttributeTypeItem.getAttributeType() instanceof RegularTypeItem)
				.forEach(metaAttributeTypeItem -> {
					var field = ReflectionUtils.findField(source.getClass(), metaAttributeTypeItem.getAttributeName());
					if (Objects.nonNull(field)) {
						ReflectionUtils.makeAccessible(field);
						var fieldLocalizedValue = ReflectionUtils.getField(field, source);
						if(Objects.nonNull(fieldLocalizedValue)){
							var localizedValue = localizedFieldMappingReverse((Map<String, ?>) fieldLocalizedValue);

							localizedValue.forEach((key, val) -> {
								target.getItemContext().setLocalizedValue(metaAttributeTypeItem.getAttributeName(), val, key);
							});
						}
					}
				});
	}

	@MapMapping
	default <O> Map<String, O> localizedFieldMapping(Map<Locale, O> source) {
		return source.entrySet().stream().collect(Collectors.toMap(e -> e.getKey().toString(), Map.Entry::getValue));
	}

	@MapMapping
	default <O> Map<Locale, O> localizedFieldMappingReverse(Map<String, O> source) {
		return source.entrySet().stream().collect(Collectors.toMap(e -> LocaleUtils.toLocale(e.getKey()), Map.Entry::getValue));
	}
}
