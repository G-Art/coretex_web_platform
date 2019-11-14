package com.coretex.core.business.repositories.area;

import com.coretex.core.activeorm.dao.Dao;
import com.coretex.items.commerce_core_model.CityItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.items.core.CountryItem;

import java.util.List;
import java.util.UUID;


public interface CityDao extends Dao<CityItem> {


	CityItem findByCode(String code);
}
