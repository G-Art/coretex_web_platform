package com.coretex.core.business.repositories.customer;


import com.coretex.core.activeorm.dao.DefaultGenericDao;
import com.coretex.items.commerce_core_model.BillingItem;
import com.coretex.items.cx_core.CustomerItem;
import com.coretex.items.core.CountryItem;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import com.coretex.core.model.customer.CustomerCriteria;
import com.coretex.core.model.customer.CustomerList;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class CustomerDaoImpl extends DefaultGenericDao<CustomerItem> implements CustomerDao {

	public CustomerDaoImpl() {
		super(CustomerItem.ITEM_TYPE);
	}

	@SuppressWarnings("unchecked")
	@Override
	public CustomerList listByStore(MerchantStoreItem store, CustomerCriteria criteria) {

		var objectBuilderSelect = new StringBuilder();
		objectBuilderSelect.append("SELECT c.* FROM " + CustomerItem.ITEM_TYPE + " AS c ");

		if (!StringUtils.isBlank(criteria.getName())
				|| !StringUtils.isBlank(criteria.getFirstName())
				|| !StringUtils.isBlank(criteria.getLastName())
				|| !StringUtils.isBlank(criteria.getCountry())) {

			objectBuilderSelect.append("JOIN " + BillingItem.ITEM_TYPE + " AS b ON (b.uuid = c.billing) ");
		}
		if (!StringUtils.isBlank(criteria.getCountry())) {

			objectBuilderSelect.append("JOIN " + CountryItem.ITEM_TYPE + " AS cry ON (cry.uuid = b.country) ");
		}

		String whereQuery = "WHERE c.merchantStore=:mId ";
		objectBuilderSelect.append(whereQuery);

		if (!StringUtils.isBlank(criteria.getName())) {
			String nameQuery = "AND b.firstName like:nm OR b.lastName like:nm ";
			objectBuilderSelect.append(nameQuery);
		}

		if (!StringUtils.isBlank(criteria.getFirstName())) {
			String nameQuery = "AND b.firstName like:fn ";
			objectBuilderSelect.append(nameQuery);
		}

		if (!StringUtils.isBlank(criteria.getLastName())) {
			String nameQuery = "AND b.lastName like:ln ";
			objectBuilderSelect.append(nameQuery);
		}

		if (!StringUtils.isBlank(criteria.getEmail())) {
			String mailQuery = "AND c.emailAddress like:email ";
			objectBuilderSelect.append(mailQuery);
		}

		if (!StringUtils.isBlank(criteria.getCountry())) {
			String countryQuery = "AND cry.isoCode like:country ";
			objectBuilderSelect.append(countryQuery);
		}

		Map params = Maps.newHashMap();

		params.put("mId", store.getUuid());


		if (!StringUtils.isBlank(criteria.getName())) {
			String nameParam = "%" + criteria.getName() + "%";
			params.put("nm", nameParam);
		}

		if (!StringUtils.isBlank(criteria.getFirstName())) {
			String nameParam = "%" + criteria.getFirstName() + "%";
			params.put("fn", nameParam);
		}

		if (!StringUtils.isBlank(criteria.getLastName())) {
			String nameParam = "%" + criteria.getLastName() + "%";
			params.put("ln", nameParam);
		}

		if (!StringUtils.isBlank(criteria.getEmail())) {
			String email = "%" + criteria.getEmail() + "%";
			params.put("email", email);
		}

		if (!StringUtils.isBlank(criteria.getCountry())) {
			String country = "%" + criteria.getCountry() + "%";
			params.put("country", country);
		}

		var customerList = new CustomerList();
		var result = find(objectBuilderSelect.toString(), params);
		customerList.setTotalCount(result.size());
		customerList.setCustomers(result);

		return customerList;
	}


	@Override
	public CustomerItem findOne(UUID id) {
		return find(id);
	}

	@Override
	public CustomerItem findByEmail(String email) {
		return findSingle(Map.of(CustomerItem.EMAIL, email), true);
	}

	@Override
	public List<CustomerItem> findByName(String name) {
		String query = "SELECT c.* FROM " + CustomerItem.ITEM_TYPE + " AS c " +
				"JOIN " + BillingItem.ITEM_TYPE + " AS b ON (b.uuid = c.billing) " +
				"WHERE b.firstName like:fn";
		return find(query, Map.of("fn", "%" + name + "%"));
	}


	@Override
	public List<CustomerItem> findByStore(UUID storeId) {
		return find(Map.of(CustomerItem.STORE, storeId));
	}
}
