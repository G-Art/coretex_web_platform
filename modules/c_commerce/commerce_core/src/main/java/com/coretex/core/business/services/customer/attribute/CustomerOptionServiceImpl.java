package com.coretex.core.business.services.customer.attribute;

import java.util.List;

import javax.annotation.Resource;

import com.coretex.items.commerce_core_model.CustomerOptionItem;
import com.coretex.items.commerce_core_model.CustomerOptionSetItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import org.springframework.stereotype.Service;

import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.business.repositories.customer.attribute.CustomerOptionDao;
import com.coretex.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.coretex.items.commerce_core_model.CustomerAttributeItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;


@Service("customerOptionService")
public class CustomerOptionServiceImpl extends
		SalesManagerEntityServiceImpl<CustomerOptionItem> implements CustomerOptionService {


	private CustomerOptionDao customerOptionDao;

	@Resource
	private CustomerAttributeService customerAttributeService;

	@Resource
	private CustomerOptionSetService customerOptionSetService;


	public CustomerOptionServiceImpl(
			CustomerOptionDao customerOptionDao) {
		super(customerOptionDao);
		this.customerOptionDao = customerOptionDao;
	}

	@Override
	public List<CustomerOptionItem> listByStore(MerchantStoreItem store, LanguageItem language) throws ServiceException {

		return customerOptionDao.findByStore(store.getUuid(), language.getUuid());

	}


	@Override
	public void saveOrUpdate(CustomerOptionItem entity) throws ServiceException {

		super.save(entity);
	}


	@Override
	public void delete(CustomerOptionItem customerOption) {

		//remove all attributes having this option
		List<CustomerAttributeItem> attributes = customerAttributeService.getByOptionId(customerOption.getMerchantStore(), customerOption.getUuid());

		for (CustomerAttributeItem attribute : attributes) {
			customerAttributeService.delete(attribute);
		}

		CustomerOptionItem option = this.getById(customerOption.getUuid());

		List<CustomerOptionSetItem> optionSets = customerOptionSetService.listByOption(customerOption, customerOption.getMerchantStore());

		for (CustomerOptionSetItem optionSet : optionSets) {
			customerOptionSetService.delete(optionSet);
		}

		//remove option
		super.delete(option);

	}

	@Override
	public CustomerOptionItem getByCode(MerchantStoreItem store, String optionCode) {
		return customerOptionDao.findByCode(store.getUuid(), optionCode);
	}


}
