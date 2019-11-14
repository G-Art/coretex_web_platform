package com.coretex.converter;

import com.coretex.core.activeorm.services.SearchResult;
import com.coretex.data.SearchResultDTO;
import org.springframework.beans.factory.annotation.Autowired;

public class QueryResultConverter {

	@Autowired
	private ItemConverter itemConverter;

	public SearchResultDTO convert(SearchResult searchResult){
		return new SearchResultDTO(searchResult, itemConverter);
	}
}
