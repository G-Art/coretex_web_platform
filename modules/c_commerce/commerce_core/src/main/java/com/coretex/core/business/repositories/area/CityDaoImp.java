package com.coretex.core.business.repositories.area;

import com.coretex.core.activeorm.dao.DefaultGenericDao;
import com.coretex.items.commerce_core_model.CityItem;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CityDaoImp extends DefaultGenericDao<CityItem> implements CityDao {

	public CityDaoImp() {
		super(CityItem.ITEM_TYPE);
	}

	@Override
	public CityItem findByCode(String code) {
		return findSingle(Map.of(CityItem.CODE, code), true);
	}
}