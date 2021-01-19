package com.coretex.converter;

import com.coretex.core.activeorm.services.PageableSearchResult;
import com.coretex.data.SearchResultDTO;
import org.springframework.beans.factory.annotation.Autowired;

public class QueryResultConverter {

	@Autowired
	private ItemConverter itemConverter;

	public SearchResultDTO convert(PageableSearchResult searchResult){
		return new SearchResultDTO(searchResult, itemConverter);
	}
}
