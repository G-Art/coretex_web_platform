package com.coretex.data;


public class SearchResultRowDTO {
	private DataType dataType;
	private Object value;

	public SearchResultRowDTO(Object value, DataType dataType) {
		this.dataType = dataType;
		this.value = value;
	}

	public DataType getDataType() {
		return dataType;
	}

	public Object getValue() {
		return value;
	}
}
