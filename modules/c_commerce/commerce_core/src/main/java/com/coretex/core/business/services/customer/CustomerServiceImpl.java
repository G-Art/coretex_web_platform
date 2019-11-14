package com.coretex.core.business.services.customer;

import com.coretex.core.business.exception.ServiceException;
import com.coretex.core.business.repositories.customer.CustomerDao;
import com.coretex.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.coretex.core.business.services.customer.attribute.CustomerAttributeService;
import com.coretex.core.model.common.Address;
import com.coretex.core.model.customer.CustomerCriteria;
import com.coretex.core.model.customer.CustomerList;
import com.coretex.core.business.utils.GeoLocation;
import com.coretex.items.commerce_core_model.CustomerAttributeItem;
import com.coretex.items.commerce_core_model.CustomerItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;


@Service("customerService")
public class CustomerServiceImpl extends SalesManagerEntityServiceImpl<CustomerItem> implements CustomerService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerServiceImpl.class);

	private CustomerDao customerDao;

	@Resource
	private CustomerAttributeService customerAttributeService;

	@Resource
	private GeoLocation geoLocation;

	public CustomerServiceImpl(CustomerDao customerDao) {
		super(customerDao);
		this.customerDao = customerDao;
	}

	@Override
	public List<CustomerItem> getByName(String firstName) {
		return customerDao.findByName(firstName);
	}


	@Override
	public CustomerItem getByNick(String nick) {
		return customerDao.findByNick(nick);
	}

	@Override
	public CustomerItem getByEmail(String email) {
		return customerDao.findByEmail(email);
	}


	@Override
	public CustomerItem getByNick(String nick, UUID storeId) {
		return customerDao.findByNick(nick, storeId);
	}

	@Override
	public CustomerList getListByStore(MerchantStoreItem store, CustomerCriteria criteria) {
		return customerDao.listByStore(store, criteria);
	}

	@Override
	public Address getCustomerAddress(MerchantStoreItem store, String ipAddress) throws ServiceException {

		try {
			return geoLocation.getAddress(ipAddress);
		} catch (Exception e) {
			throw new ServiceException(e);
		}

	}

	@Override
	public void saveOrUpdate(CustomerItem customer) throws ServiceException {

		LOGGER.debug("Creating CustomerItem");
		super.save(customer);
	}

	public void delete(CustomerItem customer) {
		customer = getById(customer.getUuid());

		//delete attributes
		List<CustomerAttributeItem> attributes = customerAttributeService.getByCustomer(customer.getMerchantStore(), customer);
		if (attributes != null) {
			for (CustomerAttributeItem attribute : attributes) {
				customerAttributeService.delete(attribute);
			}
		}
		customerDao.delete(customer);

	}


}
