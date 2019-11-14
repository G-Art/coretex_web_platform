package com.coretex.core.business.services.customer.attribute;

import java.util.List;

import javax.annotation.Resource;

import com.coretex.items.commerce_core_model.CustomerOptionItem;
import com.coretex.items.commerce_core_model.CustomerOptionValueItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Service;

import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.business.repositories.customer.attribute.CustomerOptionSetDao;
import com.coretex.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.coretex.items.commerce_core_model.CustomerOptionSetItem;


@Service("customerOptionSetService")
public class CustomerOptionSetServiceImpl extends
		SalesManagerEntityServiceImpl<CustomerOptionSetItem> implements CustomerOptionSetService {


	@Resource
	private CustomerOptionSetDao customerOptionSetDao;

	public CustomerOptionSetServiceImpl(
			CustomerOptionSetDao customerOptionSetDao) {
		super(customerOptionSetDao);
		this.customerOptionSetDao = customerOptionSetDao;
	}


	@Override
	public List<CustomerOptionSetItem> listByOption(CustomerOptionItem option, MerchantStoreItem store) {
		Validate.notNull(store, "merchant store cannot be null");
		Validate.notNull(option, "option cannot be null");

		return customerOptionSetDao.findByOptionId(store.getUuid(), option.getUuid());
	}

	@Override
	public void delete(CustomerOptionSetItem customerOptionSet) {
		customerOptionSet = customerOptionSetDao.findOne(customerOptionSet.getUuid());
		super.delete(customerOptionSet);
	}

	@Override
	public List<CustomerOptionSetItem> listByStore(MerchantStoreItem store, LanguageItem language) throws ServiceException {
		Validate.notNull(store, "merchant store cannot be null");
		return customerOptionSetDao.findByStore(store.getUuid(), language.getUuid());
	}


	@Override
	public void saveOrUpdate(CustomerOptionSetItem entity) throws ServiceException {
		Validate.notNull(entity, "customer option set cannot be null");
		super.save(entity);

	}


	@Override
	public List<CustomerOptionSetItem> listByOptionValue(
			CustomerOptionValueItem optionValue, MerchantStoreItem store) {
		return customerOptionSetDao.findByOptionValueId(store.getUuid(), optionValue.getUuid());
	}


}
