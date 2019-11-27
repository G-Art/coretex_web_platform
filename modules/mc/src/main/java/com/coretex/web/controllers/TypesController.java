package com.coretex.web.controllers;

import com.coretex.converter.QueryResultConverter;
import com.coretex.core.activeorm.services.ItemService;
import com.coretex.core.activeorm.services.SearchResult;
import com.coretex.core.activeorm.services.SearchService;
import com.coretex.core.services.bootstrap.meta.MetaTypeProvider;
import com.coretex.data.MetaTypeDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/types")
public class TypesController extends AbstractController {

	@Resource
	private MetaTypeProvider metaTypeProvider;

	@Resource
	private SearchService searchService;

	@Resource
	private QueryResultConverter queryResultConverter;

	@Resource
	private ItemService itemService;

	@RequestMapping(method = RequestMethod.GET)
	public String types(final Model model) {
		model.addAttribute("mataTypes", metaTypeProvider.getAllMetaTypes().stream().map(MetaTypeDTO::new).collect(Collectors.toList()));
		return "/server/types";
	}

	@RequestMapping(method = RequestMethod.GET, path = "/query")
	public String query() {
		return "/server/query";
	}

	@RequestMapping(method = RequestMethod.POST, path = "/query")
	@ResponseBody
	public Object executeQuery(@RequestBody final MultiValueMap<String, String>  formData) {
		SearchResult<Object> resultSet = searchService.search(formData.getFirst("query"));
		return queryResultConverter.convert(resultSet);
	}
}
