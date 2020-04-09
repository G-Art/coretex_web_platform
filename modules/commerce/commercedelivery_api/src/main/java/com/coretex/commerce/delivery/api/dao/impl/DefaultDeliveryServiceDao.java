package com.coretex.commerce.delivery.api.dao.impl;

import com.coretex.commerce.delivery.api.dao.DeliveryServiceDao;
import com.coretex.core.activeorm.dao.DefaultGenericDao;
import com.coretex.core.activeorm.exceptions.AmbiguousResultException;
import com.coretex.core.activeorm.services.SearchResult;
import com.coretex.core.utils.TypeUtil;
import com.coretex.items.cx_commercedelivery_api.DeliveryServiceItem;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
public class DefaultDeliveryServiceDao extends DefaultGenericDao<DeliveryServiceItem> implements DeliveryServiceDao {

	public DefaultDeliveryServiceDao() {
		super(DeliveryServiceItem.ITEM_TYPE);
	}

	@Override
	public  <T extends DeliveryServiceItem> T getByUUIDAndType(UUID uuid, Class<T> type){
		var query = "SELECT * FROM #"+ TypeUtil.getMetaTypeCode(type)+ " AS ds WHERE ds.uuid = :uuid";
		SearchResult<T> searchResult = this.getSearchService().search(query, Map.of("uuid", uuid));
		var result = searchResult.getResult();

		if (CollectionUtils.isNotEmpty(result) && result.size() > 1 ) {
			throw new AmbiguousResultException(String.format("Result contain more than one result (result count: %s)", result.size()));
		}
		if(result.isEmpty()){
			return null;
		}else{
			return result.get(0);
		}
	}
}
