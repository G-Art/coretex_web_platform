package com.coretex.core.business.repositories.customer.attribute;

import com.coretex.core.activeorm.dao.DefaultGenericDao;
import com.coretex.items.commerce_core_model.CustomerAttributeItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class CustomerAttributeDaoImpl extends DefaultGenericDao<CustomerAttributeItem> implements CustomerAttributeDao{


	public CustomerAttributeDaoImpl() {
		super(CustomerAttributeItem.ITEM_TYPE);
	}

	@Override
	public CustomerAttributeItem findOne(UUID id) {
		return find(id);
	}

	@Override
	public CustomerAttributeItem findByOptionId(UUID merchantId, UUID customerId, UUID id) {
		return null;
	}

	@Override
	public List<CustomerAttributeItem> findByOptionId(UUID merchantId, UUID id) {
		return null;
	}

	@Override
	public List<CustomerAttributeItem> findByCustomerId(UUID merchantId, UUID customerId) {
		return null;
	}

	@Override
	public List<CustomerAttributeItem> findByOptionValueId(UUID merchantId, UUID Id) {
		return null;
	}
}
