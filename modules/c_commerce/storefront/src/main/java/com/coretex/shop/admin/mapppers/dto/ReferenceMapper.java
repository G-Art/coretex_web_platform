package com.coretex.shop.admin.mapppers.dto;

import com.coretex.core.activeorm.query.specs.select.SelectItemsOperationSpec;
import com.coretex.core.activeorm.services.SearchService;
import com.coretex.items.core.GenericItem;
import com.coretex.shop.admin.data.GenericDto;
import org.apache.commons.collections4.CollectionUtils;
import org.mapstruct.TargetType;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

@Component
public class ReferenceMapper {
	@Resource
	private SearchService searchService;

	public <S extends GenericDto, T extends GenericItem> T resolve(S reference, @TargetType Class<T> targetClass){

		if(Objects.isNull(reference) || Objects.isNull(reference.getUuid())){
			return null;
		}

		var result = searchService.search(new SelectItemsOperationSpec<>(reference.getUuid(), targetClass)).getResult();
		if(CollectionUtils.isNotEmpty(result)){
			return result.iterator().next();
		}
		else {
			return null;
		}
	}
}
