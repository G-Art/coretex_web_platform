package com.coretex.commerce.mapper;

import com.coretex.commerce.data.GenericItemData;
import com.coretex.core.activeorm.query.specs.select.SelectItemsOperationSpec;
import com.coretex.core.activeorm.services.SearchService;
import com.coretex.items.core.GenericItem;
import org.apache.commons.collections4.CollectionUtils;
import org.mapstruct.TargetType;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.UUID;

@Component
public class ReferenceMapper {
	@Resource
	private SearchService searchService;

	public <S extends GenericItemData, T extends GenericItem> T resolve(S reference, @TargetType Class<T> targetClass){

		if(Objects.isNull(reference) || Objects.isNull(reference.getUuid())){
			return null;
		}

		var result = searchService.<T>search(new SelectItemsOperationSpec(reference.getUuid(), targetClass)).getResult();
		if(CollectionUtils.isNotEmpty(result)){
			return result.iterator().next();
		}
		else {
			return null;
		}
	}

	public < T extends GenericItem> T resolve(UUID reference, @TargetType Class<T> targetClass){

		if(Objects.isNull(reference)){
			return null;
		}

		var result = searchService.<T>search(new SelectItemsOperationSpec<>(reference, targetClass)).getResult();
		if(CollectionUtils.isNotEmpty(result)){
			return result.iterator().next();
		}
		else {
			return null;
		}
	}
}
