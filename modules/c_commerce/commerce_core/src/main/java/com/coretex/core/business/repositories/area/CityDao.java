package com.coretex.core.business.repositories.area;

import com.coretex.core.activeorm.dao.Dao;
import com.coretex.items.commerce_core_model.CityItem;


public interface CityDao extends Dao<CityItem> {


	CityItem findByCode(String code);
}
