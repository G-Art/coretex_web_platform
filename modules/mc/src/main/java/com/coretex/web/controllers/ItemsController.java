package com.coretex.web.controllers;

import com.coretex.core.activeorm.services.SearchService;
import com.coretex.core.services.bootstrap.meta.MetaTypeProvider;
import com.coretex.data.MetaTypeHierarchyDTO;
import com.coretex.data.MetaTypeHierarchyItemDTO;
import com.coretex.data.MetaTypeInfoDTO;
import com.coretex.items.core.GenericItem;
import com.coretex.items.core.MetaRelationTypeItem;
import com.coretex.items.core.MetaTypeItem;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/items")
public class ItemsController extends AbstractController {

	@Resource
	private MetaTypeProvider metaTypeProvider;

	@Resource
	private SearchService searchService;

	@RequestMapping(method = RequestMethod.GET)
	public String items(final Model model) {
		return "/server/items";
	}

	@RequestMapping(method = RequestMethod.GET, path = "/itemtypes")
	public List<Object> getItemTypes() {
		return metaTypeProvider.getAllMetaTypes().stream().map(item -> {
			Map itemMap = Maps.newHashMap();

			itemMap.put(MetaTypeItem.UUID, item.getUuid());
			itemMap.put(MetaTypeItem.TYPE_CODE, item.getTypeCode());
			return itemMap;
		}).collect(Collectors.toList());
	}

	@RequestMapping(method = RequestMethod.GET, path = "/itemtype/info")
	@ResponseBody
	public MetaTypeInfoDTO getMetaTypeInfo(@RequestParam("uuid") String uuid) {
		var metaType = metaTypeProvider.findMetaType(UUID.fromString(uuid));
		return new MetaTypeInfoDTO(metaType, metaTypeProvider.getAllAttributes(metaType).values());
	}

	@RequestMapping(method = RequestMethod.POST, path = "/type/{type}/instances")
	@ResponseBody
	public List<Map> getMetaTypeInfo(@PathVariable("type") String type, @RequestBody final MultiValueMap<String, Object> formData) {
		var attributes = formData.containsKey("attributes") ? List.of(((String)formData.getFirst("attributes")).split(",")): List.of();
		var metaType = metaTypeProvider.findMetaType(type); // required for case if type is not exist
		var attributeTypeItemMap = metaTypeProvider.getAllAttributes(metaType).entrySet()
				.stream()
				.filter(entry -> attributes.contains(entry.getKey()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
		var search = searchService.<GenericItem>search(String.format("SELECT t.* FROM %s as t", metaType.getTypeCode()));

		return search.getResultStream()
				.map(items -> {
					Map params = Maps.newHashMap();
					var itemContext = items.getItemContext();
					attributeTypeItemMap.forEach((s, metaAttributeTypeItem) -> {
						if(metaAttributeTypeItem.getLocalized()){
							params.put(s, itemContext.getLocalizedValues(s));
						}else{
							params.put(s, Objects.toString(itemContext.getValue(s), StringUtils.EMPTY));
						}
					});
					return params;

				}).collect(Collectors.toList());
	}


	@ModelAttribute("metaTypeHierarchy")
	public MetaTypeHierarchyDTO metaTypeHierarchy() {
		var metaType = metaTypeProvider.findMetaType(GenericItem.ITEM_TYPE);
		return new MetaTypeHierarchyDTO(mapHItem(metaType));
	}

	private MetaTypeHierarchyItemDTO mapHItem(MetaTypeItem metaType) {
		var itemDTO = new MetaTypeHierarchyItemDTO(metaType.getUuid(), metaType.getTypeCode());
		itemDTO.setReadOnly(metaType.getAbstract());
		if (!CollectionUtils.isEmpty(metaType.getSubtypes())) {
			itemDTO.setSubtypes(metaType.getSubtypes()
					.stream()
					.filter(t -> !(t instanceof MetaRelationTypeItem))
					.map(this::mapHItem)
					.collect(Collectors.toList()));
		}
		return itemDTO;
	}

}
