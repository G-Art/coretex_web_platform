package com.coretex.core.business.services.shipping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.coretex.core.business.repositories.shipping.ShippingOriginDao;
import com.coretex.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import com.coretex.items.commerce_core_model.ShippingOriginItem;


@Service("shippingOriginService")
public class ShippingOriginServiceImpl extends SalesManagerEntityServiceImpl<ShippingOriginItem> implements ShippingOriginService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ShippingOriginServiceImpl.class);

	private ShippingOriginDao shippingOriginDao;

	public ShippingOriginServiceImpl(ShippingOriginDao shippingOriginDao) {
		super(shippingOriginDao);
		this.shippingOriginDao = shippingOriginDao;
	}


	@Override
	public ShippingOriginItem getByStore(MerchantStoreItem store) {
		// TODO Auto-generated method stub
		ShippingOriginItem origin = shippingOriginDao.findByStore(store.getUuid());
		return origin;
	}


}
