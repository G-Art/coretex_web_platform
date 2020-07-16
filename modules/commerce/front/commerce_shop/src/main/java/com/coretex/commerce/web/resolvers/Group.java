package com.coretex.commerce.web.resolvers;

import java.util.List;
import java.util.Map;

public class Group {

	private final Map<String, List<String>> groupedParams;

	public Group(Map<String, List<String>> groupedParams) {
		this.groupedParams = groupedParams;
	}

	public Map<String, List<String>> getGroupedParams() {
		return groupedParams;
	}
}
