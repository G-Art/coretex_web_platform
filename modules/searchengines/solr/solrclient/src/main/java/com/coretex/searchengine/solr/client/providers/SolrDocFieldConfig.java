package com.coretex.searchengine.solr.client.providers;

import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

public class SolrDocFieldConfig {

	public static final String SOLR_FIELD_TYPE_BOOLEAN = "boolean";
	public static final String SOLR_FIELD_TYPE_INT = "int";
	public static final String SOLR_FIELD_TYPE_LONG = "long";
	public static final String SOLR_FIELD_TYPE_FLOAT = "float";
	public static final String SOLR_FIELD_TYPE_DOUBLE = "double";
	public static final String SOLR_FIELD_TYPE_DATE = "date";
	public static final String SOLR_FIELD_TYPE_STRING = "string";
	public static final String SOLR_FIELD_TYPE_UUID = "uuid";
	public static final String SOLR_FIELD_TYPE_TEXT = "text";

	private String name;
	private String type = SOLR_FIELD_TYPE_STRING;
	private boolean localized = false;
	private boolean multiValue = false;
	private boolean multiValueSelect = false;
	private boolean localeBeforeType = false;
	private boolean facet = false;
	private int boost = 0;
	private boolean groupedBy = false;

	public SolrDocFieldConfig(String name) {
		this.name = name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isLocalized() {
		return localized;
	}

	public void setLocalized(boolean localized) {
		this.localized = localized;
	}

	public boolean isMultiValue() {
		return multiValue;
	}

	public void setMultiValue(boolean multiValue) {
		this.multiValue = multiValue;
	}

	public int getBoost() {
		return boost;
	}

	public void setBoost(int boost) {
		this.boost = boost;
	}

	public boolean isGroupedBy() {
		return groupedBy;
	}

	public void setGroupedBy(boolean groupedBy) {
		this.groupedBy = groupedBy;
	}

	public boolean isLocaleBeforeType() {
		return localeBeforeType;
	}

	public void setLocaleBeforeType(boolean localeBeforeType) {
		this.localeBeforeType = localeBeforeType;
	}

	public String createFullFieldName(Locale locale) {
		if(isLocaleBeforeType()){
			return getName() + String.format("_%s_%s", locale.toLanguageTag(), getType()) + (isMultiValue() ? "_mv" : StringUtils.EMPTY);
		}
		return getName() + String.format("_%s_%s", getType(), locale.toLanguageTag()) + (isMultiValue() ? "_mv" : StringUtils.EMPTY);
	}

	public String createFullFieldName() {
		return getName() + String.format("_%s", getType()) + (isMultiValue() ? "_mv" : StringUtils.EMPTY);
	}

	public boolean isFacet() {
		return facet;
	}

	public void setFacet(boolean facet) {
		this.facet = facet;
	}

	public boolean isMultiValueSelect() {
		return multiValueSelect;
	}

	public void setMultiValueSelect(boolean multiValueSelect) {
		this.multiValueSelect = multiValueSelect;
	}
}
