package com.coretex.core.general.utils;

import com.coretex.items.core.*;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.equalsAny;

public class AttributeTypeUtils {


    public static Class<?> getItemClass(MetaAttributeTypeItem attribute) {
        if (RegularTypeItem.ITEM_TYPE.equals(attribute.getAttributeTypeCode())) {
            return ((RegularTypeItem) attribute.getAttributeType()).getRegularClass();
        }
        if (MetaTypeItem.ITEM_TYPE.equals(attribute.getAttributeTypeCode())) {
            return ((MetaTypeItem) attribute.getAttributeType()).getItemClass();
        }
        if (MetaEnumTypeItem.ITEM_TYPE.equals(attribute.getAttributeTypeCode())) {
            return ((MetaEnumTypeItem) attribute.getAttributeType()).getEnumClass();
        }
        if (MetaRelationTypeItem.ITEM_TYPE.equals(attribute.getAttributeTypeCode())) {
            return ((MetaRelationTypeItem) attribute.getAttributeType()).getItemClass();
        }
        return null;
    }

    public static String getItemTypeCode(MetaAttributeTypeItem attribute) {
        if (RegularTypeItem.ITEM_TYPE.equals(attribute.getAttributeTypeCode())) {
            return ((RegularTypeItem) attribute.getAttributeType()).getRegularItemCode();
        }
        if (MetaTypeItem.ITEM_TYPE.equals(attribute.getAttributeTypeCode())) {
            return ((MetaTypeItem) attribute.getAttributeType()).getTypeCode();
        }
        if (MetaEnumTypeItem.ITEM_TYPE.equals(attribute.getAttributeTypeCode())) {
            return MetaEnumTypeItem.ITEM_TYPE;
        }
        if (isRelationAttribute(attribute)) {
            return ((MetaRelationTypeItem) attribute.getAttributeType()).getTypeCode();
        }
        return null;
    }

    public static Optional<MetaAttributeTypeItem> getTargetAttribute(MetaAttributeTypeItem attribute) {
        if (isRelationAttribute(attribute)) {
            MetaRelationTypeItem relation = (MetaRelationTypeItem) attribute.getAttributeType();
            return Optional.of(attribute.getSource() ? relation.getTargetAttribute() : relation.getSourceAttribute());
        }
        return Optional.empty();
    }

    public static Optional<MetaAttributeTypeItem> getJoinedRelationAttribute(MetaAttributeTypeItem attribute) {
        if (MetaRelationTypeItem.ITEM_TYPE.equals(attribute.getOwner().getMetaType().getTypeCode()) &&
                equalsAny(attribute.getAttributeName(), "source", "target")) {
            MetaRelationTypeItem relation = (MetaRelationTypeItem) attribute.getOwner();
            return Optional.of("source".equals(attribute.getAttributeName()) ? relation.getSourceAttribute() : relation.getTargetAttribute());
        }
        if (MetaRelationTypeItem.ITEM_TYPE.equals(attribute.getAttributeType().getMetaType().getTypeCode())) {
            MetaRelationTypeItem relation = (MetaRelationTypeItem) attribute.getAttributeType();
            return Optional.of(attribute.getSource() ? relation.getTargetAttribute() : relation.getSourceAttribute());
        }
        return Optional.empty();
    }


    public static boolean isCollection(MetaAttributeTypeItem attribute) {
        return nonNull(attribute.getContainerType()) && Collection.class.isAssignableFrom(attribute.getContainerType());
    }

    public static boolean isRelationAttribute(MetaAttributeTypeItem attribute) {
        return MetaRelationTypeItem.ITEM_TYPE.equals(attribute.getAttributeTypeCode());
    }

    public static boolean isRegularTypeAttribute(MetaAttributeTypeItem attributeTypeItem) {
        return RegularTypeItem.ITEM_TYPE.equals(attributeTypeItem.getAttributeTypeCode());
    }

    public static boolean isEnumTypeAttribute(MetaAttributeTypeItem attributeTypeItem){
        return MetaEnumTypeItem.ITEM_TYPE.equals(attributeTypeItem.getAttributeTypeCode());
    }
}
