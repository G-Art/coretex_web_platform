package com.coretex.shop.store.model.filter;

import java.util.UUID;

/**
 * Used in CategoryItem and Search to filter display based on other
 * entities such as ManufacturerItem
 *
 * @author Carl Samson
 */
public class QueryFilter {

	/**
	 * used when filtering on an entity code (example property)
	 */
	private String filterCode;
	/**
	 * used when filtering on an entity id
	 */
	private UUID filterId;
	private QueryFilterType filterType;

	public String getFilterCode() {
		return filterCode;
	}

	public void setFilterCode(String filterCode) {
		this.filterCode = filterCode;
	}

	public UUID getFilterId() {
		return filterId;
	}

	public void setFilterId(UUID filterId) {
		this.filterId = filterId;
	}

	public QueryFilterType getFilterType() {
		return filterType;
	}

	public void setFilterType(QueryFilterType filterType) {
		this.filterType = filterType;
	}

}
