package com.coretex.core.business.services.customer.attribute;

import java.util.List;

import javax.annotation.Resource;

import com.coretex.items.commerce_core_model.CustomerAttributeItem;
import com.coretex.items.commerce_core_model.CustomerOptionSetItem;
import com.coretex.items.commerce_core_model.CustomerOptionValueItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import org.springframework.stereotype.Service;

import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.business.repositories.customer.attribute.CustomerOptionValueDao;
import com.coretex.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.coretex.items.commerce_core_model.LanguageItem;


@Service("customerOptionValueService")
public class CustomerOptionValueServiceImpl extends
		SalesManagerEntityServiceImpl<CustomerOptionValueItem> implements
		CustomerOptionValueService {

	@Resource
	private CustomerAttributeService customerAttributeService;

	private CustomerOptionValueDao customerOptionValueDao;

	@Resource
	private CustomerOptionSetService customerOptionSetService;

	public CustomerOptionValueServiceImpl(
			CustomerOptionValueDao customerOptionValueDao) {
		super(customerOptionValueDao);
		this.customerOptionValueDao = customerOptionValueDao;
	}


	@Override
	public List<CustomerOptionValueItem> listByStore(MerchantStoreItem store, LanguageItem language) throws ServiceException {

		return customerOptionValueDao.findByStore(store.getUuid(), language.getUuid());
	}


	@Override
	public void saveOrUpdate(CustomerOptionValueItem entity) throws ServiceException {

		super.save(entity);
	}


	public void delete(CustomerOptionValueItem customerOptionValue) {

		//remove all attributes having this option
		List<CustomerAttributeItem> attributes = customerAttributeService.getByCustomerOptionValueId(customerOptionValue.getMerchantStore(), customerOptionValue.getUuid());

		for (CustomerAttributeItem attribute : attributes) {
			customerAttributeService.delete(attribute);
		}

		List<CustomerOptionSetItem> optionSets = customerOptionSetService.listByOptionValue(customerOptionValue, customerOptionValue.getMerchantStore());

		for (CustomerOptionSetItem optionSet : optionSets) {
			customerOptionSetService.delete(optionSet);
		}

		CustomerOptionValueItem option = super.getById(customerOptionValue.getUuid());

		//remove option
		super.delete(option);

	}

	@Override
	public CustomerOptionValueItem getByCode(MerchantStoreItem store, String optionValueCode) {
		return customerOptionValueDao.findByCode(store.getUuid(), optionValueCode);
	}


}
