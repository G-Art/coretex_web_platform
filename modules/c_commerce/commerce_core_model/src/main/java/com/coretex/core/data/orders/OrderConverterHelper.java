package com.coretex.core.data.orders;

import com.coretex.items.commerce_core_model.BillingItem;
import com.coretex.items.commerce_core_model.DeliveryItem;
import com.coretex.items.cx_core.AddressItem;
import com.coretex.items.cx_core.ZoneItem;

import java.util.Objects;

public class OrderConverterHelper {

	public static Billing convertBilling(BillingItem billingItem) {
		var billing = new Billing();
		if (Objects.nonNull(billingItem)) {
			convertAbstractAddress(billingItem, billing);
		}
		return billing;
	}

	public static Delivery convertDelivery(DeliveryItem deliveryItem) {
		var delivery = new Delivery();
		if (Objects.nonNull(deliveryItem)) {
			convertAbstractAddress(deliveryItem, delivery);
		}
		return delivery;
	}

	private static <T extends AddressItem, R extends AbstractAddress> void convertAbstractAddress(T source, R target) {
		target.setFirstName(source.getFirstName());
		target.setLastName(source.getLastName());
		target.setZone(convertZone(source.getZone()));
		target.setAddress(source.getAddressLine1());
		target.setState(source.getState());
		target.setCity(source.getCity());
		target.setPostalCode(source.getPostalCode());
		target.setTelephone(source.getPhone());
	}

	public static Zone convertZone(ZoneItem zoneItem) {
		var zone = new Zone();
		if (Objects.nonNull(zoneItem)) {
			zone.setCode(zoneItem.getCode());
		}
		return zone;
	}

	public static DeliveryItem convertBillingItem(Delivery delivery) {
		var deliveryItem = new DeliveryItem();
		convertAbstractAddressItem(delivery, deliveryItem);
		return deliveryItem;
	}

	public static BillingItem convertDeliveryItem(Billing billing) {
		var billingItem = new BillingItem();
		convertAbstractAddressItem(billing, billingItem);
		return billingItem;
	}

	private static <T extends AbstractAddress, R extends AddressItem> void convertAbstractAddressItem(T source, R target) {
		target.setFirstName(source.getFirstName());
		target.setLastName(source.getLastName());
		target.setAddressLine1(source.getAddress());
		target.setState(source.getState());
		target.setCity(source.getCity());
		target.setPostalCode(source.getPostalCode());
		target.setPhone(source.getTelephone());
	}
}
