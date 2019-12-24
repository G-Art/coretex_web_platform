package com.coretex.commerce.admin.converters;

import com.coretex.commerce.data.GenericItemData;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;

@Component
public class StringToGenericItemDataConverter implements ConditionalGenericConverter {
	private static final Logger LOGGER = LoggerFactory.getLogger(StringToGenericItemDataConverter.class);

	public StringToGenericItemDataConverter() {
	}

	@Override
	public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
		var isString = String.class.isAssignableFrom(sourceType.getObjectType());
		var isGenericItem = GenericItemData.class.isAssignableFrom(targetType.getObjectType());
		return isString && isGenericItem;
	}

	@Override
	public Set<ConvertiblePair> getConvertibleTypes() {
		return Collections.singleton(new ConvertiblePair(String.class, GenericItemData.class));
	}

	@Override
	public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
		if(StringUtils.isNotBlank((CharSequence) source)){
			try {
				GenericItemData instance = (GenericItemData) ReflectionUtils.accessibleConstructor(targetType.getType()).newInstance();
				instance.setUuid(UUID.fromString((String) source));
				return instance;
			} catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
				LOGGER.error("Instance creation error", e);
			}
		}
		return null;
	}
}