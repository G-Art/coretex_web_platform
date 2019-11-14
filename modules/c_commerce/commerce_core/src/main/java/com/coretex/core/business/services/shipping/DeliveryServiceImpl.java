package com.coretex.core.business.services.shipping;

import com.coretex.core.business.repositories.shipping.DeliveryServiceDao;
import com.coretex.core.model.shipping.ShippingProduct;
import com.coretex.core.model.shipping.ShippingQuote;
import com.coretex.core.utils.TypeUtil;
import com.coretex.items.commerce_core_model.DeliveryItem;
import com.coretex.items.commerce_core_model.DeliveryServiceItem;
import com.coretex.items.commerce_core_model.LanguageItem;
import com.coretex.items.commerce_core_model.MerchantStoreItem;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service("deliveryService")
public class DeliveryServiceImpl implements DeliveryService {

	@Resource
	private DeliveryServiceDao deliveryServiceDao;

	@Override
	public List<? extends DeliveryServiceItem> getAllDeliveryServices() {
		return deliveryServiceDao.find();
	}

	@Override
	public List<? extends DeliveryServiceItem> getAvailableDeliveryServicesForStore(MerchantStoreItem merchantStoreItem) {
		return deliveryServiceDao.findActiveForStore(merchantStoreItem);
	}

	@Override
	public DeliveryServiceItem getByCode(String code) {
		return deliveryServiceDao.findSingle(Map.of(DeliveryServiceItem.CODE, code), true);
	}

	@Override
	public DeliveryServiceItem getTypeByCode(Class<? extends DeliveryServiceItem> type, String code) {
		return deliveryServiceDao.findActiveTypeForCode(TypeUtil.getMetaTypeCode(type), code);
	}

	@Override
	public DeliveryServiceItem getByUUID(String uuid) {
		return deliveryServiceDao.find(UUID.fromString(uuid));
	}

	@Override
	public ShippingQuote getShippingQuote(UUID shoppingCartId, MerchantStoreItem store, DeliveryItem delivery, List<ShippingProduct> products, LanguageItem language) {
		var sq = new ShippingQuote();
		return sq;
	}
}
