package com.coretex.web.controllers;

import com.coretex.core.services.bootstrap.meta.MetaTypeProvider;
import com.coretex.items.core.MetaTypeItem;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/items")
public class ItemsController extends AbstractController {

	@Autowired
	private MetaTypeProvider metaTypeProvider;

	@RequestMapping(method = RequestMethod.GET)
	public String items(final Model model) {
		return "/server/items";
	}

	@RequestMapping(method = RequestMethod.GET, path = "/itemtypes")
	public List<Object> getItemTypes(){
		return metaTypeProvider.getAllMetaTypes().stream().map( item -> {
			Map itemMap = Maps.newHashMap();

			itemMap.put(MetaTypeItem.UUID, item.getUuid());
			itemMap.put(MetaTypeItem.TYPE_CODE, item.getTypeCode());
			return itemMap;
		}).collect(Collectors.toList());
	}

}
