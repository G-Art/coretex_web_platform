package com.coretex.searchengine.solr.client.providers;

import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

public class SolrDocFieldConfig {

	public enum WildcardQueryType {
		PREFIX,POSTFIX,PREFIX_AND_POSTFIX
	}

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
	private boolean groupedBy = false;

	private boolean query=false;
	private double queryBoost = 1.0d;
	private boolean phraseQuery=false;
	private double phraseQueryBoost = 1.0d;
	private double phraseQuerySlop = 20.0d;
	private boolean fuzzyQuery=false;
	private double fuzzyQueryBoost = 1.0d;
	private int fuzzyQueryFuzziness = 1;
	private boolean wildcardQuery=false;
	private double wildcardQueryBoost = 1.0d;
	private WildcardQueryType wildcardQueryType = WildcardQueryType.POSTFIX;

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

	public boolean isInSearch(){
		return query || phraseQuery || fuzzyQuery || wildcardQuery;
	}

	public boolean isQuery() {
		return query;
	}

	public void setQuery(boolean query) {
		this.query = query;
	}

	public boolean isPhraseQuery() {
		return phraseQuery;
	}

	public void setPhraseQuery(boolean phraseQuery) {
		this.phraseQuery = phraseQuery;
	}

	public boolean isFuzzyQuery() {
		return fuzzyQuery;
	}

	public void setFuzzyQuery(boolean fuzzyQuery) {
		this.fuzzyQuery = fuzzyQuery;
	}

	public boolean isWildcardQuery() {
		return wildcardQuery;
	}

	public void setWildcardQuery(boolean wildcardQuery) {
		this.wildcardQuery = wildcardQuery;
	}

	public double getQueryBoost() {
		return queryBoost;
	}

	public void setQueryBoost(double queryBoost) {
		this.queryBoost = queryBoost;
	}

	public double getPhraseQueryBoost() {
		return phraseQueryBoost;
	}

	public void setPhraseQueryBoost(double phraseQueryBoost) {
		this.phraseQueryBoost = phraseQueryBoost;
	}

	public double getPhraseQuerySlop() {
		return phraseQuerySlop;
	}

	public void setPhraseQuerySlop(double phraseQuerySlop) {
		this.phraseQuerySlop = phraseQuerySlop;
	}

	public double getFuzzyQueryBoost() {
		return fuzzyQueryBoost;
	}

	public void setFuzzyQueryBoost(double fuzzyQueryBoost) {
		this.fuzzyQueryBoost = fuzzyQueryBoost;
	}

	public int getFuzzyQueryFuzziness() {
		return fuzzyQueryFuzziness;
	}

	public void setFuzzyQueryFuzziness(int fuzzyQueryFuzziness) {
		this.fuzzyQueryFuzziness = fuzzyQueryFuzziness;
	}

	public double getWildcardQueryBoost() {
		return wildcardQueryBoost;
	}

	public void setWildcardQueryBoost(double wildcardQueryBoost) {
		this.wildcardQueryBoost = wildcardQueryBoost;
	}

	public WildcardQueryType getWildcardQueryType() {
		return wildcardQueryType;
	}

	public void setWildcardQueryType(WildcardQueryType wildcardQueryType) {
		this.wildcardQueryType = wildcardQueryType;
	}
}
